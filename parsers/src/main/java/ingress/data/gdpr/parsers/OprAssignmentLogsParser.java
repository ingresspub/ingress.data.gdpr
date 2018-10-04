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

import static ingress.data.gdpr.models.utils.Preconditions.notNull;
import static ingress.data.gdpr.parsers.utils.CsvUtil.escapeQuote;
import static ingress.data.gdpr.parsers.utils.CsvUtil.split;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NOT_REGULAR_FILE;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.UNREADABLE_FILE;

import ingress.data.gdpr.models.records.opr.OprAssignmentLogItem;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;
import ingress.data.gdpr.parsers.utils.ZonedDateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class OprAssignmentLogsParser implements DataFileParser<List<OprAssignmentLogItem>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OprAssignmentLogsParser.class);

    private static final OprAssignmentLogsParser INSTANCE = new OprAssignmentLogsParser();
    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private OprAssignmentLogsParser() {
    }

    public static OprAssignmentLogsParser getDefault() {
        return INSTANCE;
    }

    @Override public ReportDetails<List<OprAssignmentLogItem>> parse(final Path dataFile) {
        notNull(dataFile, "Data file needs to be specified");
        if (!Files.isRegularFile(dataFile)) {
            LOGGER.warn("{} is not a regular file", dataFile.getFileName());
            return ReportDetails.error(NOT_REGULAR_FILE);
        }
        if (!Files.isReadable(dataFile)) {
            LOGGER.warn("{} is not a readable file", dataFile.getFileName());
            return ReportDetails.error(UNREADABLE_FILE);
        }

        final List<String> lines;
        try {
            lines = Files.readAllLines(dataFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
        if (lines.size() < 2) {
            return ReportDetails.error(NO_DATA);
        }

        ReportDetails<List<OprAssignmentLogItem>> details;
        try {
            List<OprAssignmentLogItem> data = new LinkedList<>();
            for (int i = 1; i < lines.size(); i++) {
                final String[] columns = split(lines.get(i));
                if (columns.length != 2) {
                    throw new MalformattedRecordException(String.format("Expecting record with %d columns at line %d but got %d", 2, i, columns.length));
                }
                data.add(parse(columns));
            }
            if (data.size() > 1) {
                LOGGER.info("Parsed {} records from {}", data.size(), dataFile.getFileName());
            } else {
                LOGGER.info("Parsed {} record from {}", data.size(), dataFile.getFileName());
            }
            return ReportDetails.ok(data);
        } catch (MalformattedRecordException e) {
            details = ReportDetails.error(e.getMessage());
        }

        return details;
    }

    private OprAssignmentLogItem parse(final String... columns) throws MalformattedRecordException {
        notNull(columns, "Missing columns to parse from");
        try {
            return new OprAssignmentLogItem(escapeQuote(columns[0]), TIME_PARSER.parse(escapeQuote(columns[1])));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MalformattedRecordException(String.format("Unable to parse OPR assignment log item from %s", Arrays.asList(columns)), e);
        }
    }

}
