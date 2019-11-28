/*
 * Copyright (C) 2014-2019 SgrAlpha
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

package ingress.data.gdpr.web.dao;

import static com.google.common.base.Preconditions.checkNotNull;

import ingress.data.gdpr.models.analyzed.CommMessageInTimeline;
import ingress.data.gdpr.models.analyzed.CommMessageType;
import ingress.data.gdpr.models.analyzed.Event;
import ingress.data.gdpr.models.analyzed.Feed;
import ingress.data.gdpr.models.analyzed.LevelUpEvent;
import ingress.data.gdpr.models.analyzed.NewBadgeEvent;
import ingress.data.gdpr.models.records.profile.BadgeLevel;
import ingress.data.gdpr.parsers.utils.TimeZoneUtil;

import io.sgr.geometry.Coordinate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * @author SgrAlpha
 */
@Component
public class RawDataDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RawDataDao(@Qualifier("primaryJdbcTemplate") @Nonnull final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = checkNotNull(jdbcTemplate, "Missing JDBC template");
    }

    public boolean noGameLogData() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM gdpr_raw_game_logs", Integer.class);
        return Optional.ofNullable(count).orElse(0) <= 0;
    }

    public List<Coordinate> listUpc() {
        final String sql = "SELECT DISTINCT loc_latE6, loc_lngE6 FROM gdpr_raw_game_logs WHERE tracker_trigger = 'captured portal'";
        return listPortals(sql);
    }

    public List<Coordinate> listUpv(final boolean excludeCaptured) {
        String sql = "SELECT DISTINCT loc_latE6, loc_lngE6 FROM gdpr_raw_game_logs WHERE tracker_trigger";
        if (excludeCaptured) {
            sql = sql + " IN ('hacked enemy portal','hacked friendly portal','hacked neutral portal','mod deployed','resonator deployed','resonator upgraded')";
        } else {
            sql = sql + " IN ('captured portal','hacked enemy portal','hacked friendly portal','hacked neutral portal','mod deployed','resonator deployed','resonator upgraded')";
        }
        sql = sql + " AND comment != 'fail'";
        return listPortals(sql);
    }

    private List<Coordinate> listPortals(final String sql) {
        return jdbcTemplate.query(sql, rs -> {
            List<Coordinate> locations = new LinkedList<>();
            while (rs.next()) {
                locations.add(new Coordinate(rs.getInt("loc_latE6") / 1e6, rs.getInt("loc_lngE6") / 1e6));
            }
            return locations;
        });
    }

    public List<Event> listProfileEvents(final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        List<Event> events = listNewBadgeEvent(userLocale, userZoneId, formatStyle);
        events.addAll(listLevelUpEvent(userLocale, userZoneId, formatStyle));
        return events.stream()
                .sorted((event1, event2) -> Long.compare(event2.getTimeInMs(), event1.getTimeInMs()))
                .collect(Collectors.toList());
    }

    public List<Event> listNewBadgeEvent(final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        return jdbcTemplate.query("SELECT * FROM gdpr_raw_agent_profile_badges ORDER BY time DESC", (rs, rowNum) -> {
            final BadgeLevel level = BadgeLevel.valueOf(rs.getString("level"));
            final String name = rs.getString("name");
            final long timeInMs = rs.getLong("time");
            final String localDate = TimeZoneUtil.epochSecondToLocalDate(timeInMs, userLocale, userZoneId, formatStyle);
            final String localTime = TimeZoneUtil.epochSecondToLocalTime(timeInMs, userLocale, userZoneId, formatStyle);
            final String url = null;
            return new NewBadgeEvent(level, name, url, timeInMs, localDate, localTime);
        });
    }

    public List<Event> listLevelUpEvent(final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        return jdbcTemplate.query("SELECT time,loc_latE6,loc_lngE6,comment FROM gdpr_raw_game_logs where tracker_trigger = 'level up' ORDER BY time DESC", (rs, rowNum) -> {
            final long timeInMs = rs.getLong("time");
            final String localDate = TimeZoneUtil.epochSecondToLocalDate(timeInMs, userLocale, userZoneId, formatStyle);
            final String localTime = TimeZoneUtil.epochSecondToLocalTime(timeInMs, userLocale, userZoneId, formatStyle);
            Coordinate loc = null;
            if (rs.getObject("loc_latE6") != null
                    && rs.getObject("loc_lngE6") != null) {
                loc = new Coordinate(rs.getInt("loc_latE6") / 1e6, rs.getInt("loc_lngE6") / 1e6);
            }
            String comment = rs.getString("comment");
            comment = comment.substring("nextLevelToken: ".length());
            final int level = Integer.parseInt(comment);
            return new LevelUpEvent(level, loc, timeInMs, localDate, localTime);
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

    public Feed<CommMessageInTimeline> listCommMessages(final Integer curPage, final Integer pageSize, final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
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
                    final String timeStr = TimeZoneUtil.epochSecondToZonedDateTime(rs.getLong("time"), userLocale, userZoneId, formatStyle);
                    return new CommMessageInTimeline(timeStr, loc, rs.getBoolean("secured"), rs.getString("from_agent"), rs.getString("message"), CommMessageType.valueOf(rs.getString("type")));
                },
                offset, size
        );
        return new Feed<>(messages, page, size);
    }

}
