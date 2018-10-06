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
import ingress.data.gdpr.web.services.Summarizer;
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
public class UniquePortalsController {

    private final Summarizer summarizer;

    public UniquePortalsController(@Autowired final Summarizer summarizer) {
        this.summarizer = summarizer;
    }

    @GetMapping("/unique_portals")
    public String index(
            @RequestParam(value = "excludeCaptured", required = false, defaultValue = "false") boolean excludeCaptured,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "radius", required = false, defaultValue = "20") int radius,
            final ModelMap map) {
        String displayColor = Optional.ofNullable(color).orElse("#ff00ff");
        final List<Circle> capturedPortals = summarizer.listCapturedPortals(displayColor, radius);
        final List<Circle> visitedPortals = summarizer.listVisitedPortals(displayColor, radius);
        if (excludeCaptured) {
            visitedPortals.removeAll(capturedPortals);
        }
        map.addAttribute("capturedPortals", JsonUtil.toJson(capturedPortals));
        map.addAttribute("visitedPortals", JsonUtil.toJson(visitedPortals));
        return "unique_portals";
    }

}
