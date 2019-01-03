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

package ingress.data.gdpr.models.analyzed;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * @author SgrAlpha
 */
public class Feed<T> {

    private final List<T> data;
    private final int curPage;
    private final int pageSize;

    public Feed(final List<T> data, final int curPage, final int pageSize) {
        this.data = data;
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    @JsonProperty("data")
    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    @JsonProperty("curPage")
    public int getCurPage() {
        return curPage;
    }

    @JsonProperty("pageSize")
    public int getPageSize() {
        return pageSize;
    }

}
