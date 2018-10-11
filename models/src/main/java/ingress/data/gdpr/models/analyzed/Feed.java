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

package ingress.data.gdpr.models.analyzed;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class Feed<T> {

    private final List<T> messages;
    private final Integer curPage;
    private final Integer pageSize;

    public Feed(final List<T> messages, final Integer curPage, final Integer pageSize) {
        this.messages = messages;
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    public List<T> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public Optional<Integer> getCurPage() {
        return Optional.ofNullable(curPage);
    }

    public Optional<Integer> getPageSize() {
        return Optional.ofNullable(pageSize);
    }
}
