/*
 * Copyright (C) 2014-2019 SgrAlpha
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

package ingress.data.gdpr.models.records.opr;

import java.time.ZonedDateTime;

/**
 * @author SgrAlpha
 */
public class OprAssignmentLogItem {

    private final String candidateId;
    private final ZonedDateTime time;

    public OprAssignmentLogItem(final String candidateId, final ZonedDateTime time) {
        this.candidateId = candidateId;
        this.time = time;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public ZonedDateTime getTime() {
        return time;
    }
}
