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

import ingress.data.gdpr.models.records.CommMention;
import ingress.data.gdpr.models.records.GameLog;
import ingress.data.gdpr.models.records.StorePurchase;
import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.records.ZendeskTicket;
import ingress.data.gdpr.models.records.mission.Mission;
import ingress.data.gdpr.models.records.opr.OprAssignmentLogItem;
import ingress.data.gdpr.models.records.opr.OprProfile;
import ingress.data.gdpr.models.records.opr.OprSubmissionLogItem;
import ingress.data.gdpr.models.records.profile.AgentProfile;
import ingress.data.gdpr.models.reports.RawDataReport;
import ingress.data.gdpr.models.reports.ReportDetails;
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

        assertTrue(rpt.getAgentProfile().isPresent());
        final ReportDetails<AgentProfile> agentProfile = rpt.getAgentProfile().get();
        assertTrue(agentProfile.getError(), agentProfile.isOk());
        assertNotNull(agentProfile.getData());

        assertTrue(rpt.getGameLogs().isPresent());
        final ReportDetails<List<GameLog>> gameLogs = rpt.getGameLogs().get();
        assertTrue(gameLogs.getError(), gameLogs.isOk());
        assertNotNull(gameLogs.getData());
        assertFalse(gameLogs.getData().isEmpty());

        assertTrue(rpt.getCommMentions().isPresent());
        final ReportDetails<List<CommMention>> commMentions = rpt.getCommMentions().get();
        assertTrue(commMentions.getError(), commMentions.isOk());
        assertNotNull(commMentions.getData());
        assertFalse(commMentions.getData().isEmpty());

        assertTrue(rpt.getUsedDevices().isPresent());
        final ReportDetails<List<UsedDevice>> usedDevices = rpt.getUsedDevices().get();
        assertTrue(usedDevices.getError(), usedDevices.isOk());
        assertNotNull(usedDevices.getData());
        assertFalse(usedDevices.getData().isEmpty());

        assertTrue(rpt.getOprProfile().isPresent());
        final ReportDetails<OprProfile> oprProfile = rpt.getOprProfile().get();
        assertTrue(oprProfile.getError(), oprProfile.isOk());
        assertNotNull(oprProfile.getData());

        assertTrue(rpt.getOprAgreements().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements = rpt.getOprAgreements().get();
        assertTrue(oprAgreements.getError(), oprAgreements.isOk());
        assertNotNull(oprAgreements.getData());
        assertFalse(oprAgreements.getData().isEmpty());

        assertTrue(rpt.getOprAssignmentLogs().isPresent());
        final ReportDetails<List<OprAssignmentLogItem>> oprAssignmentLogs = rpt.getOprAssignmentLogs().get();
        assertTrue(oprAssignmentLogs.getError(), oprAssignmentLogs.isOk());
        assertNotNull(oprAssignmentLogs.getData());
        assertFalse(oprAssignmentLogs.getData().isEmpty());

        assertTrue(rpt.getOprSubmissionLogs().isPresent());
        final ReportDetails<List<OprSubmissionLogItem>> oprSubmissionLogs = rpt.getOprSubmissionLogs().get();
        assertTrue(oprSubmissionLogs.getError(), oprSubmissionLogs.isOk());
        assertNotNull(oprSubmissionLogs.getData());
        assertFalse(oprSubmissionLogs.getData().isEmpty());

        assertTrue(rpt.getAllPortalsApproved().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved = rpt.getAllPortalsApproved().get();
        assertTrue(allPortalsApproved.getError(), allPortalsApproved.isOk());
        assertNotNull(allPortalsApproved.getData());
        assertFalse(allPortalsApproved.getData().isEmpty());

        assertTrue(rpt.getSeerPortals().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> seerPortals = rpt.getSeerPortals().get();
        assertTrue(seerPortals.getError(), seerPortals.isOk());
        assertNotNull(seerPortals.getData());
        assertFalse(seerPortals.getData().isEmpty());

        assertTrue(rpt.getPortalsVisited().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited = rpt.getPortalsVisited().get();
        assertTrue(portalsVisited.getError(), portalsVisited.isOk());
        assertNotNull(portalsVisited.getData());
        assertFalse(portalsVisited.getData().isEmpty());

        assertTrue(rpt.getXmCollected().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> xmCollected = rpt.getXmCollected().get();
        assertTrue(xmCollected.getError(), xmCollected.isOk());
        assertNotNull(xmCollected.getData());
        assertFalse(xmCollected.getData().isEmpty());

        assertTrue(rpt.getKilometersWalked().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> kilometersWalked = rpt.getKilometersWalked().get();
        assertTrue(kilometersWalked.getError(), kilometersWalked.isOk());
        assertNotNull(kilometersWalked.getData());
        assertFalse(kilometersWalked.getData().isEmpty());

        assertTrue(rpt.getMindUnitsControlled().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> mindUnitsControlled = rpt.getMindUnitsControlled().get();
        assertTrue(mindUnitsControlled.getError(), mindUnitsControlled.isOk());
        assertNotNull(mindUnitsControlled.getData());
        assertFalse(mindUnitsControlled.getData().isEmpty());

        assertTrue(rpt.getMindUnitsControlledActive().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> mindUnitsControlledActive = rpt.getMindUnitsControlledActive().get();
        assertTrue(mindUnitsControlledActive.getError(), mindUnitsControlledActive.isOk());
        assertNotNull(mindUnitsControlledActive.getData());
        assertEquals(1, mindUnitsControlledActive.getData().size());

        assertTrue(rpt.getFieldsCreated().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> fieldsCreated = rpt.getFieldsCreated().get();
        assertTrue(fieldsCreated.getError(), fieldsCreated.isOk());
        assertNotNull(fieldsCreated.getData());
        assertFalse(fieldsCreated.getData().isEmpty());

        assertTrue(rpt.getFieldsCreatedActive().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> fieldsCreatedActive = rpt.getFieldsCreatedActive().get();
        assertTrue(fieldsCreatedActive.getError(), fieldsCreatedActive.isOk());
        assertNotNull(fieldsCreatedActive.getData());
        assertEquals(1, fieldsCreatedActive.getData().size());

        assertTrue(rpt.getLinksCreated().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> linksCreated = rpt.getLinksCreated().get();
        assertTrue(linksCreated.getError(), linksCreated.isOk());
        assertNotNull(linksCreated.getData());
        assertFalse(linksCreated.getData().isEmpty());

        assertTrue(rpt.getLinkLengthInKm().isPresent());
        final ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKm = rpt.getLinkLengthInKm().get();
        assertTrue(linkLengthInKm.getError(), linkLengthInKm.isOk());
        assertNotNull(linkLengthInKm.getData());
        assertFalse(linkLengthInKm.getData().isEmpty());

        assertTrue(rpt.getLinksCreatedActive().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> linksCreatedActive = rpt.getLinksCreatedActive().get();
        assertTrue(linksCreatedActive.getError(), linksCreatedActive.isOk());
        assertNotNull(linksCreatedActive.getData());
        assertEquals(1, linksCreatedActive.getData().size());

        assertTrue(rpt.getPortalsCaptured().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsCaptured = rpt.getPortalsCaptured().get();
        assertTrue(portalsCaptured.getError(), portalsCaptured.isOk());
        assertNotNull(portalsCaptured.getData());
        assertFalse(portalsCaptured.getData().isEmpty());

        assertTrue(rpt.getPortalsOwned().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsOwned = rpt.getPortalsOwned().get();
        assertTrue(portalsOwned.getError(), portalsOwned.isOk());
        assertNotNull(portalsOwned.getData());
        assertEquals(1, portalsOwned.getData().size());

        assertTrue(rpt.getResonatorsDeployed().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDeployed = rpt.getResonatorsDeployed().get();
        assertTrue(resonatorsDeployed.getError(), resonatorsDeployed.isOk());
        assertNotNull(resonatorsDeployed.getData());
        assertFalse(resonatorsDeployed.getData().isEmpty());

        assertTrue(rpt.getModsDeployed().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> modsDeployed = rpt.getModsDeployed().get();
        assertTrue(modsDeployed.getError(), modsDeployed.isOk());
        assertNotNull(modsDeployed.getData());
        assertFalse(modsDeployed.getData().isEmpty());

        assertTrue(rpt.getXmRecharged().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> xmRecharged = rpt.getXmRecharged().get();
        assertTrue(xmRecharged.getError(), xmRecharged.isOk());
        assertNotNull(xmRecharged.getData());
        assertFalse(xmRecharged.getData().isEmpty());

        assertTrue(rpt.getResonatorsDestroyed().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDestroyed = rpt.getResonatorsDestroyed().get();
        assertTrue(resonatorsDestroyed.getError(), resonatorsDestroyed.isOk());
        assertNotNull(resonatorsDestroyed.getData());
        assertFalse(resonatorsDestroyed.getData().isEmpty());

        assertTrue(rpt.getPortalsNeutralized().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsNeutralized = rpt.getPortalsNeutralized().get();
        assertTrue(portalsNeutralized.getError(), portalsNeutralized.isOk());
        assertNotNull(portalsNeutralized.getData());
        assertFalse(portalsNeutralized.getData().isEmpty());

        assertTrue(rpt.getLinksDestroyed().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> linksDestroyed = rpt.getLinksDestroyed().get();
        assertTrue(linksDestroyed.getError(), linksDestroyed.isOk());
        assertNotNull(linksDestroyed.getData());
        assertFalse(linksDestroyed.getData().isEmpty());

        assertTrue(rpt.getFieldsDestroyed().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> fieldsDestroyed = rpt.getFieldsDestroyed().get();
        assertTrue(fieldsDestroyed.getError(), fieldsDestroyed.isOk());
        assertNotNull(fieldsDestroyed.getData());
        assertFalse(fieldsDestroyed.getData().isEmpty());

        assertTrue(rpt.getPortalHeldDays().isPresent());
        final ReportDetails<List<TimestampedRecord<Double>>> portalHeldDays = rpt.getPortalHeldDays().get();
        assertTrue(portalHeldDays.getError(), portalHeldDays.isOk());
        assertNotNull(portalHeldDays.getData());
        assertFalse(portalHeldDays.getData().isEmpty());

        assertTrue(rpt.getLinkHeldDays().isPresent());
        final ReportDetails<List<TimestampedRecord<Double>>> linkHeldDays = rpt.getLinkHeldDays().get();
        assertTrue(linkHeldDays.getError(), linkHeldDays.isOk());
        assertNotNull(linkHeldDays.getData());
        assertFalse(linkHeldDays.getData().isEmpty());

        assertTrue(rpt.getLinkLengthInKmTimesDaysHeld().isPresent());
        final ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKmTimesDaysHeld = rpt.getLinkLengthInKmTimesDaysHeld().get();
        assertTrue(linkLengthInKmTimesDaysHeld.getError(), linkLengthInKmTimesDaysHeld.isOk());
        assertNotNull(linkLengthInKmTimesDaysHeld.getData());
        assertFalse(linkLengthInKmTimesDaysHeld.getData().isEmpty());

        assertTrue(rpt.getFieldHeldDays().isPresent());
        final ReportDetails<List<TimestampedRecord<Double>>> fieldHeldDays = rpt.getFieldHeldDays().get();
        assertTrue(fieldHeldDays.getError(), fieldHeldDays.isOk());
        assertNotNull(fieldHeldDays.getData());
        assertFalse(fieldHeldDays.getData().isEmpty());

        assertTrue(rpt.getMindUnitsTimesDaysHeld().isPresent());
        final ReportDetails<List<TimestampedRecord<Double>>> mindUnitsTimesDaysHeld = rpt.getMindUnitsTimesDaysHeld().get();
        assertTrue(mindUnitsTimesDaysHeld.getError(), mindUnitsTimesDaysHeld.isOk());
        assertNotNull(mindUnitsTimesDaysHeld.getData());
        assertFalse(mindUnitsTimesDaysHeld.getData().isEmpty());

        assertTrue(rpt.getHacks().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> hacks = rpt.getHacks().get();
        assertTrue(hacks.getError(), hacks.isOk());
        assertNotNull(hacks.getData());
        assertFalse(hacks.getData().isEmpty());

        assertTrue(rpt.getGlyphHackPoints().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackPoints = rpt.getGlyphHackPoints().get();
        assertTrue(glyphHackPoints.getError(), glyphHackPoints.isOk());
        assertNotNull(glyphHackPoints.getData());
        assertFalse(glyphHackPoints.getData().isEmpty());

        assertTrue(rpt.getGlyphHackOnePerfect().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackOnePerfect = rpt.getGlyphHackOnePerfect().get();
        assertTrue(glyphHackOnePerfect.getError(), glyphHackOnePerfect.isOk());
        assertNotNull(glyphHackOnePerfect.getData());
        assertFalse(glyphHackOnePerfect.getData().isEmpty());

        assertTrue(rpt.getGlyphHackThreePerfect().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackThreePerfect = rpt.getGlyphHackThreePerfect().get();
        assertTrue(glyphHackThreePerfect.getError(), glyphHackThreePerfect.isOk());
        assertNotNull(glyphHackThreePerfect.getData());
        assertFalse(glyphHackThreePerfect.getData().isEmpty());

        assertTrue(rpt.getGlyphHackFourPerfect().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackFourPerfect = rpt.getGlyphHackFourPerfect().get();
        assertTrue(glyphHackFourPerfect.getError(), glyphHackFourPerfect.isOk());
        assertNotNull(glyphHackFourPerfect.getData());
        assertFalse(glyphHackFourPerfect.getData().isEmpty());

        assertTrue(rpt.getGlyphHackFivePerfect().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackFivePerfect = rpt.getGlyphHackFivePerfect().get();
        assertTrue(glyphHackFivePerfect.getError(), glyphHackFivePerfect.isOk());
        assertNotNull(glyphHackFivePerfect.getData());
        assertFalse(glyphHackFivePerfect.getData().isEmpty());

        assertTrue(rpt.getAgentsRecruited().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> agentsRecruited = rpt.getAgentsRecruited().get();
        assertTrue(agentsRecruited.getError(), agentsRecruited.isOk());
        assertNotNull(agentsRecruited.getData());
        assertFalse(agentsRecruited.getData().isEmpty());

        assertTrue(rpt.getExo5ControlFieldsCreated().isPresent());
        final ReportDetails<List<TimestampedRecord<Float>>> exo5ControlFieldsCreated = rpt.getExo5ControlFieldsCreated().get();
        assertTrue(exo5ControlFieldsCreated.getError(), exo5ControlFieldsCreated.isOk());
        assertNotNull(exo5ControlFieldsCreated.getData());
        assertFalse(exo5ControlFieldsCreated.getData().isEmpty());

        assertTrue(rpt.getMagusBuilderSlotsDeployed().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> magusBuilderSlotsDeployed = rpt.getMagusBuilderSlotsDeployed().get();
        assertTrue(magusBuilderSlotsDeployed.getError(), magusBuilderSlotsDeployed.isOk());
        assertNotNull(magusBuilderSlotsDeployed.getData());
        assertFalse(magusBuilderSlotsDeployed.getData().isEmpty());

        assertTrue(rpt.getNeutralizerUniquePortalsDestroyed().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> neutralizerUniquePortalsDestroyed = rpt.getNeutralizerUniquePortalsDestroyed().get();
        assertTrue(neutralizerUniquePortalsDestroyed.getError(), neutralizerUniquePortalsDestroyed.isOk());
        assertNotNull(neutralizerUniquePortalsDestroyed.getData());
        assertFalse(neutralizerUniquePortalsDestroyed.getData().isEmpty());

        assertTrue(rpt.getMissionDayPoints().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> missionDayPoints = rpt.getMissionDayPoints().get();
        assertTrue(missionDayPoints.getError(), missionDayPoints.isOk());
        assertNotNull(missionDayPoints.getData());
        assertFalse(missionDayPoints.getData().isEmpty());

        assertTrue(rpt.getMissionsCompleted().isPresent());
        final ReportDetails<List<TimestampedRecord<Integer>>> missionsCompleted = rpt.getMissionsCompleted().get();
        assertTrue(missionsCompleted.getError(), missionsCompleted.isOk());
        assertNotNull(missionsCompleted.getData());
        assertFalse(missionsCompleted.getData().isEmpty());

        assertTrue(rpt.getMissionsCreated().isPresent());
        final ReportDetails<List<Mission>> missionsCreated = rpt.getMissionsCreated().get();
        assertTrue(missionsCreated.getError(), missionsCreated.isOk());
        assertNotNull(missionsCreated.getData());
        assertFalse(missionsCreated.getData().isEmpty());

        assertTrue(rpt.getZendeskTickets().isPresent());
        final ReportDetails<List<ZendeskTicket>> zendeskTickets = rpt.getZendeskTickets().get();
        assertTrue(zendeskTickets.getError(), zendeskTickets.isOk());
        assertNotNull(zendeskTickets.getData());
        assertFalse(zendeskTickets.getData().isEmpty());

        assertTrue(rpt.getStorePurchases().isPresent());
        final ReportDetails<List<StorePurchase>> storePurchases = rpt.getStorePurchases().get();
        assertTrue(storePurchases.getError(), storePurchases.isOk());
        assertNotNull(storePurchases.getData());
        assertFalse(storePurchases.getData().isEmpty());

    }

}
