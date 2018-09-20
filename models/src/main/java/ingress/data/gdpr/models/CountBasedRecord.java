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

package ingress.data.gdpr.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import ingress.data.gdpr.models.utils.JsonUtil;

import java.time.ZonedDateTime;

/**
 * @author SgrAlpha
 */
public class CountBasedRecord implements TimestampedRecord {

    private final ZonedDateTime time;
    private final long count;

    public CountBasedRecord(final ZonedDateTime time, final long count) {
        this.time = time;
        this.count = count;
    }

    @JsonProperty("time")
    @Override public ZonedDateTime getTime() {
        return time;
    }

    @JsonProperty("count")
    public long getCount() {
        return count;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }
}
