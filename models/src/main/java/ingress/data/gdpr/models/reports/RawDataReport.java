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

package ingress.data.gdpr.models.reports;

import ingress.data.gdpr.models.records.CommMention;
import ingress.data.gdpr.models.records.GameLog;
import ingress.data.gdpr.models.records.StorePurchase;
import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.records.ZendeskTicket;
import ingress.data.gdpr.models.records.mission.Mission;
import ingress.data.gdpr.models.records.opr.OprAssignmentLogItem;
import ingress.data.gdpr.models.records.opr.OprProfile;
import ingress.data.gdpr.models.records.opr.OprSkippedLogItem;
import ingress.data.gdpr.models.records.opr.OprSubmissionLogItem;
import ingress.data.gdpr.models.records.profile.AgentProfile;
import ingress.data.gdpr.models.utils.JsonUtil;

import java.time.Clock;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class RawDataReport {

    private final long generatedTimeInMs;

    private ReportDetails<AgentProfile> agentProfile;

    private ReportDetails<List<GameLog>> gameLogs;
    private ReportDetails<List<CommMention>> commMentions;

    private ReportDetails<List<UsedDevice>> usedDevices;

    private ReportDetails<OprProfile> oprProfile;
    private ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements;
    private ReportDetails<List<OprAssignmentLogItem>> oprAssignmentLogs;
    private ReportDetails<List<OprSkippedLogItem>> oprSkippedLogs;
    private ReportDetails<List<OprSubmissionLogItem>> oprSubmissionLogs;
    private ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved;
    private ReportDetails<List<TimestampedRecord<Integer>>> seerPortals;
    private ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited;
    private ReportDetails<List<TimestampedRecord<Float>>> xmCollected;

    private ReportDetails<List<TimestampedRecord<Float>>> kilometersWalked;

    private ReportDetails<List<TimestampedRecord<Float>>> mindUnitsControlled;
    private ReportDetails<List<TimestampedRecord<Integer>>> mindUnitsControlledActive;
    private ReportDetails<List<TimestampedRecord<Float>>> fieldsCreated;
    private ReportDetails<List<TimestampedRecord<Integer>>> fieldsCreatedActive;
    private ReportDetails<List<TimestampedRecord<Float>>> linksCreated;
    private ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKm;
    private ReportDetails<List<TimestampedRecord<Integer>>> linksCreatedActive;
    private ReportDetails<List<TimestampedRecord<Integer>>> portalsCaptured;
    private ReportDetails<List<TimestampedRecord<Integer>>> portalsOwned;
    private ReportDetails<List<TimestampedRecord<Float>>> resonatorsDeployed;
    private ReportDetails<List<TimestampedRecord<Float>>> modsDeployed;
    private ReportDetails<List<TimestampedRecord<Float>>> xmRecharged;
    private ReportDetails<List<TimestampedRecord<Float>>> resonatorsDestroyed;
    private ReportDetails<List<TimestampedRecord<Integer>>> portalsNeutralized;
    private ReportDetails<List<TimestampedRecord<Float>>> linksDestroyed;
    private ReportDetails<List<TimestampedRecord<Float>>> fieldsDestroyed;

    private ReportDetails<List<TimestampedRecord<Double>>> mindUnitsTimesDaysHeld;
    private ReportDetails<List<TimestampedRecord<Double>>> fieldHeldDays;
    private ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKmTimesDaysHeld;
    private ReportDetails<List<TimestampedRecord<Double>>> linkHeldDays;
    private ReportDetails<List<TimestampedRecord<Double>>> portalHeldDays;

    private ReportDetails<List<TimestampedRecord<Float>>> hacks;
    private ReportDetails<List<TimestampedRecord<Float>>> glyphHackPoints;
    private ReportDetails<List<TimestampedRecord<Float>>> glyphHackOnePerfect;
    private ReportDetails<List<TimestampedRecord<Float>>> glyphHackThreePerfect;
    private ReportDetails<List<TimestampedRecord<Float>>> glyphHackFourPerfect;
    private ReportDetails<List<TimestampedRecord<Float>>> glyphHackFivePerfect;

    private ReportDetails<List<TimestampedRecord<Integer>>> agentsRecruited;

    private ReportDetails<List<TimestampedRecord<Float>>> exo5ControlFieldsCreated;
    private ReportDetails<List<TimestampedRecord<Integer>>> magusBuilderSlotsDeployed;
    private ReportDetails<List<TimestampedRecord<Integer>>> neutralizerUniquePortalsDestroyed;
    private ReportDetails<List<TimestampedRecord<Integer>>> missionDayPoints;

    private ReportDetails<List<TimestampedRecord<Integer>>> missionsCompleted;
    private ReportDetails<List<Mission>> missionsCreated;

    private ReportDetails<List<ZendeskTicket>> zendeskTickets;
    private ReportDetails<List<StorePurchase>> storePurchases;

    public RawDataReport() {
        generatedTimeInMs = Clock.systemUTC().millis();
    }

    public long getGeneratedTimeInMs() {
        return generatedTimeInMs;
    }

    public ReportDetails<AgentProfile> getAgentProfile() {
        return agentProfile;
    }

    public RawDataReport setAgentProfile(final ReportDetails<AgentProfile> agentProfile) {
        this.agentProfile = agentProfile;
        return this;
    }

    public ReportDetails<List<GameLog>> getGameLogs() {
        return gameLogs;
    }

    public RawDataReport setGameLogs(final ReportDetails<List<GameLog>> gameLogs) {
        this.gameLogs = gameLogs;
        return this;
    }

    public ReportDetails<List<CommMention>> getCommMentions() {
        return commMentions;
    }

    public RawDataReport setCommMentions(final ReportDetails<List<CommMention>> commMentions) {
        this.commMentions = commMentions;
        return this;
    }

    public ReportDetails<List<UsedDevice>> getUsedDevices() {
        return usedDevices;
    }

    public RawDataReport setUsedDevices(final ReportDetails<List<UsedDevice>> usedDevices) {
        this.usedDevices = usedDevices;
        return this;
    }

    public ReportDetails<OprProfile> getOprProfile() {
        return oprProfile;
    }

    public RawDataReport setOprProfile(final ReportDetails<OprProfile> oprProfile) {
        this.oprProfile = oprProfile;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getOprAgreements() {
        return oprAgreements;
    }

    public RawDataReport setOprAgreements(final ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements) {
        this.oprAgreements = oprAgreements;
        return this;
    }

    public ReportDetails<List<OprAssignmentLogItem>> getOprAssignmentLogs() {
        return oprAssignmentLogs;
    }

    public RawDataReport setOprAssignmentLogs(final ReportDetails<List<OprAssignmentLogItem>> oprAssignmentLogs) {
        this.oprAssignmentLogs = oprAssignmentLogs;
        return this;
    }

    public ReportDetails<List<OprSkippedLogItem>> getOprSkippedLogs() {
        return oprSkippedLogs;
    }

    public RawDataReport setOprSkippedLogs(final ReportDetails<List<OprSkippedLogItem>> oprSkippedLogs) {
        this.oprSkippedLogs = oprSkippedLogs;
        return this;
    }

    public ReportDetails<List<OprSubmissionLogItem>> getOprSubmissionLogs() {
        return oprSubmissionLogs;
    }

    public RawDataReport setOprSubmissionLogs(final ReportDetails<List<OprSubmissionLogItem>> oprSubmissionLogs) {
        this.oprSubmissionLogs = oprSubmissionLogs;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getAllPortalsApproved() {
        return allPortalsApproved;
    }

    public RawDataReport setAllPortalsApproved(final ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved) {
        this.allPortalsApproved = allPortalsApproved;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getSeerPortals() {
        return seerPortals;
    }

    public RawDataReport setSeerPortals(final ReportDetails<List<TimestampedRecord<Integer>>> seerPortals) {
        this.seerPortals = seerPortals;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getPortalsVisited() {
        return portalsVisited;
    }

    public RawDataReport setPortalsVisited(final ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited) {
        this.portalsVisited = portalsVisited;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getXmCollected() {
        return xmCollected;
    }

    public RawDataReport setXmCollected(final ReportDetails<List<TimestampedRecord<Float>>> xmCollected) {
        this.xmCollected = xmCollected;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getKilometersWalked() {
        return kilometersWalked;
    }

    public RawDataReport setKilometersWalked(final ReportDetails<List<TimestampedRecord<Float>>> kilometersWalked) {
        this.kilometersWalked = kilometersWalked;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getMindUnitsControlled() {
        return mindUnitsControlled;
    }

    public RawDataReport setMindUnitsControlled(final ReportDetails<List<TimestampedRecord<Float>>> mindUnitsControlled) {
        this.mindUnitsControlled = mindUnitsControlled;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getMindUnitsControlledActive() {
        return mindUnitsControlledActive;
    }

    public RawDataReport setMindUnitsControlledActive(final ReportDetails<List<TimestampedRecord<Integer>>> mindUnitsControlledActive) {
        this.mindUnitsControlledActive = mindUnitsControlledActive;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getFieldsCreated() {
        return fieldsCreated;
    }

    public RawDataReport setFieldsCreated(final ReportDetails<List<TimestampedRecord<Float>>> fieldsCreated) {
        this.fieldsCreated = fieldsCreated;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getFieldsCreatedActive() {
        return fieldsCreatedActive;
    }

    public RawDataReport setFieldsCreatedActive(final ReportDetails<List<TimestampedRecord<Integer>>> fieldsCreatedActive) {
        this.fieldsCreatedActive = fieldsCreatedActive;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getLinksCreated() {
        return linksCreated;
    }

    public RawDataReport setLinksCreated(final ReportDetails<List<TimestampedRecord<Float>>> linksCreated) {
        this.linksCreated = linksCreated;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Double>>> getLinkLengthInKm() {
        return linkLengthInKm;
    }

    public RawDataReport setLinkLengthInKm(final ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKm) {
        this.linkLengthInKm = linkLengthInKm;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getLinksCreatedActive() {
        return linksCreatedActive;
    }

    public RawDataReport setLinksCreatedActive(final ReportDetails<List<TimestampedRecord<Integer>>> linksCreatedActive) {
        this.linksCreatedActive = linksCreatedActive;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getPortalsCaptured() {
        return portalsCaptured;
    }

    public RawDataReport setPortalsCaptured(final ReportDetails<List<TimestampedRecord<Integer>>> portalCaptured) {
        this.portalsCaptured = portalCaptured;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getPortalsOwned() {
        return portalsOwned;
    }

    public RawDataReport setPortalsOwned(final ReportDetails<List<TimestampedRecord<Integer>>> portalsOwned) {
        this.portalsOwned = portalsOwned;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getResonatorsDeployed() {
        return resonatorsDeployed;
    }

    public RawDataReport setResonatorsDeployed(final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDeployed) {
        this.resonatorsDeployed = resonatorsDeployed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getModsDeployed() {
        return modsDeployed;
    }

    public RawDataReport setModsDeployed(final ReportDetails<List<TimestampedRecord<Float>>> modsDeployed) {
        this.modsDeployed = modsDeployed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getXmRecharged() {
        return xmRecharged;
    }

    public RawDataReport setXmRecharged(final ReportDetails<List<TimestampedRecord<Float>>> xmRecharged) {
        this.xmRecharged = xmRecharged;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getResonatorsDestroyed() {
        return resonatorsDestroyed;
    }

    public RawDataReport setResonatorsDestroyed(final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDestroyed) {
        this.resonatorsDestroyed = resonatorsDestroyed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getPortalsNeutralized() {
        return portalsNeutralized;
    }

    public RawDataReport setPortalsNeutralized(final ReportDetails<List<TimestampedRecord<Integer>>> portalsNeutralized) {
        this.portalsNeutralized = portalsNeutralized;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getLinksDestroyed() {
        return linksDestroyed;
    }

    public RawDataReport setLinksDestroyed(final ReportDetails<List<TimestampedRecord<Float>>> linksDestroyed) {
        this.linksDestroyed = linksDestroyed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getFieldsDestroyed() {
        return fieldsDestroyed;
    }

    public RawDataReport setFieldsDestroyed(final ReportDetails<List<TimestampedRecord<Float>>> fieldsDestroyed) {
        this.fieldsDestroyed = fieldsDestroyed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Double>>> getMindUnitsTimesDaysHeld() {
        return mindUnitsTimesDaysHeld;
    }

    public RawDataReport setMindUnitsTimesDaysHeld(final ReportDetails<List<TimestampedRecord<Double>>> mindUnitsTimesDaysHeld) {
        this.mindUnitsTimesDaysHeld = mindUnitsTimesDaysHeld;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Double>>> getFieldHeldDays() {
        return fieldHeldDays;
    }

    public RawDataReport setFieldHeldDays(final ReportDetails<List<TimestampedRecord<Double>>> fieldHeldDays) {
        this.fieldHeldDays = fieldHeldDays;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Double>>> getLinkLengthInKmTimesDaysHeld() {
        return linkLengthInKmTimesDaysHeld;
    }

    public RawDataReport setLinkLengthInKmTimesDaysHeld(final ReportDetails<List<TimestampedRecord<Double>>> linkLengthInKmTimesDaysHeld) {
        this.linkLengthInKmTimesDaysHeld = linkLengthInKmTimesDaysHeld;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Double>>> getLinkHeldDays() {
        return linkHeldDays;
    }

    public RawDataReport setLinkHeldDays(final ReportDetails<List<TimestampedRecord<Double>>> linkHeldDays) {
        this.linkHeldDays = linkHeldDays;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Double>>> getPortalHeldDays() {
        return portalHeldDays;
    }

    public RawDataReport setPortalHeldDays(final ReportDetails<List<TimestampedRecord<Double>>> portalHeldDays) {
        this.portalHeldDays = portalHeldDays;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getHacks() {
        return hacks;
    }

    public RawDataReport setHacks(final ReportDetails<List<TimestampedRecord<Float>>> hacks) {
        this.hacks = hacks;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getGlyphHackPoints() {
        return glyphHackPoints;
    }

    public RawDataReport setGlyphHackPoints(final ReportDetails<List<TimestampedRecord<Float>>> glyphHackPoints) {
        this.glyphHackPoints = glyphHackPoints;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getGlyphHackOnePerfect() {
        return glyphHackOnePerfect;
    }

    public RawDataReport setGlyphHackOnePerfect(final ReportDetails<List<TimestampedRecord<Float>>> glyphHackOnePerfect) {
        this.glyphHackOnePerfect = glyphHackOnePerfect;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getGlyphHackThreePerfect() {
        return glyphHackThreePerfect;
    }

    public RawDataReport setGlyphHackThreePerfect(final ReportDetails<List<TimestampedRecord<Float>>> glyphHackThreePerfect) {
        this.glyphHackThreePerfect = glyphHackThreePerfect;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getGlyphHackFourPerfect() {
        return glyphHackFourPerfect;
    }

    public RawDataReport setGlyphHackFourPerfect(final ReportDetails<List<TimestampedRecord<Float>>> glyphHackFourPerfect) {
        this.glyphHackFourPerfect = glyphHackFourPerfect;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getGlyphHackFivePerfect() {
        return glyphHackFivePerfect;
    }

    public RawDataReport setGlyphHackFivePerfect(final ReportDetails<List<TimestampedRecord<Float>>> glyphHackFivePerfect) {
        this.glyphHackFivePerfect = glyphHackFivePerfect;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getAgentsRecruited() {
        return agentsRecruited;
    }

    public RawDataReport setAgentsRecruited(final ReportDetails<List<TimestampedRecord<Integer>>> agentsRecruited) {
        this.agentsRecruited = agentsRecruited;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Float>>> getExo5ControlFieldsCreated() {
        return exo5ControlFieldsCreated;
    }

    public RawDataReport setExo5ControlFieldsCreated(final ReportDetails<List<TimestampedRecord<Float>>> exo5ControlFieldsCreated) {
        this.exo5ControlFieldsCreated = exo5ControlFieldsCreated;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getMagusBuilderSlotsDeployed() {
        return magusBuilderSlotsDeployed;
    }

    public RawDataReport setMagusBuilderSlotsDeployed(final ReportDetails<List<TimestampedRecord<Integer>>> magusBuilderSlotsDeployed) {
        this.magusBuilderSlotsDeployed = magusBuilderSlotsDeployed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getNeutralizerUniquePortalsDestroyed() {
        return neutralizerUniquePortalsDestroyed;
    }

    public RawDataReport setNeutralizerUniquePortalsDestroyed(final ReportDetails<List<TimestampedRecord<Integer>>> neutralizerUniquePortalsDestroyed) {
        this.neutralizerUniquePortalsDestroyed = neutralizerUniquePortalsDestroyed;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getMissionDayPoints() {
        return missionDayPoints;
    }

    public RawDataReport setMissionDayPoints(final ReportDetails<List<TimestampedRecord<Integer>>> missionDayPoints) {
        this.missionDayPoints = missionDayPoints;
        return this;
    }

    public ReportDetails<List<TimestampedRecord<Integer>>> getMissionsCompleted() {
        return missionsCompleted;
    }

    public RawDataReport setMissionsCompleted(final ReportDetails<List<TimestampedRecord<Integer>>> missionsCompleted) {
        this.missionsCompleted = missionsCompleted;
        return this;
    }

    public ReportDetails<List<Mission>> getMissionsCreated() {
        return missionsCreated;
    }

    public RawDataReport setMissionsCreated(final ReportDetails<List<Mission>> missionsCreated) {
        this.missionsCreated = missionsCreated;
        return this;
    }

    public ReportDetails<List<ZendeskTicket>> getZendeskTickets() {
        return zendeskTickets;
    }

    public RawDataReport setZendeskTickets(final ReportDetails<List<ZendeskTicket>> zendeskTickets) {
        this.zendeskTickets = zendeskTickets;
        return this;
    }

    public ReportDetails<List<StorePurchase>> getStorePurchases() {
        return storePurchases;
    }

    public RawDataReport setStorePurchases(final ReportDetails<List<StorePurchase>> storePurchases) {
        this.storePurchases = storePurchases;
        return this;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }

}
