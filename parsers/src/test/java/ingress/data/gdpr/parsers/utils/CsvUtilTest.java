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

package ingress.data.gdpr.parsers.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author SgrAlpha
 */
public class CsvUtilTest {

    @Test
    public void testSplit() {
        final String line = "\"a23483f6589a37b76dfa1eff56cc3482\",\"2017-12-12 07:59:11 GMT\",\"1956年开的，的确有历史了，符合 \"\"当地隐藏的好地方\"\"\n" +
                "http://gd.sina.com.cn/dg/social/2015-01-14/095916702.html\",\"5\",\"5\",\"false\",\"\",\"5\",\"23.047531152445462, 113.74173490311045\",\"5\",\"5\",\"false\",\"2017-12-12 08:03:15 GMT\",\"5\",\"\"";
        String[] columns = CsvUtil.split(line);
        assertEquals(15, columns.length);
    }
}
