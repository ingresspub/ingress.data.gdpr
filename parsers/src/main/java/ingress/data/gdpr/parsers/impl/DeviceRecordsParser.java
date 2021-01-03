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

import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.PlainTextDataFileParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SgrAlpha
 */
public class DeviceRecordsParser extends PlainTextDataFileParser<List<UsedDevice>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRecordsParser.class);

    private static final DeviceRecordsParser INSTANCE = new DeviceRecordsParser();

    private DeviceRecordsParser() {
    }

    public static DeviceRecordsParser getDefault() {
        return INSTANCE;
    }

    @Override protected ReportDetails<List<UsedDevice>> readLines(final List<String> lines, final Path dataFile) {
        checkNotNull(lines, "No line to read from");
        if (lines.isEmpty()) {
            return ReportDetails.error(NO_DATA);
        }
        checkNotNull(dataFile, "Data file needs to be specified");
        try {
            final List<UsedDevice> records = lines.stream()
                    .filter(line -> !isNullOrEmpty(line))
                    .map(UsedDevice::new)
                    .collect(Collectors.toList());
            LOGGER.info("Parsed {} used devices in {}", records.size(), dataFile.getFileName());
            return ReportDetails.ok(records);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

    @Override protected Logger getLogger() {
        return LOGGER;
    }
}
