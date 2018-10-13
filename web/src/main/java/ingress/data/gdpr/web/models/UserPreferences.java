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

package ingress.data.gdpr.web.models;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

/**
 * @author SgrAlpha
 */
public class UserPreferences {

    private static final String LANGUAGE_TAG = "language_tag";
    private static final String LANGUAGE_TAG_DEFAULT = Locale.US.toLanguageTag();
    private static final String ZONE_ID = "zone_id";
    private static final String ZONE_ID_DEFAULT = "America/Los_Angeles";

    @NotBlank
    private String languageTag;
    @NotBlank
    private String zoneId;

    @SuppressWarnings("unused")
    public UserPreferences() {
    }

    private UserPreferences(final String languageTag, final String zoneId) {
        this.languageTag = Optional.ofNullable(languageTag).orElse(LANGUAGE_TAG_DEFAULT);
        this.zoneId = Optional.ofNullable(zoneId).orElse(ZONE_ID_DEFAULT);
    }

    public static UserPreferences fromMap(final Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return UserPreferences.getDefault();
        }
        return new UserPreferences(map.get(LANGUAGE_TAG), map.get(ZONE_ID));
    }

    public static UserPreferences getDefault() {
        return new UserPreferences(LANGUAGE_TAG_DEFAULT, ZONE_ID_DEFAULT);
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(LANGUAGE_TAG, languageTag);
        map.put(ZONE_ID, zoneId);
        return map;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(final String languageTag) {
        this.languageTag = languageTag;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(final String zoneId) {
        this.zoneId = zoneId;
    }
}
