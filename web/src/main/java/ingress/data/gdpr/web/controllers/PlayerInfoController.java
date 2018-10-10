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

import ingress.data.gdpr.web.services.Summarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.ZoneId;

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
        map.addAttribute("timeline", summarizer.listBadgeTimeline(ZoneId.systemDefault()));
        return "player/badges";
    }
}
