/*
 * Copyright (C) 2014-2019 SgrAlpha
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

        assertNotNull(rpt.getAgentProfile());
        final ReportDetails<AgentProfile> agentProfile = rpt.getAgentProfile();
        assertTrue(agentProfile.getError(), agentProfile.isOk());
        assertNotNull(agentProfile.getData());

        assertNotNull(rpt.getGameLogs());
        final ReportDetails<List<GameLog>> gameLogs = rpt.getGameLogs();
        assertTrue(gameLogs.getError(), gameLogs.isOk());
        assertNotNull(gameLogs.getData());
        assertFalse(gameLogs.getData().isEmpty());

        assertNotNull(rpt.getCommMentions());
        final ReportDetails<List<CommMention>> commMentions = rpt.getCommMentions();
        assertTrue(commMentions.getError(), commMentions.isOk());
        assertNotNull(commMentions.getData());
        assertFalse(commMentions.getData().isEmpty());

        assertNotNull(rpt.getUsedDevices());
        final ReportDetails<List<UsedDevice>> usedDevices = rpt.getUsedDevices();
        assertTrue(usedDevices.getError(), usedDevices.isOk());
        assertNotNull(usedDevices.getData());
        assertFalse(usedDevices.getData().isEmpty());

        assertNotNull(rpt.getOprProfile());
        final ReportDetails<OprProfile> oprProfile = rpt.getOprProfile();
        assertTrue(oprProfile.getError(), oprProfile.isOk());
        assertNotNull(oprProfile.getData());

        assertNotNull(rpt.getOprAgreements());
        final ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements = rpt.getOprAgreements();
        assertTrue(oprAgreements.getError(), oprAgreements.isOk());
        assertNotNull(oprAgreements.getData());
        assertFalse(oprAgreements.getData().isEmpty());

        assertNotNull(rpt.getOprAssignmentLogs());
        final ReportDetails<List<OprAssignmentLogItem>> oprAssignmentLogs = rpt.getOprAssignmentLogs();
        assertTrue(oprAssignmentLogs.getError(), oprAssignmentLogs.isOk());
        assertNotNull(oprAssignmentLogs.getData());
        assertFalse(oprAssignmentLogs.getData().isEmpty());

        assertNotNull(rpt.getOprSubmissionLogs());
        final ReportDetails<List<OprSubmissionLogItem>> oprSubmissionLogs = rpt.getOprSubmissionLogs();
        assertTrue(oprSubmissionLogs.getError(), oprSubmissionLogs.isOk());
        assertNotNull(oprSubmissionLogs.getData());
        assertFalse(oprSubmissionLogs.getData().isEmpty());

        assertNotNull(rpt.getAllPortalsApproved());
        final ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved = rpt.getAllPortalsApproved();
        assertTrue(allPortalsApproved.getError(), allPortalsApproved.isOk());
        assertNotNull(allPortalsApproved.getData());
        assertFalse(allPortalsApproved.getData().isEmpty());

        assertNotNull(rpt.getSeerPortals());
        final ReportDetails<List<TimestampedRecord<Integer>>> seerPortals = rpt.getSeerPortals();
        assertTrue(seerPortals.getError(), seerPortals.isOk());
        assertNotNull(seerPortals.getData());
        assertFalse(seerPortals.getData().isEmpty());

        assertNotNull(rpt.getPortalsVisited());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited = rpt.getPortalsVisited();
        assertTrue(portalsVisited.getError(), portalsVisited.isOk());
        assertNotNull(portalsVisited.getData());
        assertFalse(portalsVisited.getData().isEmpty());

        assertNotNull(rpt.getXmCollected());
        final ReportDetails<List<TimestampedRecord<Float>>> xmCollected = rpt.getXmCollected();
        assertTrue(xmCollected.getError(), xmCollected.isOk());
        assertNotNull(xmCollected.getData());
        assertFalse(xmCollected.getData().isEmpty());

        assertNotNull(rpt.getKilometersWalked());
        final ReportDetails<List<TimestampedRecord<Float>>> kilometersWalked = rpt.getKilometersWalked();
        assertTrue(kilometersWalked.getError(), kilometersWalked.isOk());
        assertNotNull(kilometersWalked.getData());
        assertFalse(kilometersWalked.getData().isEmpty());

        assertNotNull(rpt.getMindUnitsControlled());
        final ReportDetails<List<TimestampedRecord<Float>>> mindUnitsControlled = rpt.getMindUnitsControlled();
        assertTrue(mindUnitsControlled.getError(), mindUnitsControlled.isOk());
        assertNotNull(mindUnitsControlled.getData());
        assertFalse(mindUnitsControlled.getData().isEmpty());

        assertNotNull(rpt.getMindUnitsControlledActive());
        final ReportDetails<List<TimestampedRecord<Integer>>> mindUnitsControlledActive = rpt.getMindUnitsControlledActive();
        assertTrue(mindUnitsControlledActive.getError(), mindUnitsControlledActive.isOk());
        assertNotNull(mindUnitsControlledActive.getData());
        assertEquals(1, mindUnitsControlledActive.getData().size());

        assertNotNull(rpt.getFieldsCreated());
        final ReportDetails<List<TimestampedRecord<Float>>> fieldsCreated = rpt.getFieldsCreated();
        assertTrue(fieldsCreated.getError(), fieldsCreated.isOk());
        assertNotNull(fieldsCreated.getData());
        assertFalse(fieldsCreated.getData().isEmpty());

        assertNotNull(rpt.getFieldsCreatedActive());
        final ReportDetails<List<TimestampedRecord<Integer>>> fieldsCreatedActive = rpt.getFieldsCreatedActive();
        assertTrue(fieldsCreatedActive.getError(), fieldsCreatedActive.isOk());
        assertNotNull(fieldsCreatedActive.getData());
        assertEquals(1, fieldsCreatedActive.getData().size());

        assertNotNull(rpt.getLinksCreated());
        final ReportDetails<List<TimestampedRecord<Float>>> linksCreated = rpt.getLinksCreated();
        assertTrue(linksCreated.getError(), linksCreated.isOk());
        assertNotNull(linksCreated.getData());
        assertFalse(linksCreated.getData().isEmpty());

        assertNotNull(rpt.getLinkLengthInKm());
        final ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKm = rpt.getLinkLengthInKm();
        assertTrue(linkLengthInKm.getError(), linkLengthInKm.isOk());
        assertNotNull(linkLengthInKm.getData());
        assertFalse(linkLengthInKm.getData().isEmpty());

        assertNotNull(rpt.getLinksCreatedActive());
        final ReportDetails<List<TimestampedRecord<Integer>>> linksCreatedActive = rpt.getLinksCreatedActive();
        assertTrue(linksCreatedActive.getError(), linksCreatedActive.isOk());
        assertNotNull(linksCreatedActive.getData());
        assertEquals(1, linksCreatedActive.getData().size());

        assertNotNull(rpt.getPortalsCaptured());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsCaptured = rpt.getPortalsCaptured();
        assertTrue(portalsCaptured.getError(), portalsCaptured.isOk());
        assertNotNull(portalsCaptured.getData());
        assertFalse(portalsCaptured.getData().isEmpty());

        assertNotNull(rpt.getPortalsOwned());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsOwned = rpt.getPortalsOwned();
        assertTrue(portalsOwned.getError(), portalsOwned.isOk());
        assertNotNull(portalsOwned.getData());
        assertEquals(1, portalsOwned.getData().size());

        assertNotNull(rpt.getResonatorsDeployed());
        final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDeployed = rpt.getResonatorsDeployed();
        assertTrue(resonatorsDeployed.getError(), resonatorsDeployed.isOk());
        assertNotNull(resonatorsDeployed.getData());
        assertFalse(resonatorsDeployed.getData().isEmpty());

        assertNotNull(rpt.getModsDeployed());
        final ReportDetails<List<TimestampedRecord<Float>>> modsDeployed = rpt.getModsDeployed();
        assertTrue(modsDeployed.getError(), modsDeployed.isOk());
        assertNotNull(modsDeployed.getData());
        assertFalse(modsDeployed.getData().isEmpty());

        assertNotNull(rpt.getXmRecharged());
        final ReportDetails<List<TimestampedRecord<Float>>> xmRecharged = rpt.getXmRecharged();
        assertTrue(xmRecharged.getError(), xmRecharged.isOk());
        assertNotNull(xmRecharged.getData());
        assertFalse(xmRecharged.getData().isEmpty());

        assertNotNull(rpt.getResonatorsDestroyed());
        final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDestroyed = rpt.getResonatorsDestroyed();
        assertTrue(resonatorsDestroyed.getError(), resonatorsDestroyed.isOk());
        assertNotNull(resonatorsDestroyed.getData());
        assertFalse(resonatorsDestroyed.getData().isEmpty());

        assertNotNull(rpt.getPortalsNeutralized());
        final ReportDetails<List<TimestampedRecord<Integer>>> portalsNeutralized = rpt.getPortalsNeutralized();
        assertTrue(portalsNeutralized.getError(), portalsNeutralized.isOk());
        assertNotNull(portalsNeutralized.getData());
        assertFalse(portalsNeutralized.getData().isEmpty());

        assertNotNull(rpt.getLinksDestroyed());
        final ReportDetails<List<TimestampedRecord<Float>>> linksDestroyed = rpt.getLinksDestroyed();
        assertTrue(linksDestroyed.getError(), linksDestroyed.isOk());
        assertNotNull(linksDestroyed.getData());
        assertFalse(linksDestroyed.getData().isEmpty());

        assertNotNull(rpt.getFieldsDestroyed());
        final ReportDetails<List<TimestampedRecord<Float>>> fieldsDestroyed = rpt.getFieldsDestroyed();
        assertTrue(fieldsDestroyed.getError(), fieldsDestroyed.isOk());
        assertNotNull(fieldsDestroyed.getData());
        assertFalse(fieldsDestroyed.getData().isEmpty());

        assertNotNull(rpt.getPortalHeldDays());
        final ReportDetails<List<TimestampedRecord<Double>>> portalHeldDays = rpt.getPortalHeldDays();
        assertTrue(portalHeldDays.getError(), portalHeldDays.isOk());
        assertNotNull(portalHeldDays.getData());
        assertFalse(portalHeldDays.getData().isEmpty());

        assertNotNull(rpt.getLinkHeldDays());
        final ReportDetails<List<TimestampedRecord<Double>>> linkHeldDays = rpt.getLinkHeldDays();
        assertTrue(linkHeldDays.getError(), linkHeldDays.isOk());
        assertNotNull(linkHeldDays.getData());
        assertFalse(linkHeldDays.getData().isEmpty());

        assertNotNull(rpt.getLinkLengthInKmTimesDaysHeld());
        final ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKmTimesDaysHeld = rpt.getLinkLengthInKmTimesDaysHeld();
        assertTrue(linkLengthInKmTimesDaysHeld.getError(), linkLengthInKmTimesDaysHeld.isOk());
        assertNotNull(linkLengthInKmTimesDaysHeld.getData());
        assertFalse(linkLengthInKmTimesDaysHeld.getData().isEmpty());

        assertNotNull(rpt.getFieldHeldDays());
        final ReportDetails<List<TimestampedRecord<Double>>> fieldHeldDays = rpt.getFieldHeldDays();
        assertTrue(fieldHeldDays.getError(), fieldHeldDays.isOk());
        assertNotNull(fieldHeldDays.getData());
        assertFalse(fieldHeldDays.getData().isEmpty());

        assertNotNull(rpt.getMindUnitsTimesDaysHeld());
        final ReportDetails<List<TimestampedRecord<Double>>> mindUnitsTimesDaysHeld = rpt.getMindUnitsTimesDaysHeld();
        assertTrue(mindUnitsTimesDaysHeld.getError(), mindUnitsTimesDaysHeld.isOk());
        assertNotNull(mindUnitsTimesDaysHeld.getData());
        assertFalse(mindUnitsTimesDaysHeld.getData().isEmpty());

        assertNotNull(rpt.getHacks());
        final ReportDetails<List<TimestampedRecord<Float>>> hacks = rpt.getHacks();
        assertTrue(hacks.getError(), hacks.isOk());
        assertNotNull(hacks.getData());
        assertFalse(hacks.getData().isEmpty());

        assertNotNull(rpt.getGlyphHackPoints());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackPoints = rpt.getGlyphHackPoints();
        assertTrue(glyphHackPoints.getError(), glyphHackPoints.isOk());
        assertNotNull(glyphHackPoints.getData());
        assertFalse(glyphHackPoints.getData().isEmpty());

        assertNotNull(rpt.getGlyphHackOnePerfect());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackOnePerfect = rpt.getGlyphHackOnePerfect();
        assertTrue(glyphHackOnePerfect.getError(), glyphHackOnePerfect.isOk());
        assertNotNull(glyphHackOnePerfect.getData());
        assertFalse(glyphHackOnePerfect.getData().isEmpty());

        assertNotNull(rpt.getGlyphHackThreePerfect());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackThreePerfect = rpt.getGlyphHackThreePerfect();
        assertTrue(glyphHackThreePerfect.getError(), glyphHackThreePerfect.isOk());
        assertNotNull(glyphHackThreePerfect.getData());
        assertFalse(glyphHackThreePerfect.getData().isEmpty());

        assertNotNull(rpt.getGlyphHackFourPerfect());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackFourPerfect = rpt.getGlyphHackFourPerfect();
        assertTrue(glyphHackFourPerfect.getError(), glyphHackFourPerfect.isOk());
        assertNotNull(glyphHackFourPerfect.getData());
        assertFalse(glyphHackFourPerfect.getData().isEmpty());

        assertNotNull(rpt.getGlyphHackFivePerfect());
        final ReportDetails<List<TimestampedRecord<Float>>> glyphHackFivePerfect = rpt.getGlyphHackFivePerfect();
        assertTrue(glyphHackFivePerfect.getError(), glyphHackFivePerfect.isOk());
        assertNotNull(glyphHackFivePerfect.getData());
        assertFalse(glyphHackFivePerfect.getData().isEmpty());

        assertNotNull(rpt.getAgentsRecruited());
        final ReportDetails<List<TimestampedRecord<Integer>>> agentsRecruited = rpt.getAgentsRecruited();
        assertTrue(agentsRecruited.getError(), agentsRecruited.isOk());
        assertNotNull(agentsRecruited.getData());
        assertFalse(agentsRecruited.getData().isEmpty());

        assertNotNull(rpt.getExo5ControlFieldsCreated());
        final ReportDetails<List<TimestampedRecord<Float>>> exo5ControlFieldsCreated = rpt.getExo5ControlFieldsCreated();
        assertTrue(exo5ControlFieldsCreated.getError(), exo5ControlFieldsCreated.isOk());
        assertNotNull(exo5ControlFieldsCreated.getData());
        assertFalse(exo5ControlFieldsCreated.getData().isEmpty());

        assertNotNull(rpt.getMagusBuilderSlotsDeployed());
        final ReportDetails<List<TimestampedRecord<Integer>>> magusBuilderSlotsDeployed = rpt.getMagusBuilderSlotsDeployed();
        assertTrue(magusBuilderSlotsDeployed.getError(), magusBuilderSlotsDeployed.isOk());
        assertNotNull(magusBuilderSlotsDeployed.getData());
        assertFalse(magusBuilderSlotsDeployed.getData().isEmpty());

        assertNotNull(rpt.getNeutralizerUniquePortalsDestroyed());
        final ReportDetails<List<TimestampedRecord<Integer>>> neutralizerUniquePortalsDestroyed = rpt.getNeutralizerUniquePortalsDestroyed();
        assertTrue(neutralizerUniquePortalsDestroyed.getError(), neutralizerUniquePortalsDestroyed.isOk());
        assertNotNull(neutralizerUniquePortalsDestroyed.getData());
        assertFalse(neutralizerUniquePortalsDestroyed.getData().isEmpty());

        assertNotNull(rpt.getMissionDayPoints());
        final ReportDetails<List<TimestampedRecord<Integer>>> missionDayPoints = rpt.getMissionDayPoints();
        assertTrue(missionDayPoints.getError(), missionDayPoints.isOk());
        assertNotNull(missionDayPoints.getData());
        assertFalse(missionDayPoints.getData().isEmpty());

        assertNotNull(rpt.getMissionsCompleted());
        final ReportDetails<List<TimestampedRecord<Integer>>> missionsCompleted = rpt.getMissionsCompleted();
        assertTrue(missionsCompleted.getError(), missionsCompleted.isOk());
        assertNotNull(missionsCompleted.getData());
        assertFalse(missionsCompleted.getData().isEmpty());

        assertNotNull(rpt.getMissionsCreated());
        final ReportDetails<List<Mission>> missionsCreated = rpt.getMissionsCreated();
        assertTrue(missionsCreated.getError(), missionsCreated.isOk());
        assertNotNull(missionsCreated.getData());
        assertFalse(missionsCreated.getData().isEmpty());

        assertNotNull(rpt.getZendeskTickets());
        final ReportDetails<List<ZendeskTicket>> zendeskTickets = rpt.getZendeskTickets();
        assertTrue(zendeskTickets.getError(), zendeskTickets.isOk());
        assertNotNull(zendeskTickets.getData());
        assertFalse(zendeskTickets.getData().isEmpty());

        assertNotNull(rpt.getStorePurchases());
        final ReportDetails<List<StorePurchase>> storePurchases = rpt.getStorePurchases();
        assertTrue(storePurchases.getError(), storePurchases.isOk());
        assertNotNull(storePurchases.getData());
        assertFalse(storePurchases.getData().isEmpty());

    }

}
