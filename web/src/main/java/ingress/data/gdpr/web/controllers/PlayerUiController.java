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

package ingress.data.gdpr.web.controllers;

import static com.google.common.base.Strings.isNullOrEmpty;

import ingress.data.gdpr.web.services.PlayerService;
import ingress.data.gdpr.web.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author SgrAlpha
 */
@Controller
public class PlayerUiController {

    private final PlayerService playerService;
    private final UserService userService;

    public PlayerUiController(
            @Autowired final PlayerService playerService, @Autowired final UserService userService) {
        this.playerService = playerService;
        this.userService = userService;
    }

    @GetMapping("/player/profile/events")
    public String listProfileEvents(final ModelMap map) {
        if (playerService.noBadgesData()) {
            return "redirect:/upload";
        }
        map.addAttribute("timeline", playerService.listProfileEvents());
        return "player/profile/events";
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

    @GetMapping("/player/portals/upc")
    public String goToUpcPage(final ModelMap map) {
        if (playerService.noGameLogData()) {
            return "redirect:/upload";
        }
        final String googleApiKey = userService.getUserPreferences().getGoogleApiKey();
        if (!isNullOrEmpty(googleApiKey)) {
            map.addAttribute("googleApiKey", userService.getUserPreferences().getGoogleApiKey());
        }
        return "player/maps/upc";
    }

    @GetMapping("/player/portals/upv")
    public String goToUpvPage(final ModelMap map) {
        if (playerService.noGameLogData()) {
            return "redirect:/upload";
        }
        final String googleApiKey = userService.getUserPreferences().getGoogleApiKey();
        if (!isNullOrEmpty(googleApiKey)) {
            map.addAttribute("googleApiKey", googleApiKey);
        }
        return "player/maps/upv";
    }

    @GetMapping("/player/events/level_up")
    public String goToLevelUpMapPage(final ModelMap map) {
        if (playerService.noGameLogData()) {
            return "redirect:/upload";
        }
        final String googleApiKey = userService.getUserPreferences().getGoogleApiKey();
        if (!isNullOrEmpty(googleApiKey)) {
            map.addAttribute("googleApiKey", googleApiKey);
        }
        return "player/maps/level_up";
    }

}
