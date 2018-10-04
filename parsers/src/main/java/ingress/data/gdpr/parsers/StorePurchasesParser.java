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
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NOT_REGULAR_FILE;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.UNREADABLE_FILE;

import ingress.data.gdpr.models.records.StorePurchase;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.utils.DefaultPacificDateTimeParser;
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
public class StorePurchasesParser implements DataFileParser<List<StorePurchase>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePurchasesParser.class);

    private static final DefaultPacificDateTimeParser TIME_PARSER = new DefaultPacificDateTimeParser(StorePurchase.TIME_PATTERN);
    private static final String COLUMN_SEPARATOR = "\t";

    private static final StorePurchasesParser INSTANCE = new StorePurchasesParser();

    private StorePurchasesParser() {
    }

    public static StorePurchasesParser getDefault() {
        return INSTANCE;
    }

    @Override public ReportDetails<List<StorePurchase>> parse(final Path dataFile) {
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
            if (lines.size() < 2) {
                return ReportDetails.error(NO_DATA);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }

        try {
            List<StorePurchase> data = new LinkedList<>();
            for (int i = 1; i < lines.size(); i++) {    // Skip first line (header)
                final String line = lines.get(i);
                if (isEmptyString(line)) {
                    continue;
                }
                final String[] columns = line.split(COLUMN_SEPARATOR, 5);
                final ZonedDateTime time;
                try {
                    time = TIME_PARSER.parse(columns[0]);
                } catch (Exception e) {
                    return ReportDetails.error(String.format("Expecting a valid timestamp (%s) at the beginning of line %d, but got %s", StorePurchase.TIME_PATTERN, i, columns[0]));
                }
                final String balanceStr = columns[3];
                Integer balance = isEmptyString(balanceStr) ? null : Integer.parseInt(balanceStr);
                data.add(new StorePurchase(time, columns[1], columns[2], balance, columns[4]));
            }
            if (data.size() > 1) {
                LOGGER.info("Parsed {} records from {}", data.size(), dataFile.getFileName());
            } else {
                LOGGER.info("Parsed {} record from {}", data.size(), dataFile.getFileName());
            }
            return ReportDetails.ok(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

}
