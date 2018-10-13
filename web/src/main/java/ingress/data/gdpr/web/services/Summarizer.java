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

import ingress.data.gdpr.models.analyzed.Circle;
import ingress.data.gdpr.models.analyzed.CommMessageInTimeline;
import ingress.data.gdpr.models.analyzed.CommMessageType;
import ingress.data.gdpr.models.analyzed.Feed;
import ingress.data.gdpr.models.analyzed.InAppMedal;
import ingress.data.gdpr.models.analyzed.TimelineItem;
import ingress.data.gdpr.models.records.profile.BadgeLevel;
import ingress.data.gdpr.parsers.utils.TimeZoneUtil;
import io.sgr.geometry.Coordinate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
@Service
public class Summarizer {

    private final JdbcTemplate jdbcTemplate;

    public Summarizer(@Qualifier("primaryJdbcTemplate") final JdbcTemplate jdbcTemplate) {
        notNull(jdbcTemplate, "Missing JDBC template");
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean noGameLogData() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM gdpr_raw_game_logs", Integer.class);
        return Optional.ofNullable(count).orElse(0) <= 0;
    }

    public List<Circle> listCapturedPortals(final String color, final int radius) {
        final String sql = "SELECT DISTINCT loc_latE6, loc_lngE6 FROM gdpr_raw_game_logs WHERE tracker_trigger = 'captured portal'";
        return listPortals(sql, color, radius);
    }

    public List<Circle> listVisitedPortals(final String color, final int radius) {
        final String sql = "SELECT DISTINCT loc_latE6, loc_lngE6 FROM gdpr_raw_game_logs"
                + " WHERE tracker_trigger"
                + " IN ('captured portal','hacked enemy portal','hacked friendly portal','hacked neutral portal','mod deployed','resonator deployed','resonator upgraded')"
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

    public List<TimelineItem<?>> listBadgeTimeline(final Locale userLocale, final ZoneId userZoneId) {
        return jdbcTemplate.query("SELECT * FROM gdpr_raw_agent_profile_badges ORDER BY time DESC", (rs, rowNum) -> {
            final BadgeLevel level = BadgeLevel.valueOf(rs.getString("level"));
            final String name = rs.getString("name");
            final String dateTimeStr = TimeZoneUtil.epochSecondToZonedDateTime(rs.getLong("time"), userLocale, userZoneId, FormatStyle.FULL);
            final String url = null;
            return new TimelineItem<>("badge", String.format("%s %s", level, name), dateTimeStr, new InAppMedal(level, name, url));
        });
    }

    public boolean noBadgesData() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM gdpr_raw_agent_profile_badges", Integer.class);
        return Optional.ofNullable(count).orElse(0) <= 0;
    }

    public boolean noCommMentions() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM gdpr_raw_comm_mentions", Integer.class);
        return Optional.ofNullable(count).orElse(0) <= 0;
    }

    public Feed<CommMessageInTimeline> listCommMessages(final Integer curPage, final Integer pageSize, final Locale userLocale, final ZoneId userZoneId) {
        final String sql = "SELECT time AS time, null AS loc_latE6, null AS loc_lngE6, secured AS secured, from_agent AS from_agent, message AS message, 'RECEIVED' AS type FROM gdpr_raw_comm_mentions"
                + " UNION ALL"
                + " (SELECT time AS time, loc_latE6 AS loc_latE6, loc_lngE6 AS loc_lngE6, 'false' AS secured, null AS from_agent, comment AS message, 'SENT' AS type FROM gdpr_raw_game_logs WHERE tracker_trigger = 'send comm message')"
                + " ORDER BY time DESC LIMIT ?, ?";
        int page = Optional.ofNullable(curPage).orElse(1);
        int size = Optional.ofNullable(pageSize).orElse(Integer.MAX_VALUE);
        int offset = (page - 1) * size;
        List<CommMessageInTimeline> messages = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Coordinate loc = null;
                    if (rs.getObject("loc_latE6") != null
                            && rs.getObject("loc_lngE6") != null) {
                        loc = new Coordinate(rs.getInt("loc_latE6") / 1e6, rs.getInt("loc_lngE6") / 1e6);
                    }
                    final String timeStr = TimeZoneUtil.epochSecondToZonedDateTime(rs.getLong("time"), userLocale, userZoneId, FormatStyle.FULL);
                    return new CommMessageInTimeline(timeStr, loc, rs.getBoolean("secured"), rs.getString("from_agent"), rs.getString("message"), CommMessageType.valueOf(rs.getString("type")));
                },
                offset, size
        );
        return new Feed<>(messages, page, size);
    }

}
