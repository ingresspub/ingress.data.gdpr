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

package ingress.data.gdpr.models.analyzed;

import io.sgr.geometry.Coordinate;

import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class CommMessageInTimeline {

    private final String date;
    private final String time;
    private final Coordinate location;
    private final String content;

    public CommMessageInTimeline(final String date, final String time, final Coordinate location, final String content) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Optional<Coordinate> getLocation() {
        return Optional.ofNullable(location);
    }

    public String getContent() {
        return content;
    }
}
