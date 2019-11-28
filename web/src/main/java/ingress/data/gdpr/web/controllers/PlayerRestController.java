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

import ingress.data.gdpr.models.analyzed.Circle;
import ingress.data.gdpr.models.analyzed.Event;
import ingress.data.gdpr.web.models.CacheableData;
import ingress.data.gdpr.web.services.PlayerService;

import io.sgr.geometry.Coordinate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SgrAlpha
 */
@RestController
public class PlayerRestController {

    private final PlayerService playerService;

    public PlayerRestController(@Autowired final PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/r/player/profile/events/level_up.json")
    public ResponseEntity<CacheableData<List<Event>>> listLevelUpEvents() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(new CacheableData<>(playerService.listLevelUpEvents(), Clock.systemUTC().millis()));
    }

    @GetMapping("/r/player/portals/upc.json")
    public ResponseEntity<CacheableData<List<Coordinate>>> listUpc() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(new CacheableData<>(playerService.listUpc(), Clock.systemUTC().millis()));
    }

    @GetMapping("/r/player/portals/upc.iitc.json")
    public ResponseEntity<List<Circle>> listUpcForIitc(
            @RequestParam(value = "color", required = false, defaultValue = "#ff00ff") String color,
            @RequestParam(value = "radius", required = false, defaultValue = "20") int radius) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(playerService.listUpc().stream()
                        .map(coordinate -> new Circle(coordinate, color, radius))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/r/player/portals/upv.json")
    public ResponseEntity<CacheableData<List<Coordinate>>> listUpv(
            @RequestParam(value = "excludeCaptured", required = false, defaultValue = "false") boolean excludeCaptured) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(new CacheableData<>(playerService.listUpv(excludeCaptured), Clock.systemUTC().millis()));
    }

    @GetMapping("/r/player/portals/upv.iitc.json")
    public ResponseEntity<List<Circle>> listUpvForIitc(
            @RequestParam(value = "excludeCaptured", required = false, defaultValue = "false") boolean excludeCaptured,
            @RequestParam(value = "color", required = false, defaultValue = "#ff00ff") String color,
            @RequestParam(value = "radius", required = false, defaultValue = "20") int radius) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(playerService.listUpv(excludeCaptured).stream()
                        .map(coordinate -> new Circle(coordinate, color, radius))
                        .collect(Collectors.toList()));
    }

}
