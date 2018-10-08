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

package ingress.data.gdpr.models.records.profile;

/**
 * @author SgrAlpha
 */
public class PushNotificationFor {

    private final boolean commMention;
    private final boolean portalAttack;
    private final boolean factionActivity;
    private final boolean newStory;
    private final boolean events;

    public PushNotificationFor(final boolean commMention, final boolean portalAttack, final boolean factionActivity, final boolean newStory, final boolean events) {
        this.commMention = commMention;
        this.portalAttack = portalAttack;
        this.factionActivity = factionActivity;
        this.newStory = newStory;
        this.events = events;
    }

    public boolean enabledCommMention() {
        return commMention;
    }

    public boolean enabledPortalAttack() {
        return portalAttack;
    }

    public boolean enabledFactionActivity() {
        return factionActivity;
    }

    public boolean enabledNewStory() {
        return newStory;
    }

    public boolean enabledEvents() {
        return events;
    }
}
