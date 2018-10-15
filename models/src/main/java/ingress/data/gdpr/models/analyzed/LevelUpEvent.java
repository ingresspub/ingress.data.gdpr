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

/**
 * @author SgrAlpha
 */
public class LevelUpEvent {

    private final int level;
    private final Coordinate location;
    private final String time;

    public LevelUpEvent(final int level, final Coordinate location, final String time) {
        this.level = level;
        this.location = location;
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public Coordinate getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
