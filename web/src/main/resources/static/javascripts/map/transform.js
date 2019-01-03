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

(function(window) {
    "use strict";

    function _outOfChina(lat, lng) {
		return (lat < 22.446195 && lng > 113.678580 && lng < 114.427582)
		|| (lat < 22.217493 && lat > 22.177243 && lng > 113.528421 && lng < 113.563058)
		|| (lat < 25.401950 && lng < 125.502319 && lat > 21.675348 && lng > 119.827835)
		|| (lng < 72.004) || (lng > 137.8347) || (lat < 0.8293) || (lat > 55.8271);
    }

    function _transformLat(x, y) {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    function _transformLon(x, y) {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    function _delta(lat, lng) {
        var a = 6378137.0;
        var ee = 0.00669342162296594323;
        var dLat = _transformLat(lng - 105.0, lat - 35.0);
        var dLng = _transformLon(lng - 105.0, lat - 35.0);
        var radLat = lat / 180.0 * Math.PI;
        var magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        var sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        return {"lat": dLat, "lng": dLng};
    }

    function wgs2gcj(wgsLat, wgsLng) {
        if (_outOfChina(wgsLat, wgsLng)) {
            return {"lat": wgsLat, "lng": wgsLng};
        }
        var d = _delta(wgsLat, wgsLng);
        return {"lat": wgsLat + d.lat, "lng": wgsLng + d.lng};
    }

    function gcj2wgs(gcjLat, gcjLng) {
        if (_outOfChina(gcjLat, gcjLng)) {
            return {"lat": gcjLat, "lng": gcjLng};
        }
        var d = _delta(gcjLat, gcjLng);
        return {"lat": gcjLat - d.lat, "lng": gcjLng - d.lng};
    }

    function gcj2wgs_exact(gcjLat, gcjLng) {
        if (_outOfChina(gcjLat, gcjLng)) {
            return {"lat": gcjLat, "lng": gcjLng};
        }
        var initDelta = 0.01;
        var threshold = 0.000001;
        var dLat = initDelta, dLng = initDelta;
        var mLat = gcjLat - dLat, mLng = gcjLng - dLng;
        var pLat = gcjLat + dLat, pLng = gcjLng + dLng;
        var wgsLat, wgsLng;
        for (var i = 0; i < 30; i++) {
            wgsLat = (mLat + pLat) / 2;
            wgsLng = (mLng + pLng) / 2;
            var tmp = wgs2gcj(wgsLat, wgsLng);
            dLat = tmp.lat - gcjLat;
            dLng = tmp.lng - gcjLng;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLng) < threshold)) {
                return {"lat": wgsLat, "lng": wgsLng};
            }
            if (dLat > 0) {
                pLat = wgsLat;
            } else {
                mLat = wgsLat;
            }
            if (dLng > 0) {
                pLng = wgsLng;
            } else {
                mLng = wgsLng;
            }
        }
        return {"lat": wgsLat, "lng": wgsLng};
    }

    function distance(latA, lngA, latB, lngB) {
        var earthR = 6378137.0;
        var x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((lngA - lngB) * Math.PI / 180);
        var y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
        var s = x + y;
        if (s > 1) {
            s = 1;
        }
        if (s < -1) {
            s = -1;
        }
        return Math.acos(s) * earthR;
    }

    window.wgs2gcj = wgs2gcj;
    window.gcj2wgs = gcj2wgs;
    window.gcj2wgs_exact = gcj2wgs_exact;
    window.distance = distance;

})(window);
