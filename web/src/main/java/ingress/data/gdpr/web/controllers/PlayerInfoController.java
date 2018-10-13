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

import ingress.data.gdpr.models.analyzed.Circle;
import ingress.data.gdpr.models.utils.JsonUtil;
import ingress.data.gdpr.web.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
@Controller
public class PlayerInfoController {

    private final PlayerService playerService;

    public PlayerInfoController(@Autowired final PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/player/badges")
    public String listBadges(final ModelMap map) {
        if (playerService.noBadgesData()) {
            return "redirect:/upload";
        }
        map.addAttribute("timeline", playerService.listBadgeTimeline());
        return "player/badges";
    }

    @GetMapping("/player/comm/messages")
    public String listCommMessages(
            @RequestParam(value = "curPage", required = false) Integer curPage,
            @RequestParam(value = "perPage", required = false) Integer perPage,
            final ModelMap map) {
        if (playerService.noGameLogData() && playerService.noCommMentions()) {
            return "redirect:/upload";
        }
        map.addAttribute("feed", playerService.listCommMessages(curPage, perPage));
        return "player/comm/messages";
    }

    @GetMapping("/player/unique_portals")
    public String index(
            @RequestParam(value = "excludeCaptured", required = false, defaultValue = "false") boolean excludeCaptured,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "radius", required = false, defaultValue = "20") int radius,
            final ModelMap map) {
        if (playerService.noGameLogData()) {
            return "redirect:/upload";
        }
        String displayColor = Optional.ofNullable(color).orElse("#ff00ff");
        final List<Circle> capturedPortals = playerService.listCapturedPortals(displayColor, radius);
        final List<Circle> visitedPortals = playerService.listVisitedPortals(displayColor, radius);
        if (excludeCaptured) {
            visitedPortals.removeAll(capturedPortals);
        }
        map.addAttribute("capturedPortals", JsonUtil.toJson(capturedPortals));
        map.addAttribute("visitedPortals", JsonUtil.toJson(visitedPortals));
        return "player/unique_portals";
    }

}
