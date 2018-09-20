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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author SgrAlpha
 */
public class TimeUtil {

    public static final String GDPR_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss zz";

    public static ZonedDateTime toZonedDataTime(final String timeStr) throws DateTimeParseException {
        notEmptyString(timeStr, "Missing time info to parse from");
        return ZonedDateTime.parse(timeStr, DateTimeFormatter.ofPattern(GDPR_TIME_PATTERN));
    }

}
