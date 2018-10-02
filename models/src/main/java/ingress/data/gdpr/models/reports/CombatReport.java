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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ingress.data.gdpr.models.TimestampedRecord;
import ingress.data.gdpr.models.utils.JsonUtil;

import java.util.List;

/**
 * @author SgrAlpha
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CombatReport {

    private ReportDetails<List<TimestampedRecord<Float>>> resonatorsDestroyed;
    private ReportDetails<List<TimestampedRecord<Integer>>> portalsNeutralized;
    private ReportDetails<List<TimestampedRecord<Float>>> linksDestroyed;
    private ReportDetails<List<TimestampedRecord<Float>>> fieldsDestroyed;

    @JsonProperty("resonators_destroyed")
    public ReportDetails<List<TimestampedRecord<Float>>> getResonatorsDestroyed() {
        return resonatorsDestroyed;
    }

    public void setResonatorsDestroyed(final ReportDetails<List<TimestampedRecord<Float>>> resonatorsDestroyed) {
        this.resonatorsDestroyed = resonatorsDestroyed;
    }

    @JsonProperty("portals_neutralized")
    public ReportDetails<List<TimestampedRecord<Integer>>> getPortalsNeutralized() {
        return portalsNeutralized;
    }

    public void setPortalsNeutralized(final ReportDetails<List<TimestampedRecord<Integer>>> portalsNeutralized) {
        this.portalsNeutralized = portalsNeutralized;
    }

    @JsonProperty("links_destroyed")
    public ReportDetails<List<TimestampedRecord<Float>>> getLinksDestroyed() {
        return linksDestroyed;
    }

    public void setLinksDestroyed(final ReportDetails<List<TimestampedRecord<Float>>> linksDestroyed) {
        this.linksDestroyed = linksDestroyed;
    }

    @JsonProperty("fields_destroyed")
    public ReportDetails<List<TimestampedRecord<Float>>> getFieldsDestroyed() {
        return fieldsDestroyed;
    }

    public void setFieldsDestroyed(final ReportDetails<List<TimestampedRecord<Float>>> fieldsDestroyed) {
        this.fieldsDestroyed = fieldsDestroyed;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }

}
