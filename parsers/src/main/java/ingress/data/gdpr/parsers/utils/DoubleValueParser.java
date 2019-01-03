/*
 * Copyright (C) 2014-2019 SgrAlpha
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

package ingress.data.gdpr.parsers.utils;

import static ingress.data.gdpr.models.utils.Preconditions.notEmptyString;
import static ingress.data.gdpr.models.utils.Preconditions.notNull;

/**
 * @author SgrAlpha
 */
public class DoubleValueParser implements SingleLineValueParser<Double> {

    private static final DoubleValueParser INSTANCE = new DoubleValueParser();

    private DoubleValueParser() {}

    @Override public Double parse(final String... columns) {
        notNull(columns, "No columns to parse from");
        if (columns.length != 1) {
            throw new IllegalArgumentException(String.format("Expecting only one column, but got %d", columns.length));
        }
        final String value = columns[0];
        notEmptyString(value, "Missing value to parse from");
        return Double.parseDouble(value);
    }

    public static DoubleValueParser getDefault() {
        return INSTANCE;
    }

}
