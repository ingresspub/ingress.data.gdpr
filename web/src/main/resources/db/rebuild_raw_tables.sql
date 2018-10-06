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
  `id`              BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time`            BIGINT NOT NULL,
  `loc_latE6`       INT,
  `loc_lngE6`       INT,
  `tracker_trigger` VARCHAR(64),
  `comment`         TEXT
);

CREATE INDEX `gdpr_raw_game_logs_time`
  ON `gdpr_raw_game_logs` (`time`);
CREATE INDEX `gdpr_raw_game_logs_loc_latE6`
  ON `gdpr_raw_game_logs` (`loc_latE6`);
CREATE INDEX `gdpr_raw_game_logs_loc_lngE6`
  ON `gdpr_raw_game_logs` (`loc_lngE6`);
CREATE INDEX `gdpr_raw_game_logs_tracker_trigger`
  ON `gdpr_raw_game_logs` (`tracker_trigger`);

DROP TABLE IF EXISTS `gdpr_raw_comm_mentions`;

CREATE TABLE `gdpr_raw_comm_mentions` (
  `id`      BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time`    BIGINT NOT NULL,
  `message` TEXT
);

CREATE INDEX `gdpr_raw_comm_mentions_time`
  ON `gdpr_raw_comm_mentions` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_used_devices`;

CREATE TABLE `gdpr_raw_used_devices` (
  `name` VARCHAR(64)
);

DROP TABLE IF EXISTS `gdpr_raw_opr_profile`;

CREATE TABLE `gdpr_raw_opr_profile` (
  email                      VARCHAR(128),
  bonus_last_change_time     BIGINT NOT NULL,
  bonus_loc_latE6            INT    NOT NULL,
  bonus_loc_lngE6            INT    NOT NULL,
  account_creation_time      BIGINT NOT NULL,
  total_analyzed             INT    NOT NULL DEFAULT 0,
  portal_created             INT    NOT NULL DEFAULT 0,
  portal_rejected            INT    NOT NULL DEFAULT 0,
  hometown_changed_times     INT    NOT NULL DEFAULT 0,
  hometown_last_changed_time BIGINT,
  hometown_loc_latE6         INT    NOT NULL,
  hometown_loc_lngE6         INT    NOT NULL,
  last_activity_loc_latE6    INT    NOT NULL,
  last_activity_loc_lngE6    INT    NOT NULL,
  language                   VARCHAR(32),
  last_login_time            BIGINT NOT NULL,
  performance                VARCHAR(8),
  quiz_status                VARCHAR(16),
  quiz_time_taken            BIGINT NOT NULL,
  training_completion_time   BIGINT NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_opr_agreements`;

CREATE TABLE `gdpr_raw_opr_agreements` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_opr_assignments`;

CREATE TABLE `gdpr_raw_opr_assignments` (
  `candidate_id` CHAR(32) NOT NULL,
  `time`         BIGINT   NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_opr_submissions`;

CREATE TABLE `gdpr_raw_opr_submissions` (
  candidate_id          CHAR(32) NOT NULL,
  assigned_time         BIGINT   NOT NULL,
  comment               TEXT,
  rating_for_cultural   INT,
  rating_for_text       INT,
  is_duplicate          BOOLEAN  NOT NULL,
  duplicate_to          VARCHAR(35),
  rating_for_location   INT,
  suggested_loc_latE6   INT,
  suggested_loc_lngE6   INT,
  rating_for_quality    INT,
  rating_for_safety     INT,
  is_one_star           BOOLEAN  NOT NULL,
  submission_time       BIGINT   NOT NULL,
  rating_for_uniqueness INT,
  what_is_it            TEXT
);

DROP TABLE IF EXISTS `gdpr_raw_all_portals_approved`;

CREATE TABLE `gdpr_raw_all_portals_approved` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_seer_portals`;

CREATE TABLE `gdpr_raw_seer_portals` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_portals_visited`;

CREATE TABLE `gdpr_raw_portals_visited` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_xm_collected`;

CREATE TABLE `gdpr_raw_xm_collected` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

