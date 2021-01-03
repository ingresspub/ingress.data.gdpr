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

DROP TABLE IF EXISTS `gdpr_raw_agent_profile`;

CREATE TABLE `gdpr_raw_agent_profile` (
  `email`                     VARCHAR(128) NOT NULL,
  `creation_time`             BIGINT       NOT NULL,
  `sms_verified`              BOOLEAN      NOT NULL,
  `sms_verification_time`     BIGINT,
  `tos_accepted_time`         BIGINT       NOT NULL,
  `invites`                   VARCHAR(32)  NOT NULL,
  `agent_name`                VARCHAR(16)  NOT NULL,
  `faction`                   VARCHAR(16)  NOT NULL,
  `level`                     INT          NOT NULL,
  `ap`                        INT          NOT NULL,
  `xm`                        INT          NOT NULL,
  `extra_xm`                  INT,
  `display_stats_to_others`   BOOLEAN      NOT NULL,
  `last_loc_latE6`            INT          NOT NULL,
  `last_loc_lngE6`            INT          NOT NULL,
  `last_loc_time`             BIGINT       NOT NULL,
  `email_notification`        BOOLEAN      NOT NULL,
  `email_promos`              BOOLEAN      NOT NULL,
  `push_for_comm_mention`     BOOLEAN      NOT NULL,
  `push_for_portal_attack`    BOOLEAN      NOT NULL,
  `push_for_faction_activity` BOOLEAN      NOT NULL,
  `push_for_new_story`        BOOLEAN      NOT NULL,
  `push_for_events`           BOOLEAN      NOT NULL,
  `has_captured_portal`       BOOLEAN      NOT NULL,
  `has_created_link`          BOOLEAN      NOT NULL,
  `has_created_field`         BOOLEAN      NOT NULL,
  `blocked_agents`            TEXT
);

DROP TABLE IF EXISTS `gdpr_raw_agent_profile_badges`;

CREATE TABLE `gdpr_raw_agent_profile_badges` (
  `name`  VARCHAR(32) NOT NULL,
  `level` VARCHAR(8)  NOT NULL,
  `time`  BIGINT      NOT NULL
);

CREATE INDEX `gdpr_raw_agent_profile_badges_name`
  ON `gdpr_raw_agent_profile_badges` (`name`);

CREATE INDEX `gdpr_raw_agent_profile_badges_level`
  ON `gdpr_raw_agent_profile_badges` (`level`);

DROP TABLE IF EXISTS `gdpr_raw_agent_inventory`;

CREATE TABLE `gdpr_raw_agent_inventory` (
  `item`  VARCHAR(32) NOT NULL,
  `count` INT         NOT NULL
);

CREATE INDEX `gdpr_raw_agent_inventory_item`
  ON `gdpr_raw_agent_inventory` (`item`);

DROP TABLE IF EXISTS `gdpr_raw_agent_highest_media_id_by_category`;

