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

package ingress.data.gdpr.web.dao;

import static ingress.data.gdpr.models.utils.Preconditions.notNull;

import ingress.data.gdpr.web.models.UserPreferences;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SgrAlpha
 */
@Component
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(@Qualifier("primaryJdbcTemplate") final JdbcTemplate jdbcTemplate) {
        notNull(jdbcTemplate, "Missing JDBC template");
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserPreferences getUserPreferences() {
        final String sql = "SELECT * FROM gdpr_user_preferences";
        try {
            final Map<String, String> map = jdbcTemplate.query(sql, rs -> {
                Map<String, String> preferences = new HashMap<>();
                while (rs.next()) {
                    preferences.put(rs.getString("key"), rs.getString("value"));
                }
                return preferences;
            });
            return UserPreferences.fromMap(map);
        } catch (DataAccessException e) {
            return UserPreferences.getDefault();
        }
    }

    public void saveUserService(final UserPreferences userPreferences) {
        notNull(userPreferences, "Missing user preferences to save!");
        final String sql = "INSERT INTO gdpr_user_preferences(`KEY`, `VALUE`) VALUES (?,?) ON DUPLICATE KEY UPDATE `VALUE` = VALUES(`VALUE`)";
        final List<Map.Entry<String, String>> entries = new ArrayList<>(userPreferences.toMap().entrySet());
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                final Map.Entry<String, String> entry = entries.get(i);
                ps.setString(1, entry.getKey());
                ps.setString(2, entry.getValue());
            }

            @Override public int getBatchSize() {
                return entries.size();
            }
        });
    }

}
