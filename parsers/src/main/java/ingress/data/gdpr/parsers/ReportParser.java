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
import static ingress.data.gdpr.models.utils.Preconditions.notEmptyString;
import static ingress.data.gdpr.models.utils.Preconditions.notNull;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.FILE_NOT_FOUND;

import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;
import ingress.data.gdpr.parsers.utils.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class ReportParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportParser.class);

    public static <T> ReportDetails<List<TimestampedRecord<T>>> parse(
            final List<Path> files,
            final String targetFileName,
            final SingleLineRecordParser<TimestampedRecord<T>> recordParser) {
        notNull(files, "Missing files to parse from");
        if (files.isEmpty()) {
            throw new IllegalArgumentException("No files to parse from");
        }
        notEmptyString(targetFileName, "Missing target file name");
        notNull(recordParser, "Missing record parser");

        Optional<Path> dataFile = files.stream()
                .filter(file -> file.getFileName().toString().equals(targetFileName))
                .findFirst();
        if (!dataFile.isPresent()) {
            LOGGER.warn("Can not find report named '{}', skipping ...", targetFileName);
            return ReportDetails.error(FILE_NOT_FOUND);
        }

        final ReportDetails<List<TimestampedRecord<T>>> details = parse(dataFile.get(), recordParser);
        if (details.isOk()) {
            LOGGER.info("Parsed {} records in {}", details.getData().size(), targetFileName);
        } else {
            LOGGER.warn("Ran into error when parsing {}: {}", targetFileName, details.getError());
        }

        return details;
    }

    public static <T> ReportDetails<List<TimestampedRecord<T>>> parse(
            final Path dataFile,
            final SingleLineRecordParser<TimestampedRecord<T>> recordParser) {
        notNull(dataFile, "Data file needs to be specified");
        notNull(recordParser, "Missing record parser");
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
            List<TimestampedRecord<T>> data = new LinkedList<>();
            for (int i = 1; i < lines.size(); i++) {    // Skip first line (header)
                final String line = lines.get(i);
                if (isEmptyString(line)) {
                    continue;
                }
                final String[] columns = line.split("\t");
                try {
                    final TimestampedRecord<T> record = recordParser.parse(columns);
                    data.add(record);
                } catch (MalformattedRecordException e) {
                    return ReportDetails.error(String.format("Found mal-formatted record at line %d: %s", i, e.getMessage()));
                }
            }
            return ReportDetails.ok(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

}
