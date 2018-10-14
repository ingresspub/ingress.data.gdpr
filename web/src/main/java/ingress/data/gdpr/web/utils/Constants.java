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

package ingress.data.gdpr.web.utils;

import static ingress.data.gdpr.models.utils.Preconditions.isEmptyString;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author SgrAlpha
 */
public class Constants {

    public static final String RESOURCE_VERSION = "20181014.13";

    public static final List<Locale> ALL_AVAILABLE_LOCALES = Collections.unmodifiableList(
            Arrays.stream(Locale.getAvailableLocales())
                    .filter(locale -> Objects.nonNull(locale) && !isEmptyString(locale.getCountry()))
                    .sorted(Comparator.comparing(Locale::getDisplayName))
                    .collect(Collectors.toList())
    );

    public static final List<ZoneId> ALL_AVAILABLE_ZONES = Collections.unmodifiableList(
            ZoneId.getAvailableZoneIds().stream()
                    .sorted()
                    .map(ZoneId::of)
                    .collect(Collectors.toList())
    );

}
