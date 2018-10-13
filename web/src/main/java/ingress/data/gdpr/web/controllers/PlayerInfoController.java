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

package ingress.data.gdpr.web.controllers;

import static ingress.data.gdpr.parsers.utils.TimeZoneConstants.DEFAULT_LOCALE;
import static ingress.data.gdpr.parsers.utils.TimeZoneConstants.DEFAULT_ZONE_ID;

import ingress.data.gdpr.web.services.Summarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author SgrAlpha
 */
@Controller
public class PlayerInfoController {

    private final Summarizer summarizer;

    public PlayerInfoController(@Autowired final Summarizer summarizer) {
        this.summarizer = summarizer;
    }

    @GetMapping("/player/badges")
    public String listBadges(final ModelMap map) {
        if (summarizer.noBadgesData()) {
            return "redirect:/upload";
        }
        map.addAttribute("timeline", summarizer.listBadgeTimeline(DEFAULT_LOCALE, DEFAULT_ZONE_ID));
        return "player/badges";
    }

    @GetMapping("/player/comm/messages")
    public String listCommMessages(
            @RequestParam(value = "curPage", required = false) Integer curPage,
            @RequestParam(value = "perPage", required = false) Integer perPage,
            final ModelMap map) {
        if (summarizer.noGameLogData() && summarizer.noCommMentions()) {
            return "redirect:/upload";
        }
        map.addAttribute("feed", summarizer.listCommMessages(curPage, perPage, DEFAULT_LOCALE, DEFAULT_ZONE_ID));
        return "player/comm/messages";
    }
}
