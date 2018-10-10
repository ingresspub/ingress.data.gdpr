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

package ingress.data.gdpr.models.analyzed;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author SgrAlpha
 */
public class TimelineItem<T> {

    private final String type;
    private final String subject;
    private final String time;
    private final T details;

    public TimelineItem(final String type, final String subject, final String time, final T details) {
        this.type = type;
        this.subject = subject;
        this.time = time;
        this.details = details;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("details")
    public T getDetails() {
        return details;
    }
}
