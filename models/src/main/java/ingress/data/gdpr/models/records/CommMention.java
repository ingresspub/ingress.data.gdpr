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

package ingress.data.gdpr.models.records;

import java.time.ZonedDateTime;

/**
 * @author SgrAlpha
 */
public class CommMention {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final ZonedDateTime time;
    private final boolean secured;
    private final String from;
    private String message;

    public CommMention(final ZonedDateTime time, final boolean secured, final String from, final String message) {
        this.time = time;
        this.secured = secured;
        this.from = from;
        this.message = message;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public boolean isSecured() {
        return secured;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
