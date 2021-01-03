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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;

import ingress.data.gdpr.models.records.GameLog;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.PlainTextDataFileParser;
import ingress.data.gdpr.parsers.utils.DefaultPacificDateTimeParser;

import io.sgr.geometry.Coordinate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class GameLogParser extends PlainTextDataFileParser<List<GameLog>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameLogParser.class);

    private static final DefaultPacificDateTimeParser TIME_PARSER = new DefaultPacificDateTimeParser(GameLog.TIME_PATTERN);

    private static final GameLogParser INSTANCE = new GameLogParser();

    private GameLogParser() {
    }

    public static GameLogParser getDefault() {
        return INSTANCE;
    }

    @Override protected ReportDetails<List<GameLog>> readLines(final List<String> lines, final Path dataFile) {
        checkNotNull(lines, "No line to read from");
        if (lines.size() < 2) {
            return ReportDetails.error(NO_DATA);
        }
        checkNotNull(dataFile, "Data file needs to be specified");
        try {
            List<GameLog> data = new LinkedList<>();
            for (int i = 1; i < lines.size(); i++) {    // Skip first line (header)
                final String line = lines.get(i);
                if (isNullOrEmpty(line)) {
                    continue;
                }
                final String[] columns = line.split(SEPARATOR_TAB);
                if (columns.length != 5 && columns.length != 6) {
                    return ReportDetails.error(String.format("Expecting exactly %d columns in line %d but found %d", 6, i, columns.length));
                }
                final ZonedDateTime time;
                try {
                    time = TIME_PARSER.parse(columns[0]);
                } catch (Exception e) {
                    return ReportDetails.error(String.format("Expecting a valid timestamp (%s) at the beginning of line %d, but got %s", GameLog.TIME_PATTERN, i, columns[0]));
                }
                final Coordinate location;
                if ("None".equalsIgnoreCase(columns[1]) || "None".equalsIgnoreCase(columns[2])) {
                    location = null;
                } else {
                    try {
                        location = new Coordinate(Double.parseDouble(columns[1]), Double.parseDouble(columns[2]));
                    } catch (Exception e) {
                        return ReportDetails.error(String.format("Expecting a valid location at line %d, but got %s,%s", i, columns[1], columns[2]));
                    }
                }
                data.add(new GameLog(time, location, columns[3], columns[4], columns.length == 6 ? columns[5] : null));
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

    @Override protected Logger getLogger() {
        return LOGGER;
    }

}
