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
package ingress.data.gdpr.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author SgrAlpha
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheableData<T> {

    private final T data;
    private final long lastUpdatedTimeMs;

    public CacheableData(T data, long lastUpdatedTimeMs) {
        this.data = data;
        this.lastUpdatedTimeMs = lastUpdatedTimeMs;
    }

    @JsonProperty("data")
    public T getData() {
        return this.data;
    }

    @JsonProperty("lastUpdatedTimeMs")
    public long getLastUpdatedTimeMs() {
        return this.lastUpdatedTimeMs;
    }

}
