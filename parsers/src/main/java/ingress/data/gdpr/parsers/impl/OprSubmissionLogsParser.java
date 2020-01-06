/*
 * Copyright (C) 2014-2020 SgrAlpha
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static ingress.data.gdpr.parsers.utils.CsvUtil.escapeQuote;
import static ingress.data.gdpr.parsers.utils.CsvUtil.split;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;

import ingress.data.gdpr.models.records.opr.OprSubmissionLogItem;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.PlainTextDataFileParser;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;
import ingress.data.gdpr.parsers.utils.ZonedDateTimeParser;

import io.sgr.geometry.Coordinate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class OprSubmissionLogsParser extends PlainTextDataFileParser<List<OprSubmissionLogItem>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OprSubmissionLogsParser.class);

    private static final OprSubmissionLogsParser INSTANCE = new OprSubmissionLogsParser();
    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private OprSubmissionLogsParser() {
    }

    public static OprSubmissionLogsParser getDefault() {
        return INSTANCE;
    }

    @Override protected ReportDetails<List<OprSubmissionLogItem>> readLines(final List<String> lines, final Path dataFile) {
        checkNotNull(lines, "No line to read from");
        if (lines.size() < 2) {
            return ReportDetails.error(NO_DATA);
        }
        checkNotNull(dataFile, "Data file needs to be specified");
        try {
            List<OprSubmissionLogItem> data = new LinkedList<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (isNullOrEmpty(line)) {
                    continue;
                }
                MergeResult result = joinNextLineBeforeSplitIfNecessary(i, line, lines);
                String[] columns = result.getColumns();
                i = result.getIndex();
                data.add(parse(columns));
            }
            if (data.size() > 1) {
                LOGGER.info("Parsed {} records from {}", data.size(), dataFile.getFileName());
            } else {
                LOGGER.info("Parsed {} record from {}", data.size(), dataFile.getFileName());
            }
            return ReportDetails.ok(data);
        } catch (MalformattedRecordException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

    private MergeResult joinNextLineBeforeSplitIfNecessary(int i, String line, final List<String> lines) throws MalformattedRecordException {
        String[] columns = split(line);
        if (columns.length < 15) {
            i = i + 1;
            line = line + "\n" + lines.get(i);
            LOGGER.warn("Line {} and {} now merged to {}", i - 1, i, line);
            return joinNextLineBeforeSplitIfNecessary(i, line, lines);
        } else if (columns.length > 15) {
            throw new MalformattedRecordException(String.format("Expecting record with %d columns at line %d but got %d: %s", 15, i, columns.length, line));
        }
        return new MergeResult(columns, i);
    }

    private static class MergeResult {
        private final String[] columns;
        private final int index;

        private MergeResult(final String[] columns, final int index) {
            this.columns = columns;
            this.index = index;
        }

        String[] getColumns() {
            return columns;
        }

        int getIndex() {
            return index;
        }
    }

    @Override protected Logger getLogger() {
        return LOGGER;
    }

    private OprSubmissionLogItem parse(final String... columns) throws MalformattedRecordException {
        checkNotNull(columns, "Missing columns to parse from");
        try {
            return new OprSubmissionLogItem(
                    escapeQuote(columns[0]),
                    TIME_PARSER.parse(escapeQuote(columns[1])),
                    escapeQuote(columns[2]),
                    parseRating(columns[3]),
                    parseRating(columns[4]),
                    Boolean.parseBoolean(escapeQuote(columns[5])),
                    escapeQuote(columns[6]),
                    parseRating(columns[7]),
                    parseSuggestedLocation(columns[8]),
                    parseRating(columns[9]),
                    parseRating(columns[10]),
                    Boolean.parseBoolean(escapeQuote(columns[11])),
                    TIME_PARSER.parse(escapeQuote(columns[12])),
                    parseRating(columns[13]),
                    escapeQuote(columns[14])
            );
        } catch (Exception e) {
            throw new MalformattedRecordException(String.format("Unable to parse OPR assignment log item from %s", Arrays.asList(columns)), e);
        }
    }

    private Coordinate parseSuggestedLocation(final String column) {
        final String tmp = escapeQuote(column);
        if (isNullOrEmpty(tmp)) {
            return null;
        }
        return Coordinate.parseCommaSeparatedString(tmp);
    }

    private static Integer parseRating(final String column) {
        final String tmp = escapeQuote(column);
        if (isNullOrEmpty(tmp)) {
            return null;
        }
        return Integer.parseInt(tmp);
    }

}
