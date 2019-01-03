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

package ingress.data.gdpr.models.analyzed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ingress.data.gdpr.models.records.profile.BadgeLevel;
import io.sgr.geometry.Coordinate;

/**
 * @author SgrAlpha
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewBadgeEvent implements Event {

    private static final String TYPE = "new_badge";

    private final BadgeLevel level;
    private final String name;
    private final String url;
    private final long timeInMs;
    private final String localDate;
    private final String localTime;

    public NewBadgeEvent(final BadgeLevel level, final String name, final String url, final long timeInMs, final String localDate, final String localTime) {
        this.level = level;
        this.name = name;
        this.url = url;
        this.timeInMs = timeInMs;
        this.localDate = localDate;
        this.localTime = localTime;
    }

    @JsonProperty("level")
    public BadgeLevel getLevel() {
        return level;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("localDate")
    public String getLocalDate() {
        return localDate;
    }

    @JsonProperty("localTime")
    public String getLocalTime() {
        return localTime;
    }

    @JsonProperty("type")
    @Override public String getType() {
        return TYPE;
    }

    @JsonProperty("location")
    @Override public Coordinate getLocation() {
        return null;
    }

    @JsonIgnore
    @Override public long getTimeInMs() {
        return timeInMs;
    }

}
