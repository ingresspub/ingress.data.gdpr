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

package ingress.data.gdpr.models.records;

import io.sgr.geometry.Coordinate;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class GameLog {

    public static final String TIME_PATTERN = "yyyy MM-dd HH:mm:ss";

    private final ZonedDateTime time;
    private final Coordinate location;
    private final String trackerTrigger;
    private final String comments;
    private final String other;

    public GameLog(final ZonedDateTime time, final Coordinate location, final String trackerTrigger, final String comments, final String other) {
        this.time = time;
        this.location = location;
        this.trackerTrigger = trackerTrigger;
        this.comments = comments;
        this.other = other;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Optional<Coordinate> getLocation() {
        return Optional.ofNullable(location);
    }

    public String getTrackerTrigger() {
        return trackerTrigger;
    }

    public String getComments() {
        return comments;
    }

    public String getOther() {
        return other;
    }
}
