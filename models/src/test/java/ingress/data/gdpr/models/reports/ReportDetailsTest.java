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

package ingress.data.gdpr.models.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author SgrAlpha
 */
public class ReportDetailsTest {

    @Test
    public void testCreateGoodResult() {
        ReportDetails<?> result = ReportDetails.ok(null);
        assertNotNull(result);
        assertTrue(result.isOk());
        assertNull(result.getData());
        assertNull(result.getError());

        final String data = "Some kind of data";
        result = ReportDetails.ok(data);
        assertNotNull(result);
        assertTrue(result.isOk());
        assertEquals(data, result.getData());
        assertNull(result.getError());
    }

    @Test
    public void testCreateErrorResult() {
        ReportDetails<?> result = ReportDetails.error(null);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertNull(result.getData());
        assertNull(result.getError());

        final String errMsg = "Some kind of error";
        result = ReportDetails.error(errMsg);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertNull(result.getData());
        assertEquals(errMsg, result.getError());
    }
}
