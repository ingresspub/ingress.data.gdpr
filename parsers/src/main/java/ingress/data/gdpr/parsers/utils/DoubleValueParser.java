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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author SgrAlpha
 */
public class DoubleValueParser implements SingleLineValueParser<Double> {

    private static final DoubleValueParser INSTANCE = new DoubleValueParser();

    private DoubleValueParser() {}

    @Override public Double parse(final String... columns) {
        checkNotNull(columns, "No columns to parse from");
        checkArgument(columns.length == 1, String.format("Expecting only one column, but got %d", columns.length));
        final String value = columns[0];
        checkArgument(!isNullOrEmpty(value), "Missing value to parse from");
        return Double.parseDouble(value);
    }

    public static DoubleValueParser getDefault() {
        return INSTANCE;
    }

}
