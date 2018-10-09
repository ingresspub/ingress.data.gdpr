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

package ingress.data.gdpr.parsers.utils;

import static ingress.data.gdpr.models.utils.Preconditions.notEmptyString;
import static ingress.data.gdpr.models.utils.Preconditions.notNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author SgrAlpha
 */
public class DefaultPacificDateTimeParser implements SingleLineValueParser<ZonedDateTime> {

    private final String pattern;

    public DefaultPacificDateTimeParser(final String pattern) {
        this.pattern = pattern;
    }

    @Override public ZonedDateTime parse(final String... columns) {
        notNull(columns, "No columns to parse from");
        if (columns.length != 1) {
            throw new IllegalArgumentException(String.format("Expecting only one column, but got %d", columns.length));
        }
        final String time = columns[0];
        notEmptyString(time, "Missing time info to parse from");
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
        return ZonedDateTime.of(localDateTime, ZoneId.of("America/Los_Angeles"));
    }

}
