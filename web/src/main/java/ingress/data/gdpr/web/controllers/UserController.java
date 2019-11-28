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

import ingress.data.gdpr.web.models.UserPreferences;
import ingress.data.gdpr.web.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author SgrAlpha
 */
@Controller
public class UserController {

    private final UserService userService;

    public UserController(@Autowired final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/preferences")
    public String showUploadPage(final ModelMap map) {
        UserPreferences preferences = userService.getUserPreferences();
        map.addAttribute("userPreferences", preferences);
        return "user/preferences";
    }

    @PostMapping("/user/preferences")
    public String handleFileUpload(
            @Valid @ModelAttribute("userPreferences") final UserPreferences userPreferences,
            final BindingResult result, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/user/preferences";
        }
        userService.saveUserService(userPreferences);
        return "redirect:/user/preferences";
    }

}
