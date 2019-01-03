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

package ingress.data.gdpr.models.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author SgrAlpha
 */
public class JsonUtilTest {

    @Test
    public void testUnableToJson() {
        final BadData badData = new BadData();
        assertEquals("aaa", badData.getData());
        assertEquals("{}", JsonUtil.toJson(badData));
    }

    @Test
    public void testToJson() {
        JsonUtil.toJson(null);
        JsonUtil.toJson("");
    }

    @Test
    public void testGetter() {
        assertNotNull(JsonUtil.getObjectMapper());
    }

    private static class BadData {

        private static final String data = "aaa";

        private String getData() {
            return data;
        }

    }

}
