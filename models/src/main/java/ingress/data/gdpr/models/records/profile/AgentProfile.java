/*
 * Copyright (C) 2014-2020 SgrAlpha
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

package ingress.data.gdpr.models.records.profile;

import io.sgr.geometry.Coordinate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class AgentProfile {

    private final String email;
    private final ZonedDateTime creationTime;
    private final SmsVerification smsVerification;
    private final ZonedDateTime tosAcceptedTime;
    private final String invites;

    private final String agentName;
    private final String faction;
    private final int agentLevel;
    private final int ap;
    private final int xm;
    private final Integer extraXm;
    private final boolean displayStatsToOthers;
    private final Coordinate lastLocation;
    private final ZonedDateTime lastLocationTime;
    private final Map<String, Integer> highestMediaIdByCategory;

    private final boolean enabledEmailNotification;
    private final boolean enabledEmailPromos;
    private final PushNotificationFor pushNotificationFor;

    private final boolean hasCapturedPortal;
    private final boolean hasCreatedLink;
    private final boolean hasCreatedField;
    private final List<String> blockedAgents;

    private final List<TutorialState> tutorialState;

    private final List<Badge> badges;

    private final Map<String, Integer> inventory;

    public AgentProfile(
            final String email,
            final ZonedDateTime creationTime,
            final SmsVerification smsVerification,
            final ZonedDateTime tosAcceptedTime,
            final String invites,
            final String agentName, final String faction, final int agentLevel,
            final int ap, final int xm, final Integer extraXm,
            final boolean displayStatsToOthers,
            final Coordinate lastLocation, final ZonedDateTime lastLocationTime,
            final Map<String, Integer> highestMediaIdByCategory,
            final boolean enabledEmailNotification, final boolean enabledEmailPromos, final PushNotificationFor pushNotificationFor,
            final boolean hasCapturedPortal, final boolean hasCreatedLink, final boolean hasCreatedField,
            final List<String> blockedAgents,
            final List<TutorialState> tutorialState,
            final List<Badge> badges,
            final Map<String, Integer> inventory) {
        this.email = email;
        this.creationTime = creationTime;
        this.smsVerification = smsVerification;
        this.tosAcceptedTime = tosAcceptedTime;
        this.invites = invites;
        this.agentName = agentName;
        this.faction = faction;
        this.agentLevel = agentLevel;
        this.ap = ap;
        this.xm = xm;
        this.extraXm = extraXm;
        this.displayStatsToOthers = displayStatsToOthers;
        this.lastLocation = lastLocation;
        this.lastLocationTime = lastLocationTime;
        this.highestMediaIdByCategory = highestMediaIdByCategory;
        this.enabledEmailNotification = enabledEmailNotification;
        this.enabledEmailPromos = enabledEmailPromos;
        this.pushNotificationFor = pushNotificationFor;
        this.hasCapturedPortal = hasCapturedPortal;
        this.hasCreatedLink = hasCreatedLink;
        this.hasCreatedField = hasCreatedField;
        this.blockedAgents = blockedAgents;
        this.tutorialState = tutorialState;
        this.badges = badges;
        this.inventory = inventory;
    }

    public String getEmail() {
        return email;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public SmsVerification getSmsVerification() {
        return smsVerification;
    }

    public ZonedDateTime getTosAcceptedTime() {
        return tosAcceptedTime;
    }

    public String getInvites() {
        return invites;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getFaction() {
        return faction;
    }

    public int getAgentLevel() {
        return agentLevel;
    }

    public int getAp() {
        return ap;
    }

    public int getXm() {
        return xm;
    }

    public Optional<Integer> getExtraXm() {
        return Optional.ofNullable(extraXm);
    }

    public boolean isDisplayStatsToOthers() {
        return displayStatsToOthers;
    }

    public Coordinate getLastLocation() {
        return lastLocation;
    }

    public ZonedDateTime getLastLocationTime() {
        return lastLocationTime;
    }

    public Map<String, Integer> getHighestMediaIdByCategory() {
        return highestMediaIdByCategory;
    }

    public boolean enabledEmailNotification() {
        return enabledEmailNotification;
    }

    public boolean enabledEmailPromos() {
        return enabledEmailPromos;
    }

    public PushNotificationFor getPushNotificationFor() {
        return pushNotificationFor;
    }

    public boolean hasCapturedPortal() {
        return hasCapturedPortal;
    }

    public boolean hasCreatedLink() {
        return hasCreatedLink;
    }

    public boolean hasCreatedField() {
        return hasCreatedField;
    }

    public Optional<List<String>> getBlockedAgents() {
        return Optional.ofNullable(blockedAgents);
    }

    public List<TutorialState> getTutorialState() {
        return tutorialState;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

}
