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

import io.sgr.geometry.Coordinate;

/**
 * @author SgrAlpha
 */
public class CommMessageInTimeline {

    private final String time;
    private final Coordinate location;
    private final boolean secured;
    private final String from;
    private final String content;
    private final CommMessageType type;

    public CommMessageInTimeline(final String time, final Coordinate location, final boolean secured, final String from, final String content, final CommMessageType type) {
        this.time = time;
        this.location = location;
        this.secured = secured;
        this.from = from;
        this.content = content;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public Coordinate getLocation() {
        return location;
    }

    public boolean isSecured() {
        return secured;
    }

    public String getFrom() {
        return from;
    }

    public String getContent() {
        return content;
    }

    public CommMessageType getType() {
        return type;
    }

}
