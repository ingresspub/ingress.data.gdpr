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

package ingress.data.gdpr.models.records.mission;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class MissionDetail {

    private final ZonedDateTime creationTime;
    private final ZonedDateTime lastModifiedTime;
    private final int version;
    private final String description;
    private final String type;

    private final List<String> wayPoints = new LinkedList<>();

    public MissionDetail(final ZonedDateTime creationTime, final ZonedDateTime lastModifiedTime,
                         final int version, final String description, final String type) {
        this.creationTime = creationTime;
        this.lastModifiedTime = lastModifiedTime;
        this.version = version;
        this.description = description;
        this.type = type;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public ZonedDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public List<String> getWayPoints() {
        return Collections.unmodifiableList(wayPoints);
    }

    public void addWayPoints(final String wayPoint) {
        checkArgument(!isNullOrEmpty(wayPoint), "");
        this.wayPoints.add(wayPoint);
    }
}
