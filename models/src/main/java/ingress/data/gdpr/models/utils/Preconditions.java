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

package ingress.data.gdpr.models.utils;

/**
 * @author SgrAlpha
 */
public final class Preconditions {

    private static final String DEFAULT_MESSAGE = "Received an invalid parameter"; //$NON-NLS-1$

    /**
     * Make sure the given object is not null, otherwise an IllegalArgumentException will be thrown
     *
     * @param object   any object
     * @param errorMsg error message
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(final Object object, final String errorMsg) {
        matchRequirement(object != null, errorMsg);
    }

    /**
     * Make sure the given string is not null or a blank string, otherwise an IllegalArgumentException will be thrown
     *
     * @param string   any string
     * @param errorMsg error message
     * @throws IllegalArgumentException if the string is null or a blank string
     */
    public static void notEmptyString(final String string, final String errorMsg) {
        matchRequirement(!isEmptyString(string), errorMsg);
    }

    /**
     * Check if the specified string is null or blank string
     *
     * @param string any string
     * @return Whether or not the given string is an empty string
     */
    public static boolean isEmptyString(final String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Check if the requirement matches
     *
     * @param requirement expression to check
     * @param errorMsg    error message if does not match the requirement
     * @throws IllegalArgumentException if does not match the requirement
     */
    public static void matchRequirement(final boolean requirement, final String errorMsg) {
        if (!requirement) {
            throw new IllegalArgumentException(isEmptyString(errorMsg) ? DEFAULT_MESSAGE : errorMsg);
        }
    }

}
