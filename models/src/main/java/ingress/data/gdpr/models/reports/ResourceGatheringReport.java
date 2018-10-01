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
public class ResourceGatheringReport {

    private ReportDetails<List<NumericBasedRecord<Float>>> hacks;
    private ReportDetails<List<NumericBasedRecord<Float>>> glyphHackPoints;
    private ReportDetails<List<NumericBasedRecord<Float>>> glyphHackOnePerfect;
    private ReportDetails<List<NumericBasedRecord<Float>>> glyphHackTwoPerfect;
    private ReportDetails<List<NumericBasedRecord<Float>>> glyphHackThreePerfect;
    private ReportDetails<List<NumericBasedRecord<Float>>> glyphHackFourPerfect;
    private ReportDetails<List<NumericBasedRecord<Float>>> glyphHackFivePerfect;

    @JsonProperty("hacks")
    public ReportDetails<List<NumericBasedRecord<Float>>> getHacks() {
        return hacks;
    }

    public void setHacks(final ReportDetails<List<NumericBasedRecord<Float>>> hacks) {
        this.hacks = hacks;
    }

    @JsonProperty("glyph_hack_points")
    public ReportDetails<List<NumericBasedRecord<Float>>> getGlyphHackPoints() {
        return glyphHackPoints;
    }

    public void setGlyphHackPoints(final ReportDetails<List<NumericBasedRecord<Float>>> glyphHackPoints) {
        this.glyphHackPoints = glyphHackPoints;
    }

    @JsonProperty("glyph_hack_1_perfect")
    public ReportDetails<List<NumericBasedRecord<Float>>> getGlyphHackOnePerfect() {
        return glyphHackOnePerfect;
    }

    public void setGlyphHackOnePerfect(final ReportDetails<List<NumericBasedRecord<Float>>> glyphHackOnePerfect) {
        this.glyphHackOnePerfect = glyphHackOnePerfect;
    }

    @JsonProperty("glyph_hack_2_perfect")
    public ReportDetails<List<NumericBasedRecord<Float>>> getGlyphHackTwoPerfect() {
        return glyphHackTwoPerfect;
    }

    public void setGlyphHackTwoPerfect(final ReportDetails<List<NumericBasedRecord<Float>>> glyphHackTwoPerfect) {
        this.glyphHackTwoPerfect = glyphHackTwoPerfect;
    }

    @JsonProperty("glyph_hack_3_perfect")
    public ReportDetails<List<NumericBasedRecord<Float>>> getGlyphHackThreePerfect() {
        return glyphHackThreePerfect;
    }

    public void setGlyphHackThreePerfect(final ReportDetails<List<NumericBasedRecord<Float>>> glyphHackThreePerfect) {
        this.glyphHackThreePerfect = glyphHackThreePerfect;
    }

    @JsonProperty("glyph_hack_4_perfect")
    public ReportDetails<List<NumericBasedRecord<Float>>> getGlyphHackFourPerfect() {
        return glyphHackFourPerfect;
    }

    public void setGlyphHackFourPerfect(final ReportDetails<List<NumericBasedRecord<Float>>> glyphHackFourPerfect) {
        this.glyphHackFourPerfect = glyphHackFourPerfect;
    }

    @JsonProperty("glyph_hack_5_perfect")
    public ReportDetails<List<NumericBasedRecord<Float>>> getGlyphHackFivePerfect() {
        return glyphHackFivePerfect;
    }

    public void setGlyphHackFivePerfect(final ReportDetails<List<NumericBasedRecord<Float>>> glyphHackFivePerfect) {
        this.glyphHackFivePerfect = glyphHackFivePerfect;
    }

    @Override public String toString() {
        return JsonUtil.toJson(this);
    }

}
