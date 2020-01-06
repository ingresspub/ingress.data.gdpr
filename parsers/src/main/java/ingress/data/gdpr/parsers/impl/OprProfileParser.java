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

import ingress.data.gdpr.models.records.opr.OprProfile;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.PlainTextDataFileParser;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;
import ingress.data.gdpr.parsers.utils.ZonedDateTimeParser;

import io.sgr.geometry.Coordinate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class OprProfileParser extends PlainTextDataFileParser<OprProfile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OprProfileParser.class);

    private static final OprProfileParser INSTANCE = new OprProfileParser();
    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private OprProfileParser() {
    }

    public static OprProfileParser getDefault() {
        return INSTANCE;
    }

    @Override protected ReportDetails<OprProfile> readLines(final List<String> lines, final Path dataFile) {
        checkNotNull(lines, "No line to read from");
        if (lines.size() < 2) {
            return ReportDetails.error(NO_DATA);
        }
        checkNotNull(dataFile, "Data file needs to be specified");

        ReportDetails<OprProfile> details;
        try {
            final String[] columns = split(lines.get(1));
            if (columns.length != 17) {
                throw new MalformattedRecordException(String.format("Expecting record with %d columns but got %d", 16, columns.length));
            }
            details = ReportDetails.ok(parse(columns));
            LOGGER.info("Parsed OPR profile in {}", dataFile.getFileName());
        } catch (MalformattedRecordException e) {
            LOGGER.error(e.getMessage(), e);
            details = ReportDetails.error(e.getMessage());
        }

        return details;
    }

    @Override protected Logger getLogger() {
        return LOGGER;
    }

    private OprProfile parse(final String... columns) throws MalformattedRecordException {
        checkNotNull(columns, "Missing columns to parse from");
        try {
            String quizTakenTime = escapeQuote(columns[15]);
            return new OprProfile(
                    escapeQuote(columns[0]),
                    TIME_PARSER.parse(escapeQuote(columns[1])),
                    Coordinate.parseCommaSeparatedString(escapeQuote(columns[2])),
                    TIME_PARSER.parse(escapeQuote(columns[3])),
                    Integer.parseInt(escapeQuote(columns[4])),
                    Integer.parseInt(escapeQuote(columns[5])),
                    Integer.parseInt(escapeQuote(columns[6])),
                    Integer.parseInt(escapeQuote(columns[7])),
                    isNullOrEmpty(escapeQuote(columns[8])) ? null : TIME_PARSER.parse(escapeQuote(columns[8])),
                    Coordinate.parseCommaSeparatedString(escapeQuote(columns[9])),
                    Coordinate.parseCommaSeparatedString(escapeQuote(columns[10])),
                    escapeQuote(columns[11]),
                    TIME_PARSER.parse(escapeQuote(columns[12])),
                    escapeQuote(columns[13]),
                    escapeQuote(columns[14]),
                    isNullOrEmpty(quizTakenTime) ? null : TIME_PARSER.parse(quizTakenTime),
                    TIME_PARSER.parse(escapeQuote(columns[16]))
            );
        } catch (Exception e) {
            throw new MalformattedRecordException(String.format("Unable to parse OPR profile from %s", Arrays.asList(columns)), e);
        }
    }

}
