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

package ingress.data.gdpr.web.services;

import ingress.data.gdpr.models.analyzed.CommMessageInTimeline;
import ingress.data.gdpr.models.analyzed.Event;
import ingress.data.gdpr.models.analyzed.Feed;
import ingress.data.gdpr.web.dao.RawDataDao;
import ingress.data.gdpr.web.models.UserPreferences;
import io.sgr.geometry.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author SgrAlpha
 */
@Service
public class PlayerService {

    private final RawDataDao rawDataDao;
    private final UserService userService;

    public PlayerService(@Autowired final RawDataDao rawDataDao, @Autowired final UserService userService) {
        this.rawDataDao = rawDataDao;
        this.userService = userService;
    }

    public List<Event> listProfileEvents() {
        final UserPreferences preferences = userService.getUserPreferences();
        final Locale userLocale = Locale.forLanguageTag(preferences.getLanguageTag());
        final ZoneId userZoneId = ZoneId.of(preferences.getZoneId());
        return rawDataDao.listProfileEvents(userLocale, userZoneId, FormatStyle.LONG);
    }

    public List<Event> listLevelUpEvents() {
        final UserPreferences preferences = userService.getUserPreferences();
        final Locale userLocale = Locale.forLanguageTag(preferences.getLanguageTag());
        final ZoneId userZoneId = ZoneId.of(preferences.getZoneId());
        return rawDataDao.listLevelUpEvent(userLocale, userZoneId, FormatStyle.LONG);
    }

    public Feed<CommMessageInTimeline> listCommMessages(final Integer curPage, final Integer pageSize) {
        final UserPreferences preferences = userService.getUserPreferences();
        final Locale userLocale = Locale.forLanguageTag(preferences.getLanguageTag());
        final ZoneId userZoneId = ZoneId.of(preferences.getZoneId());
        return rawDataDao.listCommMessages(curPage, pageSize, userLocale, userZoneId, FormatStyle.FULL);
    }

    public List<Coordinate> listUpc() {
        if (noGameLogData()) {
            return Collections.emptyList();
        }
        return rawDataDao.listUpc();
    }

    public List<Coordinate> listUpv(final boolean excludeCaptured) {
        if (noGameLogData()) {
            return Collections.emptyList();
        }
        return rawDataDao.listUpv(excludeCaptured);
    }

    public boolean noGameLogData() {
        return rawDataDao.noGameLogData();
    }

    public boolean noBadgesData() {
        return rawDataDao.noBadgesData();
    }

    public boolean noCommMentions() {
        return rawDataDao.noCommMentions();
    }

}
