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
import static ingress.data.gdpr.parsers.utils.ErrorConstants.UNREADABLE_FILE;

import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.reports.ReportDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SgrAlpha
 */
public class DeviceRecordsParser implements DataFileParser<List<UsedDevice>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRecordsParser.class);

    private static final DeviceRecordsParser INSTANCE = new DeviceRecordsParser();

    private DeviceRecordsParser() {
    }

    public static DeviceRecordsParser getDefault() {
        return INSTANCE;
    }

    @Override public ReportDetails<List<UsedDevice>> parse(final Path dataFile) {
        notNull(dataFile, "Data file needs to be specified");
        if (!Files.isRegularFile(dataFile)) {
            LOGGER.warn("{} is not a regular file", dataFile.getFileName());
            return ReportDetails.error(NOT_REGULAR_FILE);
        }
        if (!Files.isReadable(dataFile)) {
            LOGGER.warn("{} is not a readable file", dataFile.getFileName());
            return ReportDetails.error(UNREADABLE_FILE);
        }
        try {
            final List<String> lines = Files.readAllLines(dataFile);
            final List<UsedDevice> records = lines.stream()
                    .filter(line -> !isEmptyString(line))
                    .map(UsedDevice::new)
                    .collect(Collectors.toList());
            LOGGER.info("Parsed {} used devices in {}", records.size(), dataFile.getFileName());
            return ReportDetails.ok(records);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }
}
