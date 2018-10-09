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
import io.sgr.geometry.Coordinate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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

    private int countGameLogs() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM gdpr_raw_game_logs", Integer.class);
        return Optional.ofNullable(count).orElse(0);
    }

}
