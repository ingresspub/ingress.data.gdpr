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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ingress.data.gdpr.models.reports.BuildingReport;
import ingress.data.gdpr.models.reports.CombatReport;
import ingress.data.gdpr.models.reports.SummarizedReport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SgrAlpha
 */
public class SummarizerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SummarizerTest.class);

    @Test
    public void test() throws IOException {
        final String basePath = System.getenv("DATA_DIR");
        if (isEmptyString(basePath)) {
            LOGGER.warn("To run this test, you need to set a system variable named DATA_DIR to the dir contains the data files.");
            return;
        }
        final List<Path> files = Files.list(Paths.get(basePath)).collect(Collectors.toList());
        SummarizedReport rpt = Summarizer.summarize(files);
        assertNotNull(rpt);

        final CombatReport combatReport = rpt.getCombat();
        assertNotNull(combatReport);
        assertNotNull(combatReport.getResonatorsDestroyed());
        assertTrue(combatReport.getResonatorsDestroyed().isOk());
        assertNotNull(combatReport.getResonatorsDestroyed().getData());
        assertFalse(combatReport.getResonatorsDestroyed().getData().isEmpty());
        assertNotNull(combatReport.getPortalsNeutralized());
        assertTrue(combatReport.getPortalsNeutralized().isOk());
        assertNotNull(combatReport.getPortalsNeutralized().getData());
        assertFalse(combatReport.getPortalsNeutralized().getData().isEmpty());
        assertNotNull(combatReport.getLinksDestroyed());
        assertTrue(combatReport.getLinksDestroyed().isOk());
        assertNotNull(combatReport.getLinksDestroyed().getData());
        assertFalse(combatReport.getLinksDestroyed().getData().isEmpty());
        assertNotNull(combatReport.getFieldsDestroyed());
        assertTrue(combatReport.getFieldsDestroyed().isOk());
        assertNotNull(combatReport.getFieldsDestroyed().getData());
        assertFalse(combatReport.getFieldsDestroyed().getData().isEmpty());

        final BuildingReport buildingReport = rpt.getBuilding();
        assertNotNull(buildingReport);
        assertNotNull(buildingReport.getMindUnitsControlled());
        assertTrue(buildingReport.getMindUnitsControlled().isOk());
        assertFalse(buildingReport.getMindUnitsControlled().getData().isEmpty());
        assertNotNull(buildingReport.getMindUnitsControlledActive());
        assertNotNull(buildingReport.getMindUnitsControlledActive().getData());
        assertEquals(1, buildingReport.getMindUnitsControlledActive().getData().size());
        assertNotNull(buildingReport.getFieldsCreated());
        assertTrue(buildingReport.getFieldsCreated().isOk());
        assertFalse(buildingReport.getFieldsCreated().getData().isEmpty());
        assertNotNull(buildingReport.getFieldsCreatedActive());
        assertNotNull(buildingReport.getFieldsCreatedActive().getData());
        assertEquals(1, buildingReport.getFieldsCreatedActive().getData().size());
        assertNotNull(buildingReport.getLinksCreated());
        assertTrue(buildingReport.getLinksCreated().isOk());
        assertFalse(buildingReport.getLinksCreated().getData().isEmpty());
        assertNotNull(buildingReport.getLinkLengthInKm());
        assertTrue(buildingReport.getLinkLengthInKm().isOk());
        assertFalse(buildingReport.getLinkLengthInKm().getData().isEmpty());
        assertNotNull(buildingReport.getLinksCreatedActive());
        assertNotNull(buildingReport.getLinksCreatedActive().getData());
        assertEquals(1, buildingReport.getLinksCreatedActive().getData().size());
        assertNotNull(buildingReport.getPortalsCaptured());
        assertTrue(buildingReport.getPortalsCaptured().isOk());
        assertFalse(buildingReport.getPortalsCaptured().getData().isEmpty());
        assertNotNull(buildingReport.getPortalsOwned());
        assertNotNull(buildingReport.getPortalsOwned().getData());
        assertEquals(1, buildingReport.getPortalsOwned().getData().size());
        assertNotNull(buildingReport.getResonatorsDeployed());
        assertTrue(buildingReport.getResonatorsDeployed().isOk());
        assertFalse(buildingReport.getResonatorsDeployed().getData().isEmpty());
        assertNotNull(buildingReport.getModsDeployed());
        assertTrue(buildingReport.getModsDeployed().isOk());
        assertFalse(buildingReport.getModsDeployed().getData().isEmpty());
        assertNotNull(buildingReport.getXmRecharged());
        assertTrue(buildingReport.getXmRecharged().isOk());
        assertFalse(buildingReport.getXmRecharged().getData().isEmpty());

        assertNotNull(rpt.getUsedDevices());
        assertNotNull(rpt.getHealth());
        assertNotNull(rpt.getMentoring());
    }

}