CREATE TABLE `gdpr_raw_agent_highest_media_id_by_category` (
  `category` VARCHAR(16) NOT NULL,
  `media_id` INT         NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_agent_tutorial_state`;

CREATE TABLE `gdpr_raw_agent_tutorial_state` (
  `name`  VARCHAR(64)  NOT NULL,
  `state` VARCHAR(256) NOT NULL,
  `time`  BIGINT       NOT NULL
);

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
  `id`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `time`       BIGINT      NOT NULL,
  `secured`    BOOLEAN     NOT NULL,
  `from_agent` VARCHAR(16) NOT NULL,
  `message`    TEXT
);

CREATE INDEX `gdpr_raw_comm_mentions_time`
  ON `gdpr_raw_comm_mentions` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_used_devices`;

CREATE TABLE `gdpr_raw_used_devices` (
  `name` VARCHAR(64)
);

DROP TABLE IF EXISTS `gdpr_raw_opr_profile`;

CREATE TABLE `gdpr_raw_opr_profile` (
  `email`                      VARCHAR(128),
  `bonus_last_change_time`     BIGINT NOT NULL,
  `bonus_loc_latE6`            INT    NOT NULL,
  `bonus_loc_lngE6`            INT    NOT NULL,
  `account_creation_time`      BIGINT NOT NULL,
  `total_analyzed`             INT    NOT NULL DEFAULT 0,
  `portal_created`             INT    NOT NULL DEFAULT 0,
  `portal_rejected`            INT    NOT NULL DEFAULT 0,
  `hometown_changed_times`     INT    NOT NULL DEFAULT 0,
  `hometown_last_changed_time` BIGINT,
  `hometown_loc_latE6`         INT    NOT NULL,
  `hometown_loc_lngE6`         INT    NOT NULL,
  `last_activity_loc_latE6`    INT    NOT NULL,
  `last_activity_loc_lngE6`    INT    NOT NULL,
  `language`                   VARCHAR(32),
  `last_login_time`            BIGINT NOT NULL,
  `performance`                VARCHAR(8),
  `quiz_status`                VARCHAR(16),
  `quiz_time_taken`            BIGINT,
  `training_completion_time`   BIGINT NOT NULL
);

DROP TABLE IF EXISTS `gdpr_raw_opr_agreements`;

CREATE TABLE `gdpr_raw_opr_agreements` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_opr_agreements_time`
  ON `gdpr_raw_opr_agreements` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_opr_assignments`;

CREATE TABLE `gdpr_raw_opr_assignments` (
  `candidate_id` CHAR(32) NOT NULL,
  `time`         BIGINT   NOT NULL
);

CREATE INDEX `gdpr_raw_opr_assignments_candidate_id`
  ON `gdpr_raw_opr_assignments` (`candidate_id`);

CREATE INDEX `gdpr_raw_opr_assignments_time`
  ON `gdpr_raw_opr_assignments` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_opr_skipped_logs`;

CREATE TABLE `gdpr_raw_opr_skipped_logs` (
  `candidate_id` CHAR(32) NOT NULL,
  `time`         BIGINT   NOT NULL
);

CREATE INDEX `gdpr_raw_opr_skipped_logs_candidate_id`
  ON `gdpr_raw_opr_skipped_logs` (`candidate_id`);

CREATE INDEX `gdpr_raw_opr_skipped_logs_time`
  ON `gdpr_raw_opr_skipped_logs` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_opr_submissions`;

CREATE TABLE `gdpr_raw_opr_submissions` (
  `id`                    BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `candidate_id`          CHAR(32) NOT NULL,
  `assigned_time`         BIGINT   NOT NULL,
  `comment`               TEXT,
  `rating_for_cultural`   INT,
  `rating_for_text`       INT,
  `is_duplicate`          BOOLEAN  NOT NULL,
  `duplicate_to`          VARCHAR(35),
  `rating_for_location`   INT,
  `suggested_loc_latE6`   INT,
  `suggested_loc_lngE6`   INT,
  `rating_for_quality`    INT,
  `rating_for_safety`     INT,
  `is_one_star`           BOOLEAN  NOT NULL,
  `submission_time`       BIGINT   NOT NULL,
  `rating_for_uniqueness` INT,
  `what_is_it`            TEXT
);

CREATE INDEX `gdpr_raw_opr_submissions_candidate_id`
  ON `gdpr_raw_opr_submissions` (`candidate_id`);

CREATE INDEX `gdpr_raw_opr_submissions_assigned_time`
  ON `gdpr_raw_opr_submissions` (`assigned_time`);

CREATE INDEX `gdpr_raw_opr_submissions_rating_for_cultural`
  ON `gdpr_raw_opr_submissions` (`rating_for_cultural`);

CREATE INDEX `gdpr_raw_opr_submissions_rating_for_text`
  ON `gdpr_raw_opr_submissions` (`rating_for_text`);

CREATE INDEX `gdpr_raw_opr_submissions_rating_for_location`
  ON `gdpr_raw_opr_submissions` (`rating_for_location`);

CREATE INDEX `gdpr_raw_opr_submissions_rating_for_quality`
  ON `gdpr_raw_opr_submissions` (`rating_for_quality`);

CREATE INDEX `gdpr_raw_opr_submissions_rating_for_safety`
  ON `gdpr_raw_opr_submissions` (`rating_for_safety`);

CREATE INDEX `gdpr_raw_opr_submissions_rating_for_uniqueness`
  ON `gdpr_raw_opr_submissions` (`rating_for_uniqueness`);

CREATE INDEX `gdpr_raw_opr_submissions_is_duplicate`
  ON `gdpr_raw_opr_submissions` (`is_duplicate`);

CREATE INDEX `gdpr_raw_opr_submissions_duplicate_to`
  ON `gdpr_raw_opr_submissions` (`duplicate_to`);

CREATE INDEX `gdpr_raw_opr_submissions_suggested_loc_latE6`
  ON `gdpr_raw_opr_submissions` (`suggested_loc_latE6`);

CREATE INDEX `gdpr_raw_opr_submissions_suggested_loc_lngE6`
  ON `gdpr_raw_opr_submissions` (`suggested_loc_lngE6`);

CREATE INDEX `gdpr_raw_opr_submissions_is_one_star`
  ON `gdpr_raw_opr_submissions` (`is_one_star`);

CREATE INDEX `gdpr_raw_opr_submissions_submission_time`
  ON `gdpr_raw_opr_submissions` (`submission_time`);

DROP TABLE IF EXISTS `gdpr_raw_all_portals_approved`;

CREATE TABLE `gdpr_raw_all_portals_approved` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_all_portals_approved_time`
  ON `gdpr_raw_all_portals_approved` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_seer_portals`;

CREATE TABLE `gdpr_raw_seer_portals` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_seer_portals_time`
  ON `gdpr_raw_seer_portals` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_portals_visited`;

CREATE TABLE `gdpr_raw_portals_visited` (
  `time`      BIGINT NOT NULL,
  `portal_id` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_portals_visited_time`
  ON `gdpr_raw_portals_visited` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_xm_collected`;

CREATE TABLE `gdpr_raw_xm_collected` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_xm_collected_time`
  ON `gdpr_raw_xm_collected` (`time`);

CREATE INDEX `gdpr_raw_xm_collected_value`
  ON `gdpr_raw_xm_collected` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_kilometers_walked`;

CREATE TABLE `gdpr_raw_kilometers_walked` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_kilometers_walked_time`
  ON `gdpr_raw_kilometers_walked` (`time`);

CREATE INDEX `gdpr_raw_kilometers_walked_value`
  ON `gdpr_raw_kilometers_walked` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_mind_units_controlled`;

CREATE TABLE `gdpr_raw_mind_units_controlled` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_mind_units_controlled_time`
  ON `gdpr_raw_mind_units_controlled` (`time`);

CREATE INDEX `gdpr_raw_mind_units_controlled_value`
  ON `gdpr_raw_mind_units_controlled` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_mind_units_controlled_active`;

CREATE TABLE `gdpr_raw_mind_units_controlled_active` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_mind_units_controlled_active_time`
  ON `gdpr_raw_mind_units_controlled_active` (`time`);

CREATE INDEX `gdpr_raw_mind_units_controlled_active_value`
  ON `gdpr_raw_mind_units_controlled_active` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_fields_created`;

CREATE TABLE `gdpr_raw_fields_created` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_fields_created_time`
  ON `gdpr_raw_fields_created` (`time`);

CREATE INDEX `gdpr_raw_fields_created_value`
  ON `gdpr_raw_fields_created` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_fields_created_active`;

CREATE TABLE `gdpr_raw_fields_created_active` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_fields_created_active_time`
  ON `gdpr_raw_fields_created_active` (`time`);

CREATE INDEX `gdpr_raw_fields_created_active_value`
  ON `gdpr_raw_fields_created_active` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_links_created`;

CREATE TABLE `gdpr_raw_links_created` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_links_created_time`
  ON `gdpr_raw_links_created` (`time`);

CREATE INDEX `gdpr_raw_links_created_value`
  ON `gdpr_raw_links_created` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_link_length_in_km`;

CREATE TABLE `gdpr_raw_link_length_in_km` (
  `time`  BIGINT NOT NULL,
  `value` DOUBLE NOT NULL
);

CREATE INDEX `gdpr_raw_link_length_in_km_time`
  ON `gdpr_raw_link_length_in_km` (`time`);

CREATE INDEX `gdpr_raw_link_length_in_km_value`
  ON `gdpr_raw_link_length_in_km` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_links_created_active`;

CREATE TABLE `gdpr_raw_links_created_active` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_links_created_active_time`
  ON `gdpr_raw_links_created_active` (`time`);

CREATE INDEX `gdpr_raw_links_created_active_value`
  ON `gdpr_raw_links_created_active` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_portals_captured`;

CREATE TABLE `gdpr_raw_portals_captured` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_portals_captured_time`
  ON `gdpr_raw_portals_captured` (`time`);

CREATE INDEX `gdpr_raw_portals_captured_value`
  ON `gdpr_raw_portals_captured` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_portals_owned`;

CREATE TABLE `gdpr_raw_portals_owned` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_portals_owned_time`
  ON `gdpr_raw_portals_owned` (`time`);

CREATE INDEX `gdpr_raw_portals_owned_value`
  ON `gdpr_raw_portals_owned` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_resonators_deployed`;

CREATE TABLE `gdpr_raw_resonators_deployed` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_resonators_deployed_time`
  ON `gdpr_raw_resonators_deployed` (`time`);

CREATE INDEX `gdpr_raw_resonators_deployed_value`
  ON `gdpr_raw_resonators_deployed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_mods_deployed`;

CREATE TABLE `gdpr_raw_mods_deployed` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_mods_deployed_time`
  ON `gdpr_raw_mods_deployed` (`time`);

CREATE INDEX `gdpr_raw_mods_deployed_value`
  ON `gdpr_raw_mods_deployed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_xm_recharged`;

CREATE TABLE `gdpr_raw_xm_recharged` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_xm_recharged_time`
  ON `gdpr_raw_xm_recharged` (`time`);

CREATE INDEX `gdpr_raw_xm_recharged_value`
  ON `gdpr_raw_xm_recharged` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_resonators_destroyed`;

CREATE TABLE `gdpr_raw_resonators_destroyed` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_resonators_destroyed_time`
  ON `gdpr_raw_resonators_destroyed` (`time`);

CREATE INDEX `gdpr_raw_resonators_destroyed_value`
  ON `gdpr_raw_resonators_destroyed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_portals_neutralized`;

CREATE TABLE `gdpr_raw_portals_neutralized` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_portals_neutralized_time`
  ON `gdpr_raw_portals_neutralized` (`time`);

CREATE INDEX `gdpr_raw_portals_neutralized_value`
  ON `gdpr_raw_portals_neutralized` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_links_destroyed`;

CREATE TABLE `gdpr_raw_links_destroyed` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_links_destroyed_time`
  ON `gdpr_raw_links_destroyed` (`time`);

CREATE INDEX `gdpr_raw_links_destroyed_value`
  ON `gdpr_raw_links_destroyed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_fields_destroyed`;

CREATE TABLE `gdpr_raw_fields_destroyed` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_fields_destroyed_time`
  ON `gdpr_raw_fields_destroyed` (`time`);

CREATE INDEX `gdpr_raw_fields_destroyed_value`
  ON `gdpr_raw_fields_destroyed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_mind_units_times_days_held`;

CREATE TABLE `gdpr_raw_mind_units_times_days_held` (
  `time`  BIGINT NOT NULL,
  `value` DOUBLE NOT NULL
);

CREATE INDEX `gdpr_raw_mind_units_times_days_held_time`
  ON `gdpr_raw_mind_units_times_days_held` (`time`);

CREATE INDEX `gdpr_raw_mind_units_times_days_held_value`
  ON `gdpr_raw_mind_units_times_days_held` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_field_held_days`;

CREATE TABLE `gdpr_raw_field_held_days` (
  `time`  BIGINT NOT NULL,
  `value` DOUBLE NOT NULL
);

CREATE INDEX `gdpr_raw_field_held_days_time`
  ON `gdpr_raw_field_held_days` (`time`);

CREATE INDEX `gdpr_raw_field_held_days_value`
  ON `gdpr_raw_field_held_days` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_link_length_times_days_held`;

CREATE TABLE `gdpr_raw_link_length_times_days_held` (
  `time`  BIGINT NOT NULL,
  `value` DOUBLE NOT NULL
);

CREATE INDEX `gdpr_raw_link_length_times_days_held_time`
  ON `gdpr_raw_link_length_times_days_held` (`time`);

CREATE INDEX `gdpr_raw_link_length_times_days_held_value`
  ON `gdpr_raw_link_length_times_days_held` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_link_held_days`;

CREATE TABLE `gdpr_raw_link_held_days` (
  `time`  BIGINT NOT NULL,
  `value` DOUBLE NOT NULL
);

CREATE INDEX `gdpr_raw_link_held_days_time`
  ON `gdpr_raw_link_held_days` (`time`);

CREATE INDEX `gdpr_raw_link_held_days_value`
  ON `gdpr_raw_link_held_days` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_portal_held_days`;

CREATE TABLE `gdpr_raw_portal_held_days` (
  `time`  BIGINT NOT NULL,
  `value` DOUBLE NOT NULL
);

CREATE INDEX `gdpr_raw_portal_held_days_time`
  ON `gdpr_raw_portal_held_days` (`time`);

CREATE INDEX `gdpr_raw_portal_held_days_value`
  ON `gdpr_raw_portal_held_days` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_hacks`;

CREATE TABLE `gdpr_raw_hacks` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_hacks_time`
  ON `gdpr_raw_hacks` (`time`);

CREATE INDEX `gdpr_raw_hacks_value`
  ON `gdpr_raw_hacks` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_glyph_hack_points`;

CREATE TABLE `gdpr_raw_glyph_hack_points` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_glyph_hack_points_time`
  ON `gdpr_raw_glyph_hack_points` (`time`);

CREATE INDEX `gdpr_raw_glyph_hack_points_value`
  ON `gdpr_raw_glyph_hack_points` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_glyph_hack_1_perfect`;

CREATE TABLE `gdpr_raw_glyph_hack_1_perfect` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_glyph_hack_1_perfect_time`
  ON `gdpr_raw_glyph_hack_1_perfect` (`time`);

CREATE INDEX `gdpr_raw_glyph_hack_1_perfect_value`
  ON `gdpr_raw_glyph_hack_1_perfect` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_glyph_hack_3_perfect`;

CREATE TABLE `gdpr_raw_glyph_hack_3_perfect` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_glyph_hack_3_perfect_time`
  ON `gdpr_raw_glyph_hack_1_perfect` (`time`);

CREATE INDEX `gdpr_raw_glyph_hack_3_perfect_value`
  ON `gdpr_raw_glyph_hack_1_perfect` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_glyph_hack_4_perfect`;

CREATE TABLE `gdpr_raw_glyph_hack_4_perfect` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_glyph_hack_4_perfect_time`
  ON `gdpr_raw_glyph_hack_1_perfect` (`time`);

CREATE INDEX `gdpr_raw_glyph_hack_4_perfect_value`
  ON `gdpr_raw_glyph_hack_1_perfect` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_glyph_hack_5_perfect`;

CREATE TABLE `gdpr_raw_glyph_hack_5_perfect` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_glyph_hack_5_perfect_time`
  ON `gdpr_raw_glyph_hack_1_perfect` (`time`);

CREATE INDEX `gdpr_raw_glyph_hack_5_perfect_value`
  ON `gdpr_raw_glyph_hack_1_perfect` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_agents_recruited`;

CREATE TABLE `gdpr_raw_agents_recruited` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_agents_recruited_time`
  ON `gdpr_raw_agents_recruited` (`time`);

CREATE INDEX `gdpr_raw_agents_recruited_value`
  ON `gdpr_raw_agents_recruited` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_exo5_control_fields_created`;

CREATE TABLE `gdpr_raw_exo5_control_fields_created` (
  `time`  BIGINT NOT NULL,
  `value` FLOAT  NOT NULL
);

CREATE INDEX `gdpr_raw_exo5_control_fields_created_time`
  ON `gdpr_raw_exo5_control_fields_created` (`time`);

CREATE INDEX `gdpr_raw_exo5_control_fields_created_value`
  ON `gdpr_raw_exo5_control_fields_created` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_magus_builder_slots_deployed`;

CREATE TABLE `gdpr_raw_magus_builder_slots_deployed` (
  `time`           BIGINT NOT NULL,
  `unique_slot_id` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_magus_builder_slots_deployed_time`
  ON `gdpr_raw_magus_builder_slots_deployed` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_neutralizer_unique_portal_destroyed`;

CREATE TABLE `gdpr_raw_neutralizer_unique_portal_destroyed` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_neutralizer_unique_portal_destroyed_time`
  ON `gdpr_raw_neutralizer_unique_portal_destroyed` (`time`);

CREATE INDEX `gdpr_raw_neutralizer_unique_portal_destroyed_value`
  ON `gdpr_raw_neutralizer_unique_portal_destroyed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_event_mission_day_points`;

CREATE TABLE `gdpr_raw_event_mission_day_points` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_event_mission_day_points_time`
  ON `gdpr_raw_event_mission_day_points` (`time`);

CREATE INDEX `gdpr_raw_event_mission_day_points_value`
  ON `gdpr_raw_event_mission_day_points` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_missions_completed`;

CREATE TABLE `gdpr_raw_missions_completed` (
  `time`  BIGINT NOT NULL,
  `value` INT    NOT NULL
);

CREATE INDEX `gdpr_raw_missions_completed_time`
  ON `gdpr_raw_missions_completed` (`time`);

CREATE INDEX `gdpr_raw_missions_completed_value`
  ON `gdpr_raw_missions_completed` (`value`);

DROP TABLE IF EXISTS `gdpr_raw_zendesk_tickets`;

CREATE TABLE `gdpr_raw_zendesk_tickets` (
  `time`    BIGINT NOT NULL,
  `subject` VARCHAR(128),
  `comment` TEXT   NOT NULL
);

CREATE INDEX `gdpr_raw_zendesk_tickets_time`
  ON `gdpr_raw_zendesk_tickets` (`time`);

DROP TABLE IF EXISTS `gdpr_raw_store_purchases`;

CREATE TABLE `gdpr_raw_store_purchases` (
  `time`                    BIGINT       NOT NULL,
  `transaction_type`        VARCHAR(32)  NOT NULL,
  `item`                    VARCHAR(128) NOT NULL,
  `cmu_balance`             INT,
  `transaction_description` TEXT         NOT NULL
);

CREATE INDEX `gdpr_raw_store_purchases_time`
  ON `gdpr_raw_store_purchases` (`time`);

CREATE INDEX `gdpr_raw_store_purchases_transaction_type`
  ON `gdpr_raw_store_purchases` (`transaction_type`);

