/*
 * Copyright (C) 2014-2020 SgrAlpha
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
 *
 */

package ingress.data.gdpr.web.controllers;

import ingress.data.gdpr.web.dao.RawDataDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author SgrAlpha
 */
@Controller
public class IndexController {

    private final RawDataDao rawDataDao;

    public IndexController(@Autowired final RawDataDao rawDataDao) {
        this.rawDataDao = rawDataDao;
    }

    @GetMapping("/")
    public String index() {
        if (rawDataDao.noGameLogData()) {
            return "redirect:/upload";
        }
        return "redirect:/player/profile/events";
    }

}
