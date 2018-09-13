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

package ingress.data.gdpr.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.IntValueParser;
import ingress.data.gdpr.parsers.NumericBasedRecordParser;
import ingress.data.gdpr.parsers.ZonedDateTimeParser;
import ingress.data.gdpr.parsers.utils.ErrorConstants;
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
public class NumericBasedRecordParserTest {

    private static final NumericBasedRecordParser<Integer> PARSER = new NumericBasedRecordParser<>(
            ZonedDateTimeParser.getDefault(), IntValueParser.getDefault());

    @Test
    public void testParseFromWrongTimeFormat() throws URISyntaxException {
        final Path temp = Paths.get(NumericBasedRecordParserTest.class.getResource("test_count_based_wrong_time_format.tsv").toURI());
        ReportDetails<List<NumericBasedRecord<Integer>>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertNull(result.getData());
    }

    @Test
    public void testParseFromWrongColumns() throws URISyntaxException {
        final Path temp = Paths.get(NumericBasedRecordParserTest.class.getResource("test_count_based_wrong_columns.tsv").toURI());
        ReportDetails<List<NumericBasedRecord<Integer>>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertNull(result.getData());
    }

    @Test
    public void testParseFromBlankFile() throws IOException {
        final Path temp = Files.createTempFile("test-file-", null);
        ReportDetails<List<NumericBasedRecord<Integer>>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertTrue(result.isOk());
        final List<NumericBasedRecord<Integer>> data = result.getData();
        assertNotNull(data);
        assertEquals(0, data.size());
        Files.delete(temp);
    }

    @Test
    public void testParseFromFolder() throws IOException {
        final Path temp = Files.createTempDirectory("test-dir-");
        ReportDetails<List<NumericBasedRecord<Integer>>> result = PARSER.parse(temp);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertEquals(ErrorConstants.NOT_REGULAR_FILE, result.getError());
        Files.delete(temp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromNull() {
        PARSER.parse(null);
    }
}
