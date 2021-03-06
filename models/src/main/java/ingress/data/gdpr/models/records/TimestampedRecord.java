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

package ingress.data.gdpr.models.records;

import ingress.data.gdpr.models.utils.JsonUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * @author SgrAlpha
 */
public class TimestampedRecord<T> {

    private final ZonedDateTime time;
    private final T value;

    public TimestampedRecord(final ZonedDateTime time, final T value) {
        this.time = time;
        this.value = value;
    }

    @JsonProperty("time")
    public ZonedDateTime getTime() {
        return time;
    }

    @JsonProperty("value")
    public T getValue() {
        return value;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }
}
