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

DROP TABLE IF EXISTS `gdpr_raw_game_logs`;

CREATE TABLE `gdpr_raw_game_logs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time` BIGINT NOT NULL,
  `loc_latE6` INT,
  `loc_lngE6` INT,
  `tracker_trigger` VARCHAR(64),
  `comment` TEXT
);

CREATE INDEX `gdpr_raw_game_logs_time` ON `gdpr_raw_game_logs`(`time`);
CREATE INDEX `gdpr_raw_game_logs_loc_latE6` ON `gdpr_raw_game_logs`(`loc_latE6`);
CREATE INDEX `gdpr_raw_game_logs_loc_lngE6` ON `gdpr_raw_game_logs`(`loc_lngE6`);
CREATE INDEX `gdpr_raw_game_logs_tracker_trigger` ON `gdpr_raw_game_logs`(`tracker_trigger`);

DROP TABLE IF EXISTS `gdpr_raw_comm_mentions`;

CREATE TABLE `gdpr_raw_comm_mentions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time` BIGINT NOT NULL,
  `message` TEXT
);

CREATE INDEX `gdpr_raw_comm_mentions_time` ON `gdpr_raw_comm_mentions`(`time`);
