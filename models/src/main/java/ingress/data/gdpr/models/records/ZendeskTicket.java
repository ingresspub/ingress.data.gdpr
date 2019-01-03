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

package ingress.data.gdpr.models.records;

import java.time.ZonedDateTime;

/**
 * @author SgrAlpha
 */
public class ZendeskTicket {

    public static final String TIME_PATTERN = "MMM d, yyyy h:mm:ss a";

    private final ZonedDateTime time;
    private final String subject;
    private final String comment;

    public ZendeskTicket(final ZonedDateTime time, final String subject, final String comment) {
        this.time = time;
        this.subject = subject;
        this.comment = comment;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public String getComment() {
        return comment;
    }
}
