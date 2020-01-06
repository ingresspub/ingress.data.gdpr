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

package ingress.data.gdpr.models.analyzed;

import io.sgr.geometry.Coordinate;

import java.util.Objects;

/**
 * @author SgrAlpha
 */
public class Circle {

    private final Coordinate latLng;
    private final String color;
    private final int radius;

    public Circle(final Coordinate latLng, final String color, final int radius) {
        this.latLng = latLng;
        this.color = color;
        this.radius = radius;
    }

    public String getType() {
        return "circle";
    }

    public Coordinate getLatLng() {
        return latLng;
    }

    public String getColor() {
        return color;
    }

    public int getRadius() {
        return radius;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Circle)) {
            return false;
        }
        final Circle circle = (Circle) o;
        return Objects.equals(getLatLng(), circle.getLatLng());
    }

    @Override public int hashCode() {
        return Objects.hash(getLatLng());
    }
}
