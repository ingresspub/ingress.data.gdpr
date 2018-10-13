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

CREATE TABLE IF NOT EXISTS `gdpr_user_preferences` (
  `key`   VARCHAR(32)  NOT NULL PRIMARY KEY,
  `value` VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS `gdpr_raw_game_logs` (
  `id`              BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time`            BIGINT NOT NULL,
  `loc_latE6`       INT,
  `loc_lngE6`       INT,
  `tracker_trigger` VARCHAR(64),
  `comment`         TEXT
);

CREATE TABLE IF NOT EXISTS `gdpr_raw_comm_mentions` (
  `id`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time`       BIGINT      NOT NULL,
  `secured`    BOOLEAN     NOT NULL,
  `from_agent` VARCHAR(16) NOT NULL,
  `message`    TEXT
);

CREATE TABLE IF NOT EXISTS `gdpr_raw_agent_profile_badges` (
  `name`  VARCHAR(32) NOT NULL,
  `level` VARCHAR(8)  NOT NULL,
  `time`  BIGINT      NOT NULL
);
