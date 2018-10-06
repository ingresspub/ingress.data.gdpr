/*
 * Copyright (C) 2014-2018 SgrAlpha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ingress.data.gdpr.web.services;

import static ingress.data.gdpr.models.utils.Preconditions.notNull;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ingress.data.gdpr.models.analyzed.Circle;
import ingress.data.gdpr.models.records.CommMention;
import ingress.data.gdpr.models.records.GameLog;
import ingress.data.gdpr.models.reports.RawDataReport;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.RawDataParser;
import io.sgr.geometry.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

/**
 * @author SgrAlpha
 */
@Service
public class Summarizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Summarizer.class);

    private final DataSource primaryDataSource;
    private final JdbcTemplate jdbcTemplate;

    public Summarizer(
            @Qualifier("primaryDataSource") final DataSource primaryDataSource,
            @Qualifier("primaryJdbcTemplate") final JdbcTemplate jdbcTemplate) {
        notNull(primaryDataSource, "Missing DataSource");
        this.primaryDataSource = primaryDataSource;
        notNull(jdbcTemplate, "Missing JDBC template");
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.query("SELECT 1", (RowCallbackHandler) -> {
        });
    }

    public boolean noDataUploaded() {
        return countGameLogs() <= 0;
    }

    public List<Circle> listCapturedPortals(final String color, final int radius) {
        final String sql = "SELECT DISTINCT loc_latE6, loc_lngE6 FROM gdpr_raw_game_logs WHERE tracker_trigger = 'captured portal'";
        return listPortals(sql, color, radius);
    }

    public List<Circle> listVisitedPortals(final String color, final int radius) {
        final String sql = "SELECT DISTINCT loc_latE6, loc_lngE6 FROM gdpr_raw_game_logs"
                + " WHERE tracker_trigger"
                + " IN ('captured portal','hacked enemy portal','hacked friendly portal','mod deployed','resonator deployed','resonator upgraded')"
                + " AND comment != 'fail'";
        return listPortals(sql, color, radius);
    }

    private List<Circle> listPortals(final String sql, final String color, final int radius) {
        return jdbcTemplate.query(sql, rs -> {
            List<Circle> markers = new LinkedList<>();
            while (rs.next()) {
                final Coordinate latLng = new Coordinate(rs.getInt("loc_latE6") / 1e6, rs.getInt("loc_lngE6") / 1e6);
                markers.add(new Circle(latLng, color, radius));
            }
            return markers;
        });
    }

    private int countGameLogs() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM gdpr_raw_game_logs", Integer.class);
        return Optional.ofNullable(count).orElse(0);
    }

    public void parseAndSaveRawDataReport(final List<Path> files) {
        RawDataReport report = RawDataParser.parse(files);
        if (report == null) {
            LOGGER.warn("Unable to parse from raw data files.");
            return;
        }
        rebuildRawDateTables();

        final int cores = Runtime.getRuntime().availableProcessors();
        final Executor executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(files.size()),
                new ThreadFactoryBuilder().setNameFormat("importer-pool-%s").build()
        );
        CompletableFuture
                .allOf(
                        persistGameLogs(report.getGameLogs().orElse(null), executor),
                        persistCommMentions(report.getCommMentions().orElse(null), executor)
                )
                .join();
    }

    private CompletableFuture<Void> persistCommMentions(final ReportDetails<List<CommMention>> commMentions, final Executor executor) {
        if (commMentions == null || !commMentions.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final List<CommMention> mentions = commMentions.getData();
            final String sql = "INSERT INTO gdpr_raw_comm_mentions(time,message) VALUES(?,?)";
            final List<List<CommMention>> batches = Lists.partition(mentions, 1000);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final CommMention mention = batch.get(i);
                    ps.setLong(1, mention.getTime().toInstant().getEpochSecond());
                    ps.setString(2, mention.getMessage());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} comm mentions.", mentions.size());
        }, executor);
    }

    private CompletableFuture<Void> persistGameLogs(final ReportDetails<List<GameLog>> gameLogs, final Executor executor) {
        if (gameLogs == null || !gameLogs.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final List<GameLog> logs = gameLogs.getData();
            final String sql = "INSERT INTO gdpr_raw_game_logs(time,loc_latE6,loc_lngE6,tracker_trigger,comment) VALUES(?,?,?,?,?)";
            final int batchSize = 1000;
            final List<List<GameLog>> batches = Lists.partition(new ArrayList<>(logs), batchSize);
            LOGGER.info("Divided {} logs into {} batches, each batch contains {} records.", logs.size(), batches.size(), batchSize);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final GameLog log = batch.get(i);
                    ps.setLong(1, log.getTime().toInstant().getEpochSecond());
                    final Optional<Coordinate> loc = log.getLocation();
                    if (loc.isPresent()) {
                        ps.setInt(2, loc.get().getLatE6());
                        ps.setInt(3, loc.get().getLngE6());
                    } else {
                        ps.setInt(2, Types.INTEGER);
                        ps.setInt(3, Types.INTEGER);
                    }
                    ps.setString(4, log.getTrackerTrigger());
                    ps.setString(5, log.getComments());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            int total = countGameLogs();
            if (total != logs.size()) {
                LOGGER.warn("Saved {} of {} game logs into database.", total, logs.size());
            } else {
                LOGGER.info("Saved total {} game logs into database.", total);
            }
            long count = logs.stream()
                    .filter(log -> log.getLocation().isPresent() && log.getTrackerTrigger().equals("captured portal"))
                    .map(log -> log.getLocation().get())
                    .distinct()
                    .count();
            LOGGER.info("UPC: {}", count);
        }, executor);
    }

    private void rebuildRawDateTables() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("db/rebuild_raw_tables.sql"));
        databasePopulator.execute(primaryDataSource);
    }

}
