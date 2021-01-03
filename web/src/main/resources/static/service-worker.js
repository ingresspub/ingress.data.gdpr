/*
 * Copyright (C) 2014-2021 SgrAlpha
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

let name = 'ingress-data-explorer';
let shellCacheName = name + '-shell';
let dataCacheName = name + '-data';
let version = '20181016';
let fix = '4';
let shellCacheFullName = shellCacheName + '.' + version + '.' + fix;
let dataCacheFullName = dataCacheName + '.' + version + '.' + fix;

let filesToCache = [
    '/android-chrome-192x192.png',
    '/android-chrome-512x512.png',
    '/apple-touch-icon.png',
    '/favicon.ico',
    '/favicon-16x16.png',
    '/favicon-32x32.png',
    '/mstile-150x150.png',
    '/safari-pinned-tab.svg',
    '/images/map/po.png',
    '/images/map/cluster/markercluster_1.png',
    '/images/map/cluster/markercluster_2.png',
    '/images/map/cluster/markercluster_3.png',
    '/images/map/cluster/markercluster_4.png',
    '/images/map/cluster/markercluster_5.png',
    '/javascripts/map/transform.js',
    'https://use.fontawesome.com/releases/v5.4.1/webfonts/fa-solid-900.woff2',
    'https://use.fontawesome.com/releases/v5.4.1/webfonts/fa-brands-400.woff2'
];

let dataEndpoints = [
];

self.addEventListener('install', function (e) {
    console.log('[' + name + '] Install');
    e.waitUntil(
        caches.open(shellCacheFullName).then(function (cache) {
            console.log('[' + name + '] Caching app shell');
            return cache.addAll(filesToCache);
        })
    );
});

self.addEventListener('activate', function (e) {
    console.log('[' + name + '] Activate');
    e.waitUntil(
        caches.keys().then(function (keyList) {
            return Promise.all(keyList.map(function (key) {
                let cache = key.split('.');
                if (cache.length !== 3 || (shellCacheName !== cache[0] && dataCacheName !== cache[0] && name !== cache[0])) {
                    console.log('[' + name + '] Skip removing cache', key);
                    return;
                }
                if (version !== cache[1] || fix !== cache[2]) {
                    console.log('[' + name + '] Removing old cache', key);
                    return caches.delete(key);
                }
            }));
        })
    );
    return self.clients.claim();
});

self.addEventListener('fetch', function (e) {
    let isDataUrl = false;
    for (let i = 0; i < dataEndpoints.length; i++) {
        let path = new URL(e.request.url).pathname;
        if (path.indexOf(dataEndpoints[i]) > -1) {
            isDataUrl = true;
            break;
        }
    }
    if (isDataUrl) {
        /*
         * When the request URL contains dataUrl, the app is asking for fresh
         * weather data. In this case, the service worker always goes to the
         * network and then caches the response. This is called the "Cache then
         * network" strategy:
         * https://jakearchibald.com/2014/offline-cookbook/#cache-then-network
         */
        e.respondWith(
            caches.open(dataCacheFullName).then(function (cache) {
                return fetch(e.request).then(function (response) {
                    console.log('[' + name + '] Cache', e.request.url);
                    cache.put(e.request.url, response.clone());
                    return response;
                });
            })
        );
    } else {
        /*
         * The app is asking for app shell files. In this scenario the app uses the
         * "Cache, falling back to the network" offline strategy:
         * https://jakearchibald.com/2014/offline-cookbook/#cache-falling-back-to-network
         */
        e.respondWith(
            caches.match(e.request).then(function (response) {
                if (!response) {
                    console.log('[' + name + '] Fetch', e.request.url);
                }
                return response || fetch(e.request);
            })
        );
    }
});
