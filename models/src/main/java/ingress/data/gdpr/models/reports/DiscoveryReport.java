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
public class DiscoveryReport {

    private ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements;
    private ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved;
    private ReportDetails<List<TimestampedRecord<Integer>>> seerPortals;
    private ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited;
    private ReportDetails<List<TimestampedRecord<Float>>> xmCollected;

    @JsonProperty("opr_agreements")
    public ReportDetails<List<TimestampedRecord<Integer>>> getOprAgreements() {
        return oprAgreements;
    }

    public void setOprAgreements(final ReportDetails<List<TimestampedRecord<Integer>>> oprAgreements) {
        this.oprAgreements = oprAgreements;
    }

    @JsonProperty("all_portals_approved")
    public ReportDetails<List<TimestampedRecord<Integer>>> getAllPortalsApproved() {
        return allPortalsApproved;
    }

    public void setAllPortalsApproved(final ReportDetails<List<TimestampedRecord<Integer>>> allPortalsApproved) {
        this.allPortalsApproved = allPortalsApproved;
    }

    @JsonProperty("seer_portals")
    public ReportDetails<List<TimestampedRecord<Integer>>> getSeerPortals() {
        return seerPortals;
    }

    public void setSeerPortals(final ReportDetails<List<TimestampedRecord<Integer>>> seerPortals) {
        this.seerPortals = seerPortals;
    }

    @JsonProperty("portals_visited")
    public ReportDetails<List<TimestampedRecord<Integer>>> getPortalsVisited() {
        return portalsVisited;
    }

    public void setPortalsVisited(final ReportDetails<List<TimestampedRecord<Integer>>> portalsVisited) {
        this.portalsVisited = portalsVisited;
    }

    @JsonProperty("xm_collected")
    public ReportDetails<List<TimestampedRecord<Float>>> getXmCollected() {
        return xmCollected;
    }

    public void setXmCollected(final ReportDetails<List<TimestampedRecord<Float>>> xmCollected) {
        this.xmCollected = xmCollected;
    }
}
