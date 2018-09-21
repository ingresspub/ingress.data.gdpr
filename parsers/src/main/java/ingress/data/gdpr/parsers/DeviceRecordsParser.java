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

import ingress.data.gdpr.models.DeviceRecord;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.utils.ErrorConstants;
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
public class DeviceRecordsParser implements SingleFileParser<List<DeviceRecord>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRecordsParser.class);

    @Override public ReportDetails<List<DeviceRecord>> parse(final Path dataFile) {
        notNull(dataFile, "Data file needs to be specified");
        if (!Files.isRegularFile(dataFile)) {
            return ReportDetails.error(ErrorConstants.NOT_REGULAR_FILE);
        }
        if (!Files.isReadable(dataFile)) {
            return ReportDetails.error(ErrorConstants.UNREADABLE_FILE);
        }
        try {
            final List<String> lines = Files.readAllLines(dataFile);
            return ReportDetails.ok(
                    lines.stream()
                            .filter(line -> !isEmptyString(line))
                            .map(DeviceRecord::new)
                            .collect(Collectors.toList())
            );
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

}
