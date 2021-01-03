/*
 * Copyright (C) 2014-2021 SgrAlpha
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
 *
 */

package ingress.data.gdpr.parsers.utils;

import static ingress.data.gdpr.parsers.utils.TimeZoneConstants.DEFAULT_LOCALE;
import static ingress.data.gdpr.parsers.utils.TimeZoneConstants.DEFAULT_ZONE_ID;
import static ingress.data.gdpr.parsers.utils.TimeZoneConstants.UTC_ZONE_ID;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class TimeZoneUtil {

    public static String epochSecondToZonedDateTime(final long epochSecond, final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        if (epochSecond < 0) {
            throw new IllegalArgumentException(String.format("Invalid epoch second: %d", epochSecond));
        }
        final Locale targetLocale = Optional.ofNullable(userLocale).orElse(DEFAULT_LOCALE);
        final ZoneId targetZoneId = Optional.ofNullable(userZoneId).orElse(DEFAULT_ZONE_ID);
        final FormatStyle targetFormatStyle = Optional.ofNullable(formatStyle).orElse(FormatStyle.FULL);
        final ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), UTC_ZONE_ID);
        final ZonedDateTime userDateTime = utcDateTime.withZoneSameInstant(targetZoneId);
        return userDateTime.format(DateTimeFormatter.ofLocalizedDateTime(targetFormatStyle).withLocale(targetLocale));
    }

    public static String epochSecondToLocalDateTime(final long epochSecond, final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        if (epochSecond < 0) {
            throw new IllegalArgumentException(String.format("Invalid epoch second: %d", epochSecond));
        }
        final Locale targetLocale = Optional.ofNullable(userLocale).orElse(DEFAULT_LOCALE);
        final ZoneId targetZoneId = Optional.ofNullable(userZoneId).orElse(DEFAULT_ZONE_ID);
        final FormatStyle targetFormatStyle = Optional.ofNullable(formatStyle).orElse(FormatStyle.MEDIUM);
        final ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), UTC_ZONE_ID);
        final ZonedDateTime userDateTime = utcDateTime.withZoneSameInstant(targetZoneId);
        return userDateTime.toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(targetFormatStyle).withLocale(targetLocale));
    }

    public static String epochSecondToLocalDate(final long epochSecond, final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        if (epochSecond < 0) {
            throw new IllegalArgumentException(String.format("Invalid epoch second: %d", epochSecond));
        }
        final Locale targetLocale = Optional.ofNullable(userLocale).orElse(DEFAULT_LOCALE);
        final ZoneId targetZoneId = Optional.ofNullable(userZoneId).orElse(DEFAULT_ZONE_ID);
        final FormatStyle targetFormatStyle = Optional.ofNullable(formatStyle).orElse(FormatStyle.MEDIUM);
        final ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), UTC_ZONE_ID);
        final ZonedDateTime userDateTime = utcDateTime.withZoneSameInstant(targetZoneId);
        return userDateTime.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(targetFormatStyle).withLocale(targetLocale));
    }

    public static String epochSecondToLocalTime(final long epochSecond, final Locale userLocale, final ZoneId userZoneId, final FormatStyle formatStyle) {
        if (epochSecond < 0) {
            throw new IllegalArgumentException(String.format("Invalid epoch second: %d", epochSecond));
        }
        final Locale targetLocale = Optional.ofNullable(userLocale).orElse(DEFAULT_LOCALE);
        final ZoneId targetZoneId = Optional.ofNullable(userZoneId).orElse(DEFAULT_ZONE_ID);
        final FormatStyle targetFormatStyle = Optional.ofNullable(formatStyle).orElse(FormatStyle.MEDIUM);
        final ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), UTC_ZONE_ID);
        final ZonedDateTime userDateTime = utcDateTime.withZoneSameInstant(targetZoneId);
        return userDateTime.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(targetFormatStyle).withLocale(targetLocale));
    }

}
