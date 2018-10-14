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
 *
 */

(function (window) {
    "use strict";

    let dataCache = {};

    dataCache.put = function (key, value) {
        dataCache[key] = value;
    };

    dataCache.get = function (key) {
        return dataCache[key];
    };

    let app = window.app || {
        contextPath: '/',
        player: {
            portals: {}
        },
        map: {
            provider: 'google',
            apiKey: undefined,
            coordinatesSystem: 'gcj',
            area: {
                ne: {
                    lat: 39.974901,
                    lng: 116.541648
                },
                sw: {
                    lat: 39.902643,
                    lng: 116.253256
                },
                center: {
                    lat: 39.938529,
                    lng: 116.397153
                }
            },
            zoom: 13
        },
        utils: {}
    };

    app.setup = function (opt) {
        app.contextPath = opt.contextPath || '/';
        app.map = opt.map || {};
        app.map.provider = app.map.provider || undefined;
        app.map.apiKey = app.map.apiKey || undefined;
        app.map.coordinatesSystem = app.map.coordinatesSystem || 'gcj';
    };

    app.listUpc = function () {
        let url = app.contextPath + 'r/player/portals/upc.json';
        app.utils.WebUtil.getJson(url, function (result) {
            if (!result || !result.lastUpdatedTimeMs || !result.data || result.data.length < 1) {
                app._onError({type: 'no_data'});
                return;
            }
            dataCache.put('upc.result', result);
            dataCache.put('upc.last_updated', result.lastUpdatedTimeMs ? new Date(result.lastUpdatedTimeMs) : new Date());
            if (app.map.provider === 'google') {
                app.map.onReady = function () {
                    let dataLastUpdatedTimeMs = result.lastUpdatedTimeMs;
                    let updatedTimeItem = $('#player-upc-lastUpdatedTime');
                    let timeStr = updatedTimeItem.text();
                    let uiLastUpdatedTimeMs = timeStr || timeStr !== '' ? parseInt(timeStr, 10) : 0;
                    if (dataLastUpdatedTimeMs <= uiLastUpdatedTimeMs) {
                        return;
                    }
                    result.lastUpdatedTimeLocal = new Date(result.lastUpdatedTimeMs).toLocaleString();
                    updatedTimeItem.attr('datetime', result.lastUpdatedTimeLocal).html(result.lastUpdatedTimeMs);
                    app.utils.MapUtil.renderCoordinatesAsCluster(result.data);
                };
                let script = document.createElement('script');
                script.src = 'https://maps.googleapis.com/maps/api/js?key=' + app.map.apiKey + '&v=3.exp&libraries=visualization&callback=onMapReady';
                document.getElementsByTagName('head')[0].appendChild(script);
            }
        });
    };

    app.listUpv = function () {
        let url = app.contextPath + 'r/player/portals/upv.json';
        app.utils.WebUtil.getJson(url, function (result) {
            if (!result || !result.lastUpdatedTimeMs || !result.data || result.data.length < 1) {
                app._onError({type: 'no_data'});
                return;
            }
            dataCache.put('upv.result', result);
            dataCache.put('upv.last_updated', result.lastUpdatedTimeMs ? new Date(result.lastUpdatedTimeMs) : new Date());
            if (app.map.provider === 'google') {
                app.map.onReady = function () {
                    let dataLastUpdatedTimeMs = result.lastUpdatedTimeMs;
                    let updatedTimeItem = $('#player-upv-lastUpdatedTime');
                    let timeStr = updatedTimeItem.text();
                    let uiLastUpdatedTimeMs = timeStr || timeStr !== '' ? parseInt(timeStr, 10) : 0;
                    if (dataLastUpdatedTimeMs <= uiLastUpdatedTimeMs) {
                        return;
                    }
                    result.lastUpdatedTimeLocal = new Date(result.lastUpdatedTimeMs).toLocaleString();
                    updatedTimeItem.attr('datetime', result.lastUpdatedTimeLocal).html(result.lastUpdatedTimeMs);
                    app.utils.MapUtil.renderCoordinatesAsCluster(result.data);
                };
                let script = document.createElement('script');
                script.src = 'https://maps.googleapis.com/maps/api/js?key=' + app.map.apiKey + '&v=3.exp&libraries=visualization&callback=onMapReady';
                document.getElementsByTagName('head')[0].appendChild(script);
            }
        });
    };

    app.utils.MapUtil = {
        renderCoordinatesAsCluster: function (data) {
            if (!data || data.length < 1) {
                return;
            }
            let bounds = new google.maps.LatLngBounds();
            let markers = [];
            data.forEach(function(loc) {
                let marker = app.utils.MapUtil.renderMarkerOnGoogleMap(loc);
                if (marker) {
                    bounds.extend(marker.getPosition());
                    markers.push(marker);
                }
            });
            app.map.instance.fitBounds(bounds);
            let imagePath = app.contextPath + 'images/map/cluster/markercluster_';
            new MarkerClusterer(app.map.instance, markers, { averageCenter: true, minimumClusterSize: 10, imagePath: imagePath });
        },
        renderMarkerOnGoogleMap: function (loc) {
            if (!loc || !loc.lat || !loc.lng) {
                return undefined;
            }
            let fixedLoc = 'gcj' === app.map.coordinatesSystem && wgs2gcj ? wgs2gcj(loc.lat, loc.lng) : loc;
            let fixedLatLng = new google.maps.LatLng(fixedLoc.lat, fixedLoc.lng);
            let iconImage = app.contextPath + 'images/map/po.png';
            return new google.maps.Marker({
                position: fixedLatLng,
                icon: {
                    url: iconImage,
                    size: new google.maps.Size(18, 18),
                    origin: new google.maps.Point(0,0),
                    anchor: new google.maps.Point(8, 8)
                }
            });
        },
        getZoom: function () {
            if (app.map.provider === 'google') {
                return app.map.instance.getZoom();
            }
            return undefined;
        },
        getBoundary: function () {
            if (!app.map) {
                return undefined;
            }
            if (app.map.provider === 'google') {
                let gMap = app.map.instance;
                if (!gMap) {
                    return undefined;
                }
                let ne = gMap.getBounds().getNorthEast();
                let sw = gMap.getBounds().getSouthWest();
                let center = gMap.getCenter();
                return {
                    zoom: app.utils.MapUtil.getZoom(),
                    area: {
                        ne: gcj2wgs(ne.lat(), ne.lng()),
                        sw: gcj2wgs(sw.lat(), sw.lng()),
                        center: gcj2wgs(center.lat(), center.lng())
                    }
                };
            }
            return undefined;
        },
        saveCurrentMapBounds: function (bounds) {
            if (!bounds) {
                return;
            }
            if (bounds.zoom) {
                app.utils.WebUtil.setCookie('ingress.map.zoom', bounds.zoom, 30, app.contextPath);
            }
            if (bounds.area) {
                app.utils.WebUtil.setCookie('ingress.map.area', JSON.stringify(bounds.area), 30, app.contextPath);
            }
        },
        loadCurrentMapBounds: function () {
            let area, zoom;
            if (app.utils.WebUtil.hasCookie('ingress.map.area')) {
                area = JSON.parse(app.utils.WebUtil.getCookie('ingress.map.area'));
            }
            if (!area) {
                area = {
                    ne: {
                        lat: 39.974901,
                        lng: 116.541648
                    },
                    sw: {
                        lat: 39.902643,
                        lng: 116.253256
                    },
                    center: {
                        lat: 39.938529,
                        lng: 116.397153
                    }
                };
            }

            if (app.utils.WebUtil.hasCookie('ingress.map.zoom')) {
                zoom = parseInt(app.utils.WebUtil.getCookie('ingress.map.zoom'));
            }
            if (!zoom) {
                zoom = 13;
            }
            return {
                area: area,
                zoom: zoom
            }
        }
    };

    app.utils.WebUtil = {
        setCookie: function (cname, cvalue, exdays, path) {
            let d = new Date();
            d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
            let expires = 'expires=' + d.toUTCString();
            let p = path && path !== '' ? path : '/';
            document.cookie = cname + '=' + encodeURIComponent(cvalue) + '; ' + expires + '; path=' + p;
        },
        hasCookie: function (cname) {
            let name = cname + '=';
            let ca = document.cookie.split(';');
            for (let i = 0; i < ca.length; i++) {
                let c = ca[i];
                while (c.charAt(0) === ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) !== -1) {
                    return true;
                }
            }
            return false;
        },
        getCookie: function (cname) {
            let name = cname + '=';
            let ca = document.cookie.split(';');
            for (let i = 0; i < ca.length; i++) {
                let c = ca[i];
                while (c.charAt(0) === ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) !== -1) {
                    return decodeURIComponent(c.substring(name.length, c.length));
                }
            }
            return '';
        },
        removeCookie: function (cname) {
            this.setCookie(cname, '', -1);
        },
        getJson: function (url, next) {
            next = next || function () {
            };
            if ('caches' in window) {
                /*
                 * Check if the service worker has already cached this city's weather
                 * data. If the service worker has the data, then display the cached
                 * data while the app fetches the latest data.
                 */
                caches.match(url).then(function (response) {
                    if (response) {
                        response.json().then(function updateFromCache(json) {
                            next(json);
                        });
                    } else {
                        app.utils.WebUtil._getJson(url, next);
                    }
                });
                return;
            }
            app.utils.WebUtil._getJson(url, next);
        },
        _getJson: function (url, next) {
            next = next || function () {
            };
            $.ajax({
                url: url,
                method: 'GET',
                dataType: 'json',
                headers: {
                    Accept: "application/json"
                },
                jsonp: false,
                success: next,
                error: function (xhr) {
                    if (xhr.status === 401) {
                        document.location.reload();
                    } else if (xhr.status === 404) {
                        next(undefined);
                    } else if (xhr.status >= 500) {
                        next(undefined);
                    }
                }
            });
        }
    };

    app._onError = function (err) {
    };

    window.app = app;

    if (navigator.serviceWorker) {
        navigator.serviceWorker.register('/service-worker.js', {scope: '/'})
            .then(function (registration) {
                console.log('ServiceWorker registration successful with scope:', registration.scope);
            })
            .catch(function (error) {
                console.log('ServiceWorker registration failed:', error);
            });
    }

})(window);

