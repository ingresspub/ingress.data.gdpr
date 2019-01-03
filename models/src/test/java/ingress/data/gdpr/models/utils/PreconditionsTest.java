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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author SgrAlpha
 */
public class PreconditionsTest {

    @Test
    public void testBlankErrorMessage() {
        try {
            Preconditions.notNull(null, "");
            fail();
        } catch (IllegalArgumentException e) {
            assertFalse(Preconditions.isEmptyString(e.getMessage()));
        }
    }

    @Test
    public void testCheckNull() {
        try {
            Preconditions.notNull(null, "err msg");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("err msg", e.getMessage());
        }
        Preconditions.notNull("", "err?");
        Preconditions.notNull("\n", "err?");
        Preconditions.notNull("\n\n\n", "err?");
        Preconditions.notNull("abc", "err?");
    }

    @Test
    public void testCheckString() {
        assertTrue(Preconditions.isEmptyString(null));
        assertTrue(Preconditions.isEmptyString(""));
        assertTrue(Preconditions.isEmptyString("\n"));
        assertTrue(Preconditions.isEmptyString("\n\n\n"));
        assertTrue(Preconditions.isEmptyString(" "));
        assertTrue(Preconditions.isEmptyString(" \n"));
        assertFalse(Preconditions.isEmptyString("abd"));
        try {
            Preconditions.notEmptyString(null, "err msg");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("err msg", e.getMessage());
        }
        try {
            Preconditions.notEmptyString("", "err msg");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("err msg", e.getMessage());
        }
        try {
            Preconditions.notEmptyString("\n", "err msg");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("err msg", e.getMessage());
        }
        try {
            Preconditions.notEmptyString("\n\n\n", "err msg");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("err msg", e.getMessage());
        }
        Preconditions.notEmptyString("abd", "err?");
    }

}
