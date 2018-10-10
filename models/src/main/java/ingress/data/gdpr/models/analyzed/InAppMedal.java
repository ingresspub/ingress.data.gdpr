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

import ingress.data.gdpr.models.records.profile.BadgeLevel;

import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class InAppMedal {

    private final BadgeLevel level;
    private final String name;
    private final String url;

    public InAppMedal(final BadgeLevel level, final String name, final String url) {
        this.level = level;
        this.name = name;
        this.url = url;
    }

    public BadgeLevel getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }
}
