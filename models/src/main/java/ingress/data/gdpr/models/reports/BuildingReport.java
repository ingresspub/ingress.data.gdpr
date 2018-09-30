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
import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.utils.JsonUtil;

import java.util.List;

/**
 * @author SgrAlpha
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BuildingReport {

    private ReportDetails<List<NumericBasedRecord<Float>>> mindUnitsControlled;
    private ReportDetails<List<NumericBasedRecord<Integer>>> mindUnitsControlledActive;
    private ReportDetails<List<NumericBasedRecord<Float>>> fieldsCreated;
    private ReportDetails<List<NumericBasedRecord<Integer>>> fieldsCreatedActive;
    private ReportDetails<List<NumericBasedRecord<Float>>> linksCreated;
    private ReportDetails<List<NumericBasedRecord<Double>>> linkLengthInKm;
    private ReportDetails<List<NumericBasedRecord<Integer>>> linksCreatedActive;
    private ReportDetails<List<NumericBasedRecord<Integer>>> portalsCaptured;
    private ReportDetails<List<NumericBasedRecord<Integer>>> portalsOwned;
    private ReportDetails<List<NumericBasedRecord<Float>>> resonatorsDeployed;
    private ReportDetails<List<NumericBasedRecord<Float>>> modsDeployed;
    private ReportDetails<List<NumericBasedRecord<Float>>> xmRecharged;

    @JsonProperty("mind_units_controlled")
    public ReportDetails<List<NumericBasedRecord<Float>>> getMindUnitsControlled() {
        return mindUnitsControlled;
    }

    public void setMindUnitsControlled(final ReportDetails<List<NumericBasedRecord<Float>>> mindUnitsControlled) {
        this.mindUnitsControlled = mindUnitsControlled;
    }

    @JsonProperty("mind_units_controlled_active")
    public ReportDetails<List<NumericBasedRecord<Integer>>> getMindUnitsControlledActive() {
        return mindUnitsControlledActive;
    }

    public void setMindUnitsControlledActive(final ReportDetails<List<NumericBasedRecord<Integer>>> mindUnitsControlledActive) {
        this.mindUnitsControlledActive = mindUnitsControlledActive;
    }

    @JsonProperty("fields_created")
    public ReportDetails<List<NumericBasedRecord<Float>>> getFieldsCreated() {
        return fieldsCreated;
    }

    public void setFieldsCreated(final ReportDetails<List<NumericBasedRecord<Float>>> fieldsCreated) {
        this.fieldsCreated = fieldsCreated;
    }

    @JsonProperty("fields_created_active")
    public ReportDetails<List<NumericBasedRecord<Integer>>> getFieldsCreatedActive() {
        return fieldsCreatedActive;
    }

    public void setFieldsCreatedActive(final ReportDetails<List<NumericBasedRecord<Integer>>> fieldsCreatedActive) {
        this.fieldsCreatedActive = fieldsCreatedActive;
    }

    @JsonProperty("links_created")
    public ReportDetails<List<NumericBasedRecord<Float>>> getLinksCreated() {
        return linksCreated;
    }

    public void setLinksCreated(final ReportDetails<List<NumericBasedRecord<Float>>> linksCreated) {
        this.linksCreated = linksCreated;
    }

    @JsonProperty("link_length_in_km")
    public ReportDetails<List<NumericBasedRecord<Double>>> getLinkLengthInKm() {
        return linkLengthInKm;
    }

    public void setLinkLengthInKm(final ReportDetails<List<NumericBasedRecord<Double>>> linkLengthInKm) {
        this.linkLengthInKm = linkLengthInKm;
    }

    @JsonProperty("links_created_active")
    public ReportDetails<List<NumericBasedRecord<Integer>>> getLinksCreatedActive() {
        return linksCreatedActive;
    }

    public void setLinksCreatedActive(final ReportDetails<List<NumericBasedRecord<Integer>>> linksCreatedActive) {
        this.linksCreatedActive = linksCreatedActive;
    }

    @JsonProperty("portals_captured")
    public ReportDetails<List<NumericBasedRecord<Integer>>> getPortalsCaptured() {
        return portalsCaptured;
    }

    public void setPortalsCaptured(final ReportDetails<List<NumericBasedRecord<Integer>>> portalCaptured) {
        this.portalsCaptured = portalCaptured;
    }

    @JsonProperty("portals_owned")
    public ReportDetails<List<NumericBasedRecord<Integer>>> getPortalsOwned() {
        return portalsOwned;
    }

    public void setPortalsOwned(final ReportDetails<List<NumericBasedRecord<Integer>>> portalsOwned) {
        this.portalsOwned = portalsOwned;
    }

    @JsonProperty("resonators_deployed")
    public ReportDetails<List<NumericBasedRecord<Float>>> getResonatorsDeployed() {
        return resonatorsDeployed;
    }

    public void setResonatorsDeployed(final ReportDetails<List<NumericBasedRecord<Float>>> resonatorsDeployed) {
        this.resonatorsDeployed = resonatorsDeployed;
    }

    @JsonProperty("mods_deployed")
    public ReportDetails<List<NumericBasedRecord<Float>>> getModsDeployed() {
        return modsDeployed;
    }

    public void setModsDeployed(final ReportDetails<List<NumericBasedRecord<Float>>> modsDeployed) {
        this.modsDeployed = modsDeployed;
    }

    @JsonProperty("xm_recharged")
    public ReportDetails<List<NumericBasedRecord<Float>>> getXmRecharged() {
        return xmRecharged;
    }

    public void setXmRecharged(final ReportDetails<List<NumericBasedRecord<Float>>> xmRecharged) {
        this.xmRecharged = xmRecharged;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }

}
