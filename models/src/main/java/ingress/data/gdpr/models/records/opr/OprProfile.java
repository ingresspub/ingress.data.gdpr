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

package ingress.data.gdpr.models.records.opr;

import io.sgr.geometry.Coordinate;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class OprProfile {

    private final String emailAddress;
    private final ZonedDateTime bonusLastChangedTime;
    private final Coordinate bonusLocation;
    private final ZonedDateTime accountCreationTime;
    private final int totalAnalyzed;
    private final int portalCreated;
    private final int portalRejected;
    private final int hometownChangedTimes;
    private final ZonedDateTime hometownLastChangedTime;
    private final Coordinate hometownLocation;
    private final Coordinate lastActivityLocation;
    private final String language;
    private final ZonedDateTime lastLoginTime;
    private final String performance;
    private final String quizStatus;
    private final ZonedDateTime quizTimeTaken;
    private final ZonedDateTime trainingCompletionTime;

    public OprProfile(
            final String emailAddress,
            final ZonedDateTime bonusLastChangedTime, final Coordinate bonusLocation,
            final ZonedDateTime accountCreationTime,
            final int totalAnalyzed, final int portalCreated, final int portalRejected,
            final int hometownChangedTimes, final ZonedDateTime hometownLastChangedTime, final Coordinate hometownLocation,
            final Coordinate lastActivityLocation,
            final String language,
            final ZonedDateTime lastLoginTime,
            final String performance,
            final String quizStatus, final ZonedDateTime quizTimeTaken, final ZonedDateTime trainingCompletionTime) {
        this.emailAddress = emailAddress;
        this.bonusLastChangedTime = bonusLastChangedTime;
        this.bonusLocation = bonusLocation;
        this.accountCreationTime = accountCreationTime;
        this.totalAnalyzed = totalAnalyzed;
        this.portalCreated = portalCreated;
        this.portalRejected = portalRejected;
        this.hometownChangedTimes = hometownChangedTimes;
        this.hometownLastChangedTime = hometownLastChangedTime;
        this.hometownLocation = hometownLocation;
        this.lastActivityLocation = lastActivityLocation;
        this.language = language;
        this.lastLoginTime = lastLoginTime;
        this.performance = performance;
        this.quizStatus = quizStatus;
        this.quizTimeTaken = quizTimeTaken;
        this.trainingCompletionTime = trainingCompletionTime;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public ZonedDateTime getBonusLastChangedTime() {
        return bonusLastChangedTime;
    }

    public Coordinate getBonusLocation() {
        return bonusLocation;
    }

    public ZonedDateTime getAccountCreationTime() {
        return accountCreationTime;
    }

    public int getTotalAnalyzed() {
        return totalAnalyzed;
    }

    public int getPortalCreated() {
        return portalCreated;
    }

    public int getPortalRejected() {
        return portalRejected;
    }

    public int getHometownChangedTimes() {
        return hometownChangedTimes;
    }

    public Optional<ZonedDateTime> getHometownLastChangedTime() {
        return Optional.ofNullable(hometownLastChangedTime);
    }

    public Coordinate getHometownLocation() {
        return hometownLocation;
    }

    public Coordinate getLastActivityLocation() {
        return lastActivityLocation;
    }

    public String getLanguage() {
        return language;
    }

    public ZonedDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public String getPerformance() {
        return performance;
    }

    public String getQuizStatus() {
        return quizStatus;
    }

    public Optional<ZonedDateTime> getQuizTimeTaken() {
        return Optional.ofNullable(quizTimeTaken);
    }

    public ZonedDateTime getTrainingCompletionTime() {
        return trainingCompletionTime;
    }
}
