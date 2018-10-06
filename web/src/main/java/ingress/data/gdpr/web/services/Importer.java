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
import ingress.data.gdpr.models.records.CommMention;
import ingress.data.gdpr.models.records.GameLog;
import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.records.opr.OprAssignmentLogItem;
import ingress.data.gdpr.models.records.opr.OprProfile;
import ingress.data.gdpr.models.records.opr.OprSubmissionLogItem;
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
public class Importer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Importer.class);
    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final DataSource primaryDataSource;
    private final JdbcTemplate jdbcTemplate;

    private final Executor executor;

    public Importer(
            @Qualifier("primaryDataSource") final DataSource primaryDataSource,
            @Qualifier("primaryJdbcTemplate") final JdbcTemplate jdbcTemplate) {
        notNull(primaryDataSource, "Missing DataSource");
        this.primaryDataSource = primaryDataSource;
        notNull(jdbcTemplate, "Missing JDBC template");
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.query("SELECT 1", (RowCallbackHandler) -> {
        });
        final int cores = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new ThreadFactoryBuilder().setNameFormat("importer-pool-%s").build()
        );
    }

    public void parseAndSaveRawDataReport(final List<Path> files) {
        RawDataReport report = RawDataParser.parse(files);
        if (report == null) {
            LOGGER.warn("Unable to parse from raw data files.");
            return;
        }
        rebuildRawDateTables();

        CompletableFuture
                .allOf(
                        persistGameLogs(report.getGameLogs()),
                        persistCommMentions(report.getCommMentions()),
                        persistUsedDevices(report.getUsedDevices()),
                        persistOprProfile(report.getOprProfile()),
                        persistOprAgreements(report.getOprAgreements()),
                        persistOprAssignments(report.getOprAssignmentLogs()),
                        persistOprSubmissions(report.getOprSubmissionLogs()),
                        persistAllPortalsApproved(report.getAllPortalsApproved()),
                        persistSeerPortals(report.getSeerPortals()),
                        persistPortalsVisited(report.getPortalsVisited()),
                        persistXmCollected(report.getXmCollected())
                )
                .join();
        LOGGER.info("Finished saving all data into database.");
    }

    private CompletableFuture<Void> persistXmCollected(final ReportDetails<List<TimestampedRecord<Float>>> xmCollected) {
        if (xmCollected == null || !xmCollected.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Float>> records = xmCollected.getData();
        if (records.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_xm_collected(time,value) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Float>>> batches = Lists.partition(new ArrayList<>(records), DEFAULT_BATCH_SIZE);
            bulkSaveTimestampedFloat(sql, batches);
            LOGGER.info("Saved {} XM collected records.", records.size());
        }, executor);
    }

    private CompletableFuture<Void> persistPortalsVisited(final ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited) {
        if (portalsVisited == null || !portalsVisited.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Integer>> portals = portalsVisited.getData();
        if (portals.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_portals_visited(time,portal_id) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Integer>>> batches = Lists.partition(new ArrayList<>(portals), DEFAULT_BATCH_SIZE);
            bulkSaveTimestampedInteger(sql, batches);
            LOGGER.info("Saved {} visited portals.", portals.size());
        }, executor);
    }

    private CompletableFuture<Void> persistSeerPortals(final ReportDetails<List<TimestampedRecord<Integer>>> seerPortals) {
        if (seerPortals == null || !seerPortals.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Integer>> portals = seerPortals.getData();
        if (portals.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_seer_portals(time,portal_id) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Integer>>> batches = Lists.partition(new ArrayList<>(portals), DEFAULT_BATCH_SIZE);
            bulkSaveTimestampedInteger(sql, batches);
            LOGGER.info("Saved {} seer portals.", portals.size());
        }, executor);
    }

    private CompletableFuture<Void> persistAllPortalsApproved(final ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved) {
        if (allPortalsApproved == null || !allPortalsApproved.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Integer>> approvals = allPortalsApproved.getData();
        if (approvals.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_all_portals_approved(time,portal_id) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Integer>>> batches = Lists.partition(new ArrayList<>(approvals), DEFAULT_BATCH_SIZE);
            bulkSaveTimestampedInteger(sql, batches);
            LOGGER.info("Saved {} approved portals.", approvals.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprSubmissions(final ReportDetails<List<OprSubmissionLogItem>> oprSubmissions) {
        if (oprSubmissions == null || !oprSubmissions.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<OprSubmissionLogItem> submissions = oprSubmissions.getData();
        if (submissions.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_opr_submissions"
                + "(candidate_id,assigned_time,comment,rating_for_cultural,rating_for_text,is_duplicate,duplicate_to,rating_for_location,"
                + "suggested_loc_latE6,suggested_loc_lngE6,rating_for_quality,rating_for_safety,is_one_star,"
                + "submission_time,rating_for_uniqueness,what_is_it)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<OprSubmissionLogItem>> batches = Lists.partition(new ArrayList<>(submissions), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final OprSubmissionLogItem record = batch.get(i);
                    ps.setString(1, record.getCandidateId());
                    ps.setLong(2, record.getAssignedTime().toInstant().getEpochSecond());
                    ps.setString(3, record.getComment().orElse(null));
                    if (record.getRatingForCultural().isPresent()) {
                        ps.setInt(4, record.getRatingForCultural().get());
                    } else {
                        ps.setNull(4, Types.INTEGER);
                    }
                    if (record.getRatingForText().isPresent()) {
                        ps.setInt(5, record.getRatingForText().get());
                    } else {
                        ps.setNull(5, Types.INTEGER);
                    }
                    ps.setBoolean(6, record.isDuplicate());
                    ps.setString(7, record.getDuplicateTo().orElse(null));
                    if (record.getRatingForLocation().isPresent()) {
                        ps.setInt(8, record.getRatingForLocation().get());
                    } else {
                        ps.setNull(8, Types.INTEGER);
                    }
                    if (record.getSuggestedLocation().isPresent()) {
                        final Coordinate loc = record.getSuggestedLocation().get();
                        ps.setInt(9, loc.getLatE6());
                        ps.setInt(10, loc.getLngE6());
                    } else {
                        ps.setNull(9, Types.INTEGER);
                        ps.setNull(10, Types.INTEGER);
                    }
                    if (record.getRatingForQuality().isPresent()) {
                        ps.setInt(11, record.getRatingForQuality().get());
                    } else {
                        ps.setNull(11, Types.INTEGER);
                    }
                    if (record.getRatingForSafety().isPresent()) {
                        ps.setInt(12, record.getRatingForSafety().get());
                    } else {
                        ps.setNull(12, Types.INTEGER);
                    }
                    ps.setBoolean(13, record.isOneStar());
                    ps.setLong(14, record.getSubmissionTime().toInstant().getEpochSecond());
                    if (record.getRatingForUniqueness().isPresent()) {
                        ps.setInt(15, record.getRatingForUniqueness().get());
                    } else {
                        ps.setNull(15, Types.INTEGER);
                    }
                    ps.setString(16, record.getWhatIsIt().orElse(null));
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} OPR submissions.", submissions.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprAssignments(final ReportDetails<List<OprAssignmentLogItem>> oprAssignments) {
        if (oprAssignments == null || !oprAssignments.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<OprAssignmentLogItem> assignments = oprAssignments.getData();
        if (assignments.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_opr_assignments(candidate_id,time) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<OprAssignmentLogItem>> batches = Lists.partition(new ArrayList<>(assignments), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final OprAssignmentLogItem record = batch.get(i);
                    ps.setString(1, record.getCandidateId());
                    ps.setLong(2, record.getTime().toInstant().getEpochSecond());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} OPR assignments.", assignments.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprAgreements(final ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements) {
        if (oprAgreements == null || !oprAgreements.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Integer>> agreements = oprAgreements.getData();
        if (agreements.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_opr_agreements(time,portal_id) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Integer>>> batches = Lists.partition(new ArrayList<>(agreements), DEFAULT_BATCH_SIZE);
            bulkSaveTimestampedInteger(sql, batches);
            LOGGER.info("Saved {} OPR agreements.", agreements.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprProfile(final ReportDetails<OprProfile> oprProfile) {
        if (oprProfile == null || !oprProfile.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final OprProfile profile = oprProfile.getData();
        return CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO gdpr_raw_opr_profile("
                    + "email,bonus_last_change_time,bonus_loc_latE6,bonus_loc_lngE6,account_creation_time,"
                    + "total_analyzed,portal_created,portal_rejected,hometown_changed_times,hometown_last_changed_time,hometown_loc_latE6,hometown_loc_lngE6,"
                    + "last_activity_loc_latE6,last_activity_loc_lngE6,language,last_login_time,performance,"
                    + "quiz_status,quiz_time_taken,training_completion_time)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql,
                    profile.getEmailAddress(),
                    profile.getBonusLastChangedTime().toInstant().getEpochSecond(), profile.getBonusLocation().getLatE6(), profile.getBonusLocation().getLngE6(),
                    profile.getAccountCreationTime().toInstant().getEpochSecond(), profile.getTotalAnalyzed(), profile.getPortalCreated(), profile.getPortalRejected(),
                    profile.getHometownChangedTimes(),
                    profile.getHometownLastChangedTime().isPresent() ? profile.getHometownLastChangedTime().get().toInstant().getEpochSecond() : null,
                    profile.getHometownLocation().getLatE6(), profile.getHometownLocation().getLngE6(),
                    profile.getLastActivityLocation().getLatE6(), profile.getLastActivityLocation().getLngE6(),
                    profile.getLanguage(), profile.getLastLoginTime().toInstant().getEpochSecond(), profile.getPerformance(),
                    profile.getQuizStatus(), profile.getQuizTimeTaken().toInstant().getEpochSecond(), profile.getTrainingCompletionTime().toInstant().getEpochSecond()
            );
            LOGGER.info("Saved OPR profile.");
        }, executor);
    }

    private CompletableFuture<Void> persistUsedDevices(final ReportDetails<List<UsedDevice>> usedDevices) {
        if (usedDevices == null || !usedDevices.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<UsedDevice> devices = usedDevices.getData();
        if (devices.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            jdbcTemplate.batchUpdate("INSERT INTO gdpr_raw_used_devices(name) VALUES(?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    UsedDevice device = devices.get(i);
                    ps.setString(1, device.getDeviceName());
                }

                @Override public int getBatchSize() {
                    return devices.size();
                }
            });
            LOGGER.info("Saved {} used devices.", devices.size());
        }, executor);
    }

    private CompletableFuture<Void> persistCommMentions(final ReportDetails<List<CommMention>> commMentions) {
        if (commMentions == null || !commMentions.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<CommMention> mentions = commMentions.getData();
        if (mentions.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO gdpr_raw_comm_mentions(time,message) VALUES(?,?)";
            final List<List<CommMention>> batches = Lists.partition(new ArrayList<>(mentions), DEFAULT_BATCH_SIZE);
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

    private CompletableFuture<Void> persistGameLogs(final ReportDetails<List<GameLog>> gameLogs) {
        if (gameLogs == null || !gameLogs.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<GameLog> logs = gameLogs.getData();
        if (logs.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO gdpr_raw_game_logs(time,loc_latE6,loc_lngE6,tracker_trigger,comment) VALUES(?,?,?,?,?)";
            final List<List<GameLog>> batches = Lists.partition(new ArrayList<>(logs), DEFAULT_BATCH_SIZE);
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
            LOGGER.info("Saved {} game logs into database.", logs.size());
        }, executor);
    }

    private void bulkSaveTimestampedInteger(final String sql, final List<List<TimestampedRecord<Integer>>> batches) {
        batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final TimestampedRecord<Integer> record = batch.get(i);
                ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                ps.setInt(2, record.getValue());
            }

            @Override public int getBatchSize() {
                return batch.size();
            }
        }));
    }

    private void bulkSaveTimestampedLong(final String sql, final List<List<TimestampedRecord<Long>>> batches) {
        batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final TimestampedRecord<Long> record = batch.get(i);
                ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                ps.setLong(2, record.getValue());
            }

            @Override public int getBatchSize() {
                return batch.size();
            }
        }));
    }

    private void bulkSaveTimestampedFloat(final String sql, final List<List<TimestampedRecord<Float>>> batches) {
        batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final TimestampedRecord<Float> record = batch.get(i);
                ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                ps.setFloat(2, record.getValue());
            }

            @Override public int getBatchSize() {
                return batch.size();
            }
        }));
    }

    private void bulkSaveTimestampedDouble(final String sql, final List<List<TimestampedRecord<Double>>> batches) {
        batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final TimestampedRecord<Double> record = batch.get(i);
                ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                ps.setDouble(2, record.getValue());
            }

            @Override public int getBatchSize() {
                return batch.size();
            }
        }));
    }

    private void rebuildRawDateTables() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("db/rebuild_raw_tables.sql"));
        databasePopulator.execute(primaryDataSource);
    }

}
