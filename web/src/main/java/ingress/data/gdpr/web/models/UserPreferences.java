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
    private static final String GOOGLD_API_KEY = "google_api_key";

    @NotBlank
    private String languageTag;
    @NotBlank
    private String zoneId;
    private String googleApiKey;

    @SuppressWarnings("unused")
    public UserPreferences() {
    }

    private UserPreferences(final String languageTag, final String zoneId, final String googleApiKey) {
        this.languageTag = Optional.ofNullable(languageTag).orElse(LANGUAGE_TAG_DEFAULT);
        this.zoneId = Optional.ofNullable(zoneId).orElse(ZONE_ID_DEFAULT);
        this.googleApiKey = googleApiKey;
    }

    public static UserPreferences fromMap(final Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return UserPreferences.getDefault();
        }
        return new UserPreferences(map.get(LANGUAGE_TAG), map.get(ZONE_ID), map.get(GOOGLD_API_KEY));
    }

    public static UserPreferences getDefault() {
        return new UserPreferences(LANGUAGE_TAG_DEFAULT, ZONE_ID_DEFAULT, null);
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(LANGUAGE_TAG, getLanguageTag());
        map.put(ZONE_ID, getZoneId());
        map.put(GOOGLD_API_KEY, getGoogleApiKey());
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

    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public void setGoogleApiKey(final String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }
}
