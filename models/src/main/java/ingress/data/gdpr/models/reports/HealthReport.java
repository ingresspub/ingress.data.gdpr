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

package ingress.data.gdpr.models.reports;

import static ingress.data.gdpr.models.utils.Preconditions.notNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.utils.JsonUtil;

import java.util.List;

/**
 * @author SgrAlpha
 */
public class HealthReport {

    private final ReportDetails<List<TimestampedRecord<Float>>> kilometersWalked;

    public HealthReport(final ReportDetails<List<TimestampedRecord<Float>>> kilometersWalked) {
        notNull(kilometersWalked, "Missing details about kilometers walked");
        this.kilometersWalked = kilometersWalked;
    }

    @JsonProperty("kilometers_walked")
    public ReportDetails<List<TimestampedRecord<Float>>> getKilometersWalked() {
        return kilometersWalked;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }

}
