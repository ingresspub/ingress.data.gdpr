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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Clock;

/**
 * @author SgrAlpha
 */
public class SummarizedReport {

    private final long generatedTimeInMs;

    private MentoringReport mentoring;

    public SummarizedReport() {
        generatedTimeInMs = Clock.systemUTC().millis();
    }

    @JsonProperty("generatedTimeInMs")
    public long getGeneratedTimeInMs() {
        return generatedTimeInMs;
    }

    @JsonProperty("mentoring")
    public MentoringReport getMentoring() {
        return mentoring;
    }

    public void setMentoring(final MentoringReport mentoring) {
        this.mentoring = mentoring;
    }
}
