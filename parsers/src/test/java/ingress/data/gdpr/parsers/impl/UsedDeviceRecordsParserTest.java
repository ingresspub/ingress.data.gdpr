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

package ingress.data.gdpr.parsers.impl;

import static ingress.data.gdpr.parsers.utils.ErrorConstants.NOT_REGULAR_FILE;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.reports.ReportDetails;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class UsedDeviceRecordsParserTest {

    private static final DeviceRecordsParser PARSER = DeviceRecordsParser.getDefault();

    @Test
    public void testParseFromValidFile() throws URISyntaxException {
        final Path temp = Paths.get(UsedDeviceRecordsParserTest.class.getResource("test_devices.txt").toURI());
        ReportDetails<List<UsedDevice>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertTrue(result.isOk());
        final List<UsedDevice> data = result.getData();
        assertNotNull(data);
        assertEquals(4, data.size());
        assertEquals("Apple iPhone X", data.get(0).getDeviceName());
        assertEquals("Apple iPhone 8", data.get(1).getDeviceName());
        assertEquals("Apple iPhone 7", data.get(2).getDeviceName());
        assertEquals("Apple iPhone 6", data.get(3).getDeviceName());
    }

    @Test
    public void testParseFromBlankFile() throws IOException {
        final Path temp = Files.createTempFile("test-file-", null);
        ReportDetails<List<UsedDevice>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertEquals(NO_DATA, result.getError());
        assertNull(result.getData());
        Files.delete(temp);
    }

    @Test
    public void testParseFromFolder() throws IOException {
        final Path temp = Files.createTempDirectory("test-dir-");
        ReportDetails<List<UsedDevice>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertEquals(NOT_REGULAR_FILE, result.getError());
        assertNull(result.getData());
        Files.delete(temp);
    }

    @Test(expected = NullPointerException.class)
    public void testParseFromNull() {
        PARSER.parse(null);
    }
}
