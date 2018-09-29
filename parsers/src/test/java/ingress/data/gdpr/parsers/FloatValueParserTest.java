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

package ingress.data.gdpr.parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author SgrAlpha
 */
public class FloatValueParserTest {

    private static final FloatValueParser PARSER = FloatValueParser.getDefault();
    
    @Test
    public void testParseFromValid() {
        assertEquals(0.00636792043224f, PARSER.parse("0.00636792043224"), 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseFromInvalid() {
        PARSER.parse("asdfg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromBlank() {
        PARSER.parse("");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testParseFromNull() {
        PARSER.parse(null);
    }

}
