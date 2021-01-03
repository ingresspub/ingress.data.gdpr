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

package ingress.data.gdpr.models.analyzed;

import io.sgr.geometry.Coordinate;

import java.util.Objects;

/**
 * @author SgrAlpha
 */
public class Marker {

    private final Coordinate latLng;
    private final String color;

    public Marker(final Coordinate latLng, final String color) {
        this.latLng = latLng;
        this.color = color;
    }

    public String getType() {
        return "marker";
    }

    public Coordinate getLatLng() {
        return latLng;
    }

    public String getColor() {
        return color;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marker)) {
            return false;
        }
        final Marker marker = (Marker) o;
        return Objects.equals(getLatLng(), marker.getLatLng());
    }

    @Override public int hashCode() {
        return Objects.hash(getLatLng());
    }
}
