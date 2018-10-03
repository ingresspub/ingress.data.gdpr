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

import ingress.data.gdpr.models.reports.RawDataReport;
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
public class RawDataParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RawDataParserTest.class);

    @Test
    public void test() throws IOException {
        final String basePath = System.getenv("DATA_DIR");
        if (isEmptyString(basePath)) {
            LOGGER.warn("To run this test, you need to set a system variable named DATA_DIR to the dir contains the data files.");
            return;
        }
        final List<Path> files = Files.list(Paths.get(basePath)).collect(Collectors.toList());
        RawDataReport rpt = RawDataParser.parse(files);
        assertNotNull(rpt);

        assertNotNull(rpt.getGameLogs());
        assertTrue(rpt.getGameLogs().getError(), rpt.getGameLogs().isOk());
        assertNotNull(rpt.getGameLogs().getData());

        assertNotNull(rpt.getCommMentions());
        assertTrue(rpt.getCommMentions().getError(), rpt.getCommMentions().isOk());
        assertNotNull(rpt.getCommMentions().getData());

        assertNotNull(rpt.getUsedDevices());
        assertTrue(rpt.getUsedDevices().getError(), rpt.getUsedDevices().isOk());
        assertNotNull(rpt.getUsedDevices().getData());

        assertNotNull(rpt.getOprProfile());
        assertTrue(rpt.getOprProfile().getError(), rpt.getOprProfile().isOk());
        assertNotNull(rpt.getOprProfile().getData());
        assertNotNull(rpt.getOprAgreements());
        assertTrue(rpt.getOprAgreements().getError(), rpt.getOprAgreements().isOk());
        assertNotNull(rpt.getOprAgreements().getData());
        assertFalse(rpt.getOprAgreements().getData().isEmpty());
        assertNotNull(rpt.getAllPortalsApproved());
        assertTrue(rpt.getAllPortalsApproved().getError(), rpt.getAllPortalsApproved().isOk());
        assertNotNull(rpt.getAllPortalsApproved().getData());
        assertFalse(rpt.getAllPortalsApproved().getData().isEmpty());
        assertNotNull(rpt.getSeerPortals());
        assertTrue(rpt.getSeerPortals().getError(), rpt.getSeerPortals().isOk());
        assertNotNull(rpt.getSeerPortals().getData());
        assertFalse(rpt.getSeerPortals().getData().isEmpty());
        assertNotNull(rpt.getPortalsVisited());
        assertTrue(rpt.getPortalsVisited().getError(), rpt.getPortalsVisited().isOk());
        assertNotNull(rpt.getPortalsVisited().getData());
        assertFalse(rpt.getPortalsVisited().getData().isEmpty());
        assertNotNull(rpt.getXmCollected());
        assertTrue(rpt.getXmCollected().getError(), rpt.getXmCollected().isOk());
        assertNotNull(rpt.getXmCollected().getData());
        assertFalse(rpt.getXmCollected().getData().isEmpty());

        assertNotNull(rpt.getKilometersWalked());
        assertTrue(rpt.getKilometersWalked().getError(), rpt.getKilometersWalked().isOk());
        assertNotNull(rpt.getKilometersWalked().getData());
        assertFalse(rpt.getKilometersWalked().getData().isEmpty());

        assertNotNull(rpt.getMindUnitsControlled());
        assertTrue(rpt.getMindUnitsControlled().getError(), rpt.getMindUnitsControlled().isOk());
        assertFalse(rpt.getMindUnitsControlled().getData().isEmpty());
        assertNotNull(rpt.getMindUnitsControlledActive());
        assertTrue(rpt.getMindUnitsControlledActive().getError(), rpt.getMindUnitsControlledActive().isOk());
        assertNotNull(rpt.getMindUnitsControlledActive().getData());
        assertEquals(1, rpt.getMindUnitsControlledActive().getData().size());
        assertNotNull(rpt.getFieldsCreated());
        assertTrue(rpt.getFieldsCreated().getError(), rpt.getFieldsCreated().isOk());
        assertFalse(rpt.getFieldsCreated().getData().isEmpty());
        assertNotNull(rpt.getFieldsCreatedActive());
        assertTrue(rpt.getFieldsCreatedActive().getError(), rpt.getFieldsCreatedActive().isOk());
        assertNotNull(rpt.getFieldsCreatedActive().getData());
        assertEquals(1, rpt.getFieldsCreatedActive().getData().size());
        assertNotNull(rpt.getLinksCreated());
        assertTrue(rpt.getLinksCreated().getError(), rpt.getLinksCreated().isOk());
        assertFalse(rpt.getLinksCreated().getData().isEmpty());
        assertNotNull(rpt.getLinkLengthInKm());
        assertTrue(rpt.getLinkLengthInKm().getError(), rpt.getLinkLengthInKm().isOk());
        assertFalse(rpt.getLinkLengthInKm().getData().isEmpty());
        assertNotNull(rpt.getLinksCreatedActive());
        assertTrue(rpt.getLinksCreatedActive().getError(), rpt.getLinksCreatedActive().isOk());
        assertNotNull(rpt.getLinksCreatedActive().getData());
        assertEquals(1, rpt.getLinksCreatedActive().getData().size());
        assertNotNull(rpt.getPortalsCaptured());
        assertTrue(rpt.getPortalsCaptured().getError(), rpt.getPortalsCaptured().isOk());
        assertFalse(rpt.getPortalsCaptured().getData().isEmpty());
        assertNotNull(rpt.getPortalsOwned());
        assertTrue(rpt.getPortalsOwned().getError(), rpt.getPortalsOwned().isOk());
        assertNotNull(rpt.getPortalsOwned().getData());
        assertEquals(1, rpt.getPortalsOwned().getData().size());
        assertNotNull(rpt.getResonatorsDeployed());
        assertTrue(rpt.getResonatorsDeployed().getError(), rpt.getResonatorsDeployed().isOk());
        assertFalse(rpt.getResonatorsDeployed().getData().isEmpty());
        assertNotNull(rpt.getModsDeployed());
        assertTrue(rpt.getModsDeployed().getError(), rpt.getModsDeployed().isOk());
        assertFalse(rpt.getModsDeployed().getData().isEmpty());
        assertNotNull(rpt.getXmRecharged());
        assertTrue(rpt.getXmRecharged().getError(), rpt.getXmRecharged().isOk());
        assertFalse(rpt.getXmRecharged().getData().isEmpty());

        assertNotNull(rpt.getResonatorsDestroyed());
        assertTrue(rpt.getResonatorsDestroyed().getError(), rpt.getResonatorsDestroyed().isOk());
        assertNotNull(rpt.getResonatorsDestroyed().getData());
        assertFalse(rpt.getResonatorsDestroyed().getData().isEmpty());
        assertNotNull(rpt.getPortalsNeutralized());
        assertTrue(rpt.getPortalsNeutralized().getError(), rpt.getPortalsNeutralized().isOk());
        assertNotNull(rpt.getPortalsNeutralized().getData());
        assertFalse(rpt.getPortalsNeutralized().getData().isEmpty());
        assertNotNull(rpt.getLinksDestroyed());
        assertTrue(rpt.getLinksDestroyed().getError(), rpt.getLinksDestroyed().isOk());
        assertNotNull(rpt.getLinksDestroyed().getData());
        assertFalse(rpt.getLinksDestroyed().getData().isEmpty());
        assertNotNull(rpt.getFieldsDestroyed());
        assertTrue(rpt.getFieldsDestroyed().getError(),rpt.getFieldsDestroyed().isOk());
        assertNotNull(rpt.getFieldsDestroyed().getData());
        assertFalse(rpt.getFieldsDestroyed().getData().isEmpty());

        assertNotNull(rpt.getPortalHeldDays());
        assertTrue(rpt.getPortalHeldDays().getError(), rpt.getPortalHeldDays().isOk());
        assertFalse(rpt.getPortalHeldDays().getData().isEmpty());
        assertNotNull(rpt.getLinkHeldDays());
        assertTrue(rpt.getLinkHeldDays().getError(), rpt.getLinkHeldDays().isOk());
        assertFalse(rpt.getLinkHeldDays().getData().isEmpty());
        assertNotNull(rpt.getLinkLengthInKmTimesDaysHeld());
        assertTrue(rpt.getLinkLengthInKmTimesDaysHeld().getError(), rpt.getLinkLengthInKmTimesDaysHeld().isOk());
        assertFalse(rpt.getLinkLengthInKmTimesDaysHeld().getData().isEmpty());
        assertNotNull(rpt.getFieldHeldDays());
        assertTrue(rpt.getFieldHeldDays().getError(), rpt.getFieldHeldDays().isOk());
        assertFalse(rpt.getFieldHeldDays().getData().isEmpty());
        assertNotNull(rpt.getMindUnitsTimesDaysHeld());
        assertTrue(rpt.getMindUnitsTimesDaysHeld().getError(), rpt.getMindUnitsTimesDaysHeld().isOk());
        assertFalse(rpt.getMindUnitsTimesDaysHeld().getData().isEmpty());

        assertNotNull(rpt.getHacks());
        assertTrue(rpt.getHacks().getError(), rpt.getHacks().isOk());
        assertNotNull(rpt.getHacks().getData());
        assertFalse(rpt.getHacks().getData().isEmpty());
        assertNotNull(rpt.getGlyphHackPoints());
        assertTrue(rpt.getGlyphHackPoints().getError(), rpt.getGlyphHackPoints().isOk());
        assertNotNull(rpt.getGlyphHackPoints().getData());
        assertFalse(rpt.getGlyphHackPoints().getData().isEmpty());
        assertNotNull(rpt.getGlyphHackOnePerfect());
        assertTrue(rpt.getGlyphHackOnePerfect().getError(), rpt.getGlyphHackOnePerfect().isOk());
        assertNotNull(rpt.getGlyphHackOnePerfect().getData());
        assertFalse(rpt.getGlyphHackOnePerfect().getData().isEmpty());
        assertNotNull(rpt.getGlyphHackThreePerfect());
        assertTrue(rpt.getGlyphHackThreePerfect().getError(), rpt.getGlyphHackThreePerfect().isOk());
        assertNotNull(rpt.getGlyphHackThreePerfect().getData());
        assertFalse(rpt.getGlyphHackThreePerfect().getData().isEmpty());
        assertNotNull(rpt.getGlyphHackFourPerfect());
        assertTrue(rpt.getGlyphHackFourPerfect().getError(), rpt.getGlyphHackFourPerfect().isOk());
        assertNotNull(rpt.getGlyphHackFourPerfect().getData());
        assertFalse(rpt.getGlyphHackFourPerfect().getData().isEmpty());
        assertNotNull(rpt.getGlyphHackFivePerfect());
        assertTrue(rpt.getGlyphHackFivePerfect().getError(), rpt.getGlyphHackFivePerfect().isOk());
        assertNotNull(rpt.getGlyphHackFivePerfect().getData());
        assertFalse(rpt.getGlyphHackFivePerfect().getData().isEmpty());

        assertNotNull(rpt.getAgentsRecruited());
        assertTrue(rpt.getAgentsRecruited().getError(), rpt.getAgentsRecruited().isOk());
        assertNotNull(rpt.getAgentsRecruited().getData());
        assertFalse(rpt.getAgentsRecruited().getData().isEmpty());

        assertNotNull(rpt.getExo5ControlFieldsCreated());
        assertTrue(rpt.getExo5ControlFieldsCreated().getError(), rpt.getExo5ControlFieldsCreated().isOk());
        assertNotNull(rpt.getExo5ControlFieldsCreated().getData());
        assertFalse(rpt.getExo5ControlFieldsCreated().getData().isEmpty());
        assertNotNull(rpt.getMagusBuilderSlotsDeployed());
        assertTrue(rpt.getMagusBuilderSlotsDeployed().getError(), rpt.getMagusBuilderSlotsDeployed().isOk());
        assertNotNull(rpt.getMagusBuilderSlotsDeployed().getData());
        assertFalse(rpt.getMagusBuilderSlotsDeployed().getData().isEmpty());
        assertNotNull(rpt.getNeutralizerUniquePortalsDestroyed());
        assertTrue(rpt.getNeutralizerUniquePortalsDestroyed().getError(), rpt.getNeutralizerUniquePortalsDestroyed().isOk());
        assertNotNull(rpt.getNeutralizerUniquePortalsDestroyed().getData());
        assertFalse(rpt.getNeutralizerUniquePortalsDestroyed().getData().isEmpty());
        assertNotNull(rpt.getMissionDayPoints());
        assertTrue(rpt.getMissionDayPoints().getError(), rpt.getMissionDayPoints().isOk());
        assertNotNull(rpt.getMissionDayPoints().getData());
        assertFalse(rpt.getMissionDayPoints().getData().isEmpty());

    }

}
