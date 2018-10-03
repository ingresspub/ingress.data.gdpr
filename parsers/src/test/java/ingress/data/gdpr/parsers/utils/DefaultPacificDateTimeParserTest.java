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

import ingress.data.gdpr.models.records.GameLog;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * @author SgrAlpha
 */
public class DefaultPacificDateTimeParserTest {

    private static final DefaultPacificDateTimeParser PARSER = new DefaultPacificDateTimeParser(GameLog.TIME_PATTERN);
    
    @Test
    public void testParseFromIso8610() {
        ZonedDateTime pst = PARSER.parse("2014 11-02 04:34:16");
        assertEquals(2014, pst.getYear());
        assertEquals(ZoneId.of("America/Los_Angeles"), pst.getZone());
        assertEquals(ZoneOffset.ofHours(-8), pst.getOffset());

        ZonedDateTime pdt = PARSER.parse("2014 10-17 05:38:27");
        assertEquals(2014, pdt.getYear());
        assertEquals(ZoneId.of("America/Los_Angeles"), pdt.getZone());
        assertEquals(ZoneOffset.ofHours(-7), pdt.getOffset());
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseFromInvalid() {
        PARSER.parse("asdfg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromBlank() {
        PARSER.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromNull() {
        PARSER.parse();
    }

}
