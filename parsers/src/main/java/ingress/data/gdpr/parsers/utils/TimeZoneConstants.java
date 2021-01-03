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

package ingress.data.gdpr.parsers.utils;

import java.time.ZoneId;
import java.util.Locale;

/**
 * @author SgrAlpha
 */
public class TimeZoneConstants {

    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static final ZoneId DEFAULT_ZONE_ID = UTC_ZONE_ID;

    public static final Locale DEFAULT_LOCALE = Locale.US;

}
