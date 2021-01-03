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

import static com.google.common.base.Strings.isNullOrEmpty;
import static ingress.data.gdpr.parsers.utils.DataFileNames.COMM_MENTIONS_TSV;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ingress.data.gdpr.models.records.CommMention;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.models.utils.JsonUtil;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class CommMentionParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommMentionParserTest.class);

    @Test
    public void test() throws IOException {
        final String basePath = System.getenv("DATA_DIR");
        if (isNullOrEmpty(basePath)) {
            LOGGER.warn("To run this test, you need to set a system variable named DATA_DIR to the dir contains the data files.");
            return;
        }
        final Optional<Path> dataFile = Files
                .list(Paths.get(basePath))
                .filter(path -> COMM_MENTIONS_TSV.equals(path.getFileName().toString()))
                .findFirst();
        if (!dataFile.isPresent()) {
            LOGGER.warn("No {} found under {}, skipping ...", COMM_MENTIONS_TSV, basePath);
            return;
        }

        ReportDetails<List<CommMention>> commMentionsReport = CommMentionParser.getDefault().parse(dataFile.get());
        assertNotNull(commMentionsReport);
        assertTrue(commMentionsReport.getError(), commMentionsReport.isOk());
        final List<CommMention> mentions = commMentionsReport.getData();
        assertNotNull(mentions);
        assertFalse(mentions.isEmpty());
        LOGGER.info(JsonUtil.toPrettyJson(mentions));
    }

}