function onMapReady() {
    let last = app.utils.MapUtil.loadCurrentMapBounds();
    if (app.map.provider === 'google') {
        let zoom = app.map.zoom;
        if (last && last.zoom) {
            zoom = last.zoom;
        }
        let area = app.map.area;
        if (last && last.area) {
            area = last.area;
        }
        let ne = wgs2gcj(area.ne.lat, area.ne.lng);
        let sw = wgs2gcj(area.sw.lat, area.sw.lng);
        let bounds = new google.maps.LatLngBounds();
        bounds.extend(new google.maps.LatLng(ne.lat, ne.lng));
        bounds.extend(new google.maps.LatLng(sw.lat, sw.lng));
        let center = bounds.getCenter();
        let map_options = {
            mapTypeControl: false,
            zoomControl: true,
            zoomControlOptions: {
                style: google.maps.ZoomControlStyle.LARGE,
                position: google.maps.ControlPosition.RIGHT_CENTER
            },
            streetViewControl: false,
            panControl: false,
            panControlOptions: {
                position: google.maps.ControlPosition.RIGHT_BOTTOM
            },
            scaleControl: true,
            center: center,
            zoom: zoom
        };
        let gMap = new google.maps.Map($('#map-canvas')[0], map_options);
        app.map.instance = gMap;
        google.maps.event.addListener(gMap, 'idle', function (event) {
            let boundary = app.utils.MapUtil.getBoundary();
            // let zoom = app._getZoom();
            app.utils.MapUtil.saveCurrentMapBounds(boundary);
        });
        // google.maps.event.addListener(gMap, 'click', function () {
        //     if (app.map.infowindow) {
        //         app.map.infowindow.close();
        //     }
        // });
    }
    if (app.map.onReady) {
        app.map.onReady.call(undefined);
    }
}
