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

import static ingress.data.gdpr.models.utils.Preconditions.isEmptyString;
import static ingress.data.gdpr.models.utils.Preconditions.notNull;

import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.utils.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class NumericBasedRecordParser<T> implements SingleFileParser<List<NumericBasedRecord<T>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumericBasedRecordParser.class);

    private final ZonedDateTimeParser timeParser;
    private final ValueParser<T> valueParser;

    public NumericBasedRecordParser(final ZonedDateTimeParser timeParser, final ValueParser<T> valueParser) {
        this.timeParser = timeParser;
        this.valueParser = valueParser;
    }

    @Override public ReportDetails<List<NumericBasedRecord<T>>> parse(final Path dataFile) {
        notNull(dataFile, "Data file needs to be specified");
        if (!Files.isRegularFile(dataFile)) {
            return ReportDetails.error(ErrorConstants.NOT_REGULAR_FILE);
        }
        if (!Files.isReadable(dataFile)) {
            return ReportDetails.error(ErrorConstants.UNREADABLE_FILE);
        }
        final List<String> lines;
        try {
            lines = Files.readAllLines(dataFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
        try {
            List<NumericBasedRecord<T>> data = new LinkedList<>();
            for (int i = 1; i < lines.size(); i++) {    // Skip first line (header)
                final String line = lines.get(i);
                if (isEmptyString(line)) {
                    continue;
                }
                final String[] columns = line.split("\t");
                if (columns.length != 2) {
                    return ReportDetails.error(String.format("Found mal-formatted data at line %d, expecting %d columns but found %d", i, 2, columns.length));
                }
                final ZonedDateTime time;
                try {
                    time = timeParser.parse(columns[0]);
                } catch (Exception e) {
                    return ReportDetails.error(String.format("Found mal-formatted timestamp at line %d: %s", i, columns[0]));
                }
                final T value;
                try {
                    value = valueParser.parse(columns[1]);
                } catch (Exception e) {
                    return ReportDetails.error(String.format("Found mal-formatted value at line %d, expecting a numeric value but got: %s", i, columns[1]));
                }
                data.add(new NumericBasedRecord<>(time, value));
            }
            return ReportDetails.ok(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

}
