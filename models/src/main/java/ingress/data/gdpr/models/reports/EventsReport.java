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
import ingress.data.gdpr.models.records.TimestampedRecord;

import java.util.List;

/**
 * @author SgrAlpha
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsReport {

    private ReportDetails<List<TimestampedRecord<Float>>> exo5ControlFieldsCreated;
    private ReportDetails<List<TimestampedRecord<Integer>>> magusBuilderSlotsDeployed;
    private ReportDetails<List<TimestampedRecord<Integer>>> neutralizerUniquePortalsDestroyed;
    private ReportDetails<List<TimestampedRecord<Integer>>> missionDayPoints;

    @JsonProperty("exo5_controller_fields_created")
    public ReportDetails<List<TimestampedRecord<Float>>> getExo5ControlFieldsCreated() {
        return exo5ControlFieldsCreated;
    }

    public void setExo5ControlFieldsCreated(final ReportDetails<List<TimestampedRecord<Float>>> exo5ControlFieldsCreated) {
        this.exo5ControlFieldsCreated = exo5ControlFieldsCreated;
    }

    @JsonProperty("magnus_builder_slots_deployed")
    public ReportDetails<List<TimestampedRecord<Integer>>> getMagusBuilderSlotsDeployed() {
        return magusBuilderSlotsDeployed;
    }

    public void setMagusBuilderSlotsDeployed(final ReportDetails<List<TimestampedRecord<Integer>>> magusBuilderSlotsDeployed) {
        this.magusBuilderSlotsDeployed = magusBuilderSlotsDeployed;
    }

    @JsonProperty("neutralizer_unique_portals_neutralized")
    public ReportDetails<List<TimestampedRecord<Integer>>> getNeutralizerUniquePortalsDestroyed() {
        return neutralizerUniquePortalsDestroyed;
    }

    public void setNeutralizerUniquePortalsDestroyed(final ReportDetails<List<TimestampedRecord<Integer>>> neutralizerUniquePortalsDestroyed) {
        this.neutralizerUniquePortalsDestroyed = neutralizerUniquePortalsDestroyed;
    }

    @JsonProperty("mission_day_points")
    public ReportDetails<List<TimestampedRecord<Integer>>> getMissionDayPoints() {
        return missionDayPoints;
    }

    public void setMissionDayPoints(final ReportDetails<List<TimestampedRecord<Integer>>> missionDayPoints) {
        this.missionDayPoints = missionDayPoints;
    }
}
