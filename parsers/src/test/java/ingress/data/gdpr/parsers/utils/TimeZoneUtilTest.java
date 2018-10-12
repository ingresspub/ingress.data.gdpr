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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.time.ZoneId;
import java.util.Locale;

/**
 * @author SgrAlpha
 */
public class TimeZoneUtilTest {

    @Test
    public void testParseLocalDate() {
        long timestamp = 1539369230;    // Fri, 12 Oct 2018 18:33:50 GMT
        final String dateInChina = TimeZoneUtil.epochSecondToLocalDate(timestamp, Locale.CHINESE, ZoneId.of("GMT+8"));
        assertEquals("2018-10-13", dateInChina);
        final String timeInChina = TimeZoneUtil.epochSecondToLocalTime(timestamp, Locale.CHINESE, ZoneId.of("GMT+8"));
        assertEquals("2:33:50", timeInChina);
        final String dateTimeInChina = TimeZoneUtil.epochSecondToLocalDateTime(timestamp, Locale.CHINESE, ZoneId.of("GMT+8"));
        assertEquals("2018-10-13 2:33:50", dateTimeInChina);
        final String dateInLA = TimeZoneUtil.epochSecondToLocalDate(timestamp, Locale.US, ZoneId.of("America/Los_Angeles"));
        assertEquals("Oct 12, 2018", dateInLA);
        final String timeInLA = TimeZoneUtil.epochSecondToLocalTime(timestamp, Locale.US, ZoneId.of("America/Los_Angeles"));
        assertEquals("11:33:50 AM", timeInLA);
        final String dateTimeInLA = TimeZoneUtil.epochSecondToLocalDateTime(timestamp, Locale.US, ZoneId.of("America/Los_Angeles"));
        assertEquals("Oct 12, 2018 11:33:50 AM", dateTimeInLA);
    }

    @Test
    public void testParseFromInvalidEpochSecond() {
        try {
            TimeZoneUtil.epochSecondToLocalDate(-1, Locale.getDefault(), ZoneId.systemDefault());
            fail();
        } catch (IllegalArgumentException e) {
            // Ignore
        }
        try {
            TimeZoneUtil.epochSecondToLocalTime(-1, Locale.getDefault(), ZoneId.systemDefault());
            fail();
        } catch (IllegalArgumentException e) {
            // Ignore
        }
        try {
            TimeZoneUtil.epochSecondToLocalDateTime(-1, Locale.getDefault(), ZoneId.systemDefault());
            fail();
        } catch (IllegalArgumentException e) {
            // Ignore
        }
    }
}
