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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ingress.data.gdpr.models.records.profile.AgentProfile;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.models.utils.JsonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class AgentProfileParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentProfileParserTest.class);

    private static final String TARGET_FILE_NAME = "profile.txt";

    @Test
    public void test() throws IOException {
        final String basePath = System.getenv("DATA_DIR");
        if (isEmptyString(basePath)) {
            LOGGER.warn("To run this test, you need to set a system variable named DATA_DIR to the dir contains the data files.");
            return;
        }
        final Optional<Path> dataFile = Files
                .list(Paths.get(basePath))
                .filter(path -> TARGET_FILE_NAME.equals(path.getFileName().toString()))
                .findFirst();
        if (!dataFile.isPresent()) {
            LOGGER.warn("No {} found under {}, skipping ...", TARGET_FILE_NAME, basePath);
            return;
        }

        ReportDetails<AgentProfile> profileReport = AgentProfileParser.getDefault().parse(dataFile.get());
        assertNotNull(profileReport);
        assertTrue(profileReport.getError(), profileReport.isOk());
        final AgentProfile profile = profileReport.getData();
        assertNotNull(profile);
        LOGGER.info(JsonUtil.toPrettyJson(profile));
    }

}
