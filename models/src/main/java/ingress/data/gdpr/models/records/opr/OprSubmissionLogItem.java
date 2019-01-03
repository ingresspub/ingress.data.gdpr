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

package ingress.data.gdpr.models.records.opr;

import io.sgr.geometry.Coordinate;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class OprSubmissionLogItem {

    private final String candidateId;
    private final ZonedDateTime assignedTime;
    private final String comment;
    private final Integer ratingForCultural;
    private final Integer ratingForText;
    private final boolean duplicate;
    private final String duplicateTo;
    private final Integer ratingForLocation;
    private final Coordinate suggestedLocation;
    private final Integer ratingForQuality;
    private final Integer ratingForSafety;
    private final boolean oneStar;
    private final ZonedDateTime submissionTime;
    private final Integer ratingForUniqueness;
    private final String whatIsIt;

    public OprSubmissionLogItem(
            final String candidateId, final ZonedDateTime assignedTime, final String comment,
            final Integer ratingForCultural, final Integer ratingForText,
            final boolean duplicate, final String duplicateTo,
            final Integer ratingForLocation, final Coordinate suggestedLocation, final Integer ratingForQuality, final Integer ratingForSafety,
            final boolean oneStar, final ZonedDateTime submissionTime, final Integer ratingForUniqueness, final String whatIsIt) {
        this.candidateId = candidateId;
        this.assignedTime = assignedTime;
        this.comment = comment;
        this.ratingForCultural = ratingForCultural;
        this.ratingForText = ratingForText;
        this.duplicate = duplicate;
        this.duplicateTo = duplicateTo;
        this.ratingForLocation = ratingForLocation;
        this.suggestedLocation = suggestedLocation;
        this.ratingForQuality = ratingForQuality;
        this.ratingForSafety = ratingForSafety;
        this.oneStar = oneStar;
        this.submissionTime = submissionTime;
        this.ratingForUniqueness = ratingForUniqueness;
        this.whatIsIt = whatIsIt;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public ZonedDateTime getAssignedTime() {
        return assignedTime;
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<Integer> getRatingForCultural() {
        return Optional.ofNullable(ratingForCultural);
    }

    public Optional<Integer> getRatingForText() {
        return Optional.ofNullable(ratingForText);
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public Optional<String> getDuplicateTo() {
        return Optional.ofNullable(duplicateTo);
    }

    public Optional<Integer> getRatingForLocation() {
        return Optional.ofNullable(ratingForLocation);
    }

    public Optional<Coordinate> getSuggestedLocation() {
        return Optional.ofNullable(suggestedLocation);
    }

    public Optional<Integer> getRatingForQuality() {
        return Optional.ofNullable(ratingForQuality);
    }

    public Optional<Integer> getRatingForSafety() {
        return Optional.ofNullable(ratingForSafety);
    }

    public boolean isOneStar() {
        return oneStar;
    }

    public ZonedDateTime getSubmissionTime() {
        return submissionTime;
    }

    public Optional<Integer> getRatingForUniqueness() {
        return Optional.ofNullable(ratingForUniqueness);
    }

    public Optional<String> getWhatIsIt() {
        return Optional.ofNullable(whatIsIt);
    }
}
