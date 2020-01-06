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

package ingress.data.gdpr.models.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author SgrAlpha
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDetails<T> {

    private final boolean ok;
    private final T data;
    private final String error;


    private ReportDetails(final boolean ok, final T data, final String error) {
        this.ok = ok;
        this.data = data;
        this.error = error;
    }

    public static <T> ReportDetails<T> ok(final T data) {
        return new ReportDetails<>(true, data, null);
    }

    public static <T> ReportDetails<T> error(final String error) {
        return new ReportDetails<>(false, null, error);
    }

    @JsonProperty("ok")
    public boolean isOk() {
        return ok;
    }

    @JsonProperty("data")
    public T getData() {
        return data;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

}
