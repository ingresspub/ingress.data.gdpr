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

package ingress.data.gdpr.web.dao;

import static com.google.common.base.Preconditions.checkNotNull;

import ingress.data.gdpr.models.records.CommMention;
import ingress.data.gdpr.models.records.GameLog;
import ingress.data.gdpr.models.records.StorePurchase;
import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.records.UsedDevice;
import ingress.data.gdpr.models.records.ZendeskTicket;
import ingress.data.gdpr.models.records.mission.Mission;
import ingress.data.gdpr.models.records.opr.OprAssignmentLogItem;
import ingress.data.gdpr.models.records.opr.OprProfile;
import ingress.data.gdpr.models.records.opr.OprSkippedLogItem;
import ingress.data.gdpr.models.records.opr.OprSubmissionLogItem;
import ingress.data.gdpr.models.records.profile.AgentProfile;
import ingress.data.gdpr.models.records.profile.Badge;
import ingress.data.gdpr.models.records.profile.PushNotificationFor;
import ingress.data.gdpr.models.records.profile.SmsVerification;
import ingress.data.gdpr.models.records.profile.TutorialState;
import ingress.data.gdpr.models.reports.RawDataReport;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.RawDataParser;

import io.sgr.geometry.Coordinate;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

/**
 * @author SgrAlpha
 */
@Component
public class Importer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Importer.class);
    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final DataSource primaryDataSource;
    private final JdbcTemplate jdbcTemplate;

    private final Executor executor;

    @Autowired
    public Importer(
            @Qualifier("primaryDataSource") @Nonnull final DataSource primaryDataSource,
            @Qualifier("primaryJdbcTemplate") @Nonnull final JdbcTemplate jdbcTemplate) {
        checkNotNull(primaryDataSource, "Missing DataSource");
        this.primaryDataSource = primaryDataSource;
        checkNotNull(jdbcTemplate, "Missing JDBC template");
        this.jdbcTemplate = jdbcTemplate;
        final int cores = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new ThreadFactoryBuilder().setNameFormat("importer-pool-%s").build()
        );
    }

    public void parseAndSaveRawDataReport(final List<Path> files) {
        RawDataReport report = RawDataParser.parse(files);
        if (report == null) {
            LOGGER.warn("Unable to parse from raw data files.");
            return;
        }
        rebuildRawDateTables();

        CompletableFuture
                .allOf(
                        persistAgentProfile(report.getAgentProfile()),
                        persistGameLogs(report.getGameLogs()),
                        persistCommMentions(report.getCommMentions()),
                        persistUsedDevices(report.getUsedDevices()),
                        persistOprProfile(report.getOprProfile()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_opr_agreements(time,portal_id) VALUES(?,?)", report.getOprAgreements()),
                        persistOprAssignmentLogs(report.getOprAssignmentLogs()),
                        persistOprSkippedLogs(report.getOprSkippedLogs()),
                        persistOprSubmissions(report.getOprSubmissionLogs()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_all_portals_approved(time,portal_id) VALUES(?,?)", report.getAllPortalsApproved()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_seer_portals(time,portal_id) VALUES(?,?)", report.getSeerPortals()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_portals_visited(time,portal_id) VALUES(?,?)", report.getPortalsVisited()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_xm_collected(time,value) VALUES(?,?)", report.getXmCollected()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_kilometers_walked(time,value) VALUES(?,?)", report.getKilometersWalked()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_mind_units_controlled(time,value) VALUES(?,?)", report.getMindUnitsControlled()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_mind_units_controlled_active(time,value) VALUES(?,?)", report.getMindUnitsControlledActive()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_fields_created(time,value) VALUES(?,?)", report.getFieldsCreated()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_fields_created_active(time,value) VALUES(?,?)", report.getFieldsCreatedActive()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_links_created(time,value) VALUES(?,?)", report.getLinksCreated()),
                        bulkSaveTimestampedDouble("INSERT INTO gdpr_raw_link_length_in_km(time,value) VALUES(?,?)", report.getLinkLengthInKm()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_links_created_active(time,value) VALUES(?,?)", report.getLinksCreatedActive()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_portals_captured(time,value) VALUES(?,?)", report.getPortalsCaptured()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_portals_owned(time,value) VALUES(?,?)", report.getPortalsOwned()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_resonators_deployed(time,value) VALUES(?,?)", report.getResonatorsDeployed()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_mods_deployed(time,value) VALUES(?,?)", report.getModsDeployed()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_xm_recharged(time,value) VALUES(?,?)", report.getXmRecharged()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_resonators_destroyed(time,value) VALUES(?,?)", report.getResonatorsDestroyed()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_portals_neutralized(time,value) VALUES(?,?)", report.getPortalsNeutralized()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_links_destroyed(time,value) VALUES(?,?)", report.getLinksDestroyed()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_fields_destroyed(time,value) VALUES(?,?)", report.getFieldsDestroyed()),
                        bulkSaveTimestampedDouble("INSERT INTO gdpr_raw_mind_units_times_days_held(time,value) VALUES(?,?)", report.getMindUnitsTimesDaysHeld()),
                        bulkSaveTimestampedDouble("INSERT INTO gdpr_raw_field_held_days(time,value) VALUES(?,?)", report.getFieldHeldDays()),
                        bulkSaveTimestampedDouble("INSERT INTO gdpr_raw_link_length_times_days_held(time,value) VALUES(?,?)", report.getLinkLengthInKmTimesDaysHeld()),
                        bulkSaveTimestampedDouble("INSERT INTO gdpr_raw_link_held_days(time,value) VALUES(?,?)", report.getLinkHeldDays()),
                        bulkSaveTimestampedDouble("INSERT INTO gdpr_raw_portal_held_days(time,value) VALUES(?,?)", report.getPortalHeldDays()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_hacks(time,value) VALUES(?,?)", report.getHacks()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_glyph_hack_points(time,value) VALUES(?,?)", report.getGlyphHackPoints()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_glyph_hack_1_perfect(time,value) VALUES(?,?)", report.getGlyphHackOnePerfect()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_glyph_hack_3_perfect(time,value) VALUES(?,?)", report.getGlyphHackThreePerfect()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_glyph_hack_4_perfect(time,value) VALUES(?,?)", report.getGlyphHackFourPerfect()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_glyph_hack_5_perfect(time,value) VALUES(?,?)", report.getGlyphHackFivePerfect()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_agents_recruited(time,value) VALUES(?,?)", report.getAgentsRecruited()),
                        bulkSaveTimestampedFloat("INSERT INTO gdpr_raw_exo5_control_fields_created(time,value) VALUES(?,?)", report.getExo5ControlFieldsCreated()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_magus_builder_slots_deployed(time,unique_slot_id) VALUES(?,?)", report.getMagusBuilderSlotsDeployed()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_neutralizer_unique_portal_destroyed(time,value) VALUES(?,?)", report.getNeutralizerUniquePortalsDestroyed()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_event_mission_day_points(time,value) VALUES(?,?)", report.getMissionDayPoints()),
                        bulkSaveTimestampedInteger("INSERT INTO gdpr_raw_missions_completed(time,value) VALUES(?,?)", report.getMissionsCompleted()),
                        persistMissionsCreated(report.getMissionsCreated()),
                        persistZendeskTickets(report.getZendeskTickets()),
                        persistStorePurchases(report.getStorePurchases())
                )
                .join();
        LOGGER.info("Finished saving all data into database.");
    }

    private CompletableFuture<Void> persistMissionsCreated(final ReportDetails<List<Mission>> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<Mission> missions = report.getData();
        if (missions.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(null);
    }

    private CompletableFuture<Void> persistAgentProfile(final ReportDetails<AgentProfile> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final AgentProfile profile = report.getData();
        if (profile == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final PushNotificationFor push = profile.getPushNotificationFor();
            SmsVerification smsVerification = profile.getSmsVerification();
            jdbcTemplate.update("INSERT INTO gdpr_raw_agent_profile"
                    + "(email,creation_time,sms_verified,sms_verification_time,tos_accepted_time,invites,"
                    + "agent_name,faction,level,ap,xm,extra_xm,display_stats_to_others,"
                    + "last_loc_latE6,last_loc_lngE6,last_loc_time,"
                    + "email_notification,email_promos,"
                    + "push_for_comm_mention,push_for_portal_attack,push_for_faction_activity,push_for_new_story,push_for_events,"
                    + "has_captured_portal,has_created_link,has_created_field,blocked_agents)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    profile.getEmail(), profile.getCreationTime().toInstant().getEpochSecond(),
                    smsVerification.isVerified(), smsVerification.getVerificationTime().isPresent() ? smsVerification.getVerificationTime().get().toInstant().getEpochSecond() : null,
                    profile.getTosAcceptedTime().toInstant().getEpochSecond(), profile.getInvites(),
                    profile.getAgentName(), profile.getFaction(), profile.getAgentLevel(), profile.getAp(), profile.getXm(), profile.getExtraXm().orElse(null),
                    profile.isDisplayStatsToOthers(), profile.getLastLocation().getLatE6(), profile.getLastLocation().getLngE6(), profile.getLastLocationTime().toInstant().getEpochSecond(),
                    profile.enabledEmailNotification(), profile.enabledEmailPromos(),
                    push.enabledCommMention(), push.enabledPortalAttack(), push.enabledFactionActivity(), push.enabledNewStory(), push.enabledEvents(),
                    profile.hasCapturedPortal(), profile.hasCreatedLink(), profile.hasCreatedField(), profile.getBlockedAgents().orElse(null));
            final List<Badge> badges = new ArrayList<>(profile.getBadges());
            jdbcTemplate.batchUpdate("INSERT INTO gdpr_raw_agent_profile_badges(name,level,time) VALUES(?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                            final Badge badge = badges.get(i);
                            ps.setString(1, badge.getName());
                            ps.setString(2, badge.getLevel().name());
                            ps.setLong(3, badge.getTime().toInstant().getEpochSecond());
                        }

                        @Override public int getBatchSize() {
                            return badges.size();
                        }
                    });
            final List<Map.Entry<String, Integer>> inventory = new ArrayList<>(profile.getInventory().entrySet());
            jdbcTemplate.batchUpdate("INSERT INTO gdpr_raw_agent_inventory(item,count) VALUES(?,?)", new BatchPreparedStatementSetter() {
                @Override public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final Map.Entry<String, Integer> item = inventory.get(i);
                    ps.setString(1, item.getKey());
                    ps.setInt(2, item.getValue());
                }

                @Override public int getBatchSize() {
                    return inventory.size();
                }
            });
            final List<Map.Entry<String, Integer>> mediaIdList = new ArrayList<>(profile.getHighestMediaIdByCategory().entrySet());
            jdbcTemplate.batchUpdate("INSERT INTO gdpr_raw_agent_highest_media_id_by_category(category,media_id) VALUES(?,?)", new BatchPreparedStatementSetter() {
                @Override public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final Map.Entry<String, Integer> entry = mediaIdList.get(i);
                    ps.setString(1, entry.getKey());
                    ps.setInt(2, entry.getValue());
                }

                @Override public int getBatchSize() {
                    return mediaIdList.size();
                }
            });
            List<TutorialState> states = profile.getTutorialState();
            jdbcTemplate.batchUpdate("INSERT INTO gdpr_raw_agent_tutorial_state(name,state,time) VALUES(?,?,?)", new BatchPreparedStatementSetter() {
                @Override public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final TutorialState state = states.get(i);
                    ps.setString(1, state.getName());
                    ps.setString(2, state.getState());
                    ps.setLong(3, state.getTime().toInstant().getEpochSecond());
                }

                @Override public int getBatchSize() {
                    return states.size();
                }
            });
        });
    }

    private CompletableFuture<Void> persistStorePurchases(final ReportDetails<List<StorePurchase>> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<StorePurchase> purchases = report.getData();
        if (purchases.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_store_purchases(time,transaction_type,item,cmu_balance,transaction_description) VALUES(?,?,?,?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<StorePurchase>> batches = Lists.partition(new ArrayList<>(purchases), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final StorePurchase record = batch.get(i);
                    ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                    ps.setString(2, record.getTransactionType());
                    ps.setString(3, record.getItem());
                    if (record.getCmuBalance().isPresent()) {
                        ps.setInt(4, record.getCmuBalance().get());
                    } else {
                        ps.setNull(4, Types.INTEGER);
                    }
                    ps.setString(5, record.getTransactionDescription());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} store purchases.", purchases.size());
        }, executor);
    }

    private CompletableFuture<Void> persistZendeskTickets(final ReportDetails<List<ZendeskTicket>> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<ZendeskTicket> tickets = report.getData();
        if (tickets.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_zendesk_tickets(time,subject,comment) VALUES(?,?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<ZendeskTicket>> batches = Lists.partition(new ArrayList<>(tickets), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final ZendeskTicket record = batch.get(i);
                    ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                    ps.setString(2, record.getSubject());
                    ps.setString(3, record.getComment());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} Zendesk tickets.", tickets.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprSubmissions(final ReportDetails<List<OprSubmissionLogItem>> oprSubmissions) {
        if (oprSubmissions == null || !oprSubmissions.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<OprSubmissionLogItem> submissions = oprSubmissions.getData();
        if (submissions.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_opr_submissions"
                + "(candidate_id,assigned_time,comment,rating_for_cultural,rating_for_text,is_duplicate,duplicate_to,rating_for_location,"
                + "suggested_loc_latE6,suggested_loc_lngE6,rating_for_quality,rating_for_safety,is_one_star,"
                + "submission_time,rating_for_uniqueness,what_is_it)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<OprSubmissionLogItem>> batches = Lists.partition(new ArrayList<>(submissions), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final OprSubmissionLogItem record = batch.get(i);
                    ps.setString(1, record.getCandidateId());
                    ps.setLong(2, record.getAssignedTime().toInstant().getEpochSecond());
                    ps.setString(3, record.getComment().orElse(null));
                    if (record.getRatingForCultural().isPresent()) {
                        ps.setInt(4, record.getRatingForCultural().get());
                    } else {
                        ps.setNull(4, Types.INTEGER);
                    }
                    if (record.getRatingForText().isPresent()) {
                        ps.setInt(5, record.getRatingForText().get());
                    } else {
                        ps.setNull(5, Types.INTEGER);
                    }
                    ps.setBoolean(6, record.isDuplicate());
                    ps.setString(7, record.getDuplicateTo().orElse(null));
                    if (record.getRatingForLocation().isPresent()) {
                        ps.setInt(8, record.getRatingForLocation().get());
                    } else {
                        ps.setNull(8, Types.INTEGER);
                    }
                    if (record.getSuggestedLocation().isPresent()) {
                        final Coordinate loc = record.getSuggestedLocation().get();
                        ps.setInt(9, loc.getLatE6());
                        ps.setInt(10, loc.getLngE6());
                    } else {
                        ps.setNull(9, Types.INTEGER);
                        ps.setNull(10, Types.INTEGER);
                    }
                    if (record.getRatingForQuality().isPresent()) {
                        ps.setInt(11, record.getRatingForQuality().get());
                    } else {
                        ps.setNull(11, Types.INTEGER);
                    }
                    if (record.getRatingForSafety().isPresent()) {
                        ps.setInt(12, record.getRatingForSafety().get());
                    } else {
                        ps.setNull(12, Types.INTEGER);
                    }
                    ps.setBoolean(13, record.isOneStar());
                    ps.setLong(14, record.getSubmissionTime().toInstant().getEpochSecond());
                    if (record.getRatingForUniqueness().isPresent()) {
                        ps.setInt(15, record.getRatingForUniqueness().get());
                    } else {
                        ps.setNull(15, Types.INTEGER);
                    }
                    ps.setString(16, record.getWhatIsIt().orElse(null));
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} OPR submissions.", submissions.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprAssignmentLogs(final ReportDetails<List<OprAssignmentLogItem>> oprAssignments) {
        if (oprAssignments == null || !oprAssignments.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<OprAssignmentLogItem> assignments = oprAssignments.getData();
        if (assignments.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_opr_assignments(candidate_id,time) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<OprAssignmentLogItem>> batches = Lists.partition(new ArrayList<>(assignments), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final OprAssignmentLogItem record = batch.get(i);
                    ps.setString(1, record.getCandidateId());
                    ps.setLong(2, record.getTime().toInstant().getEpochSecond());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} OPR assignment logs.", assignments.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprSkippedLogs(final ReportDetails<List<OprSkippedLogItem>> oprSkippedLogs) {
        if (oprSkippedLogs == null || !oprSkippedLogs.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<OprSkippedLogItem> assignments = oprSkippedLogs.getData();
        if (assignments.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final String sql = "INSERT INTO gdpr_raw_opr_skipped_logs(candidate_id,time) VALUES(?,?)";
        return CompletableFuture.runAsync(() -> {
            final List<List<OprSkippedLogItem>> batches = Lists.partition(new ArrayList<>(assignments), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final OprSkippedLogItem record = batch.get(i);
                    ps.setString(1, record.getCandidateId());
                    ps.setLong(2, record.getTime().toInstant().getEpochSecond());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} OPR skipped logs.", assignments.size());
        }, executor);
    }

    private CompletableFuture<Void> persistOprProfile(final ReportDetails<OprProfile> oprProfile) {
        if (oprProfile == null || !oprProfile.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final OprProfile profile = oprProfile.getData();
        return CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO gdpr_raw_opr_profile("
                    + "email,bonus_last_change_time,bonus_loc_latE6,bonus_loc_lngE6,account_creation_time,"
                    + "total_analyzed,portal_created,portal_rejected,hometown_changed_times,hometown_last_changed_time,hometown_loc_latE6,hometown_loc_lngE6,"
                    + "last_activity_loc_latE6,last_activity_loc_lngE6,language,last_login_time,performance,"
                    + "quiz_status,quiz_time_taken,training_completion_time)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            final Optional<ZonedDateTime> quizTimeTaken = profile.getQuizTimeTaken();
            jdbcTemplate.update(sql,
                    profile.getEmailAddress(),
                    profile.getBonusLastChangedTime().toInstant().getEpochSecond(), profile.getBonusLocation().getLatE6(), profile.getBonusLocation().getLngE6(),
                    profile.getAccountCreationTime().toInstant().getEpochSecond(), profile.getTotalAnalyzed(), profile.getPortalCreated(), profile.getPortalRejected(),
                    profile.getHometownChangedTimes(),
                    profile.getHometownLastChangedTime().isPresent() ? profile.getHometownLastChangedTime().get().toInstant().getEpochSecond() : null,
                    profile.getHometownLocation().getLatE6(), profile.getHometownLocation().getLngE6(),
                    profile.getLastActivityLocation().getLatE6(), profile.getLastActivityLocation().getLngE6(),
                    profile.getLanguage(), profile.getLastLoginTime().toInstant().getEpochSecond(), profile.getPerformance(),
                    profile.getQuizStatus(), quizTimeTaken.isPresent() ? quizTimeTaken.get().toInstant().getEpochSecond() : null,
                    profile.getTrainingCompletionTime().toInstant().getEpochSecond()
            );
            LOGGER.info("Saved OPR profile.");
        }, executor);
    }

    private CompletableFuture<Void> persistUsedDevices(final ReportDetails<List<UsedDevice>> usedDevices) {
        if (usedDevices == null || !usedDevices.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<UsedDevice> devices = usedDevices.getData();
        if (devices.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            jdbcTemplate.batchUpdate("INSERT INTO gdpr_raw_used_devices(name) VALUES(?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    UsedDevice device = devices.get(i);
                    ps.setString(1, device.getDeviceName());
                }

                @Override public int getBatchSize() {
                    return devices.size();
                }
            });
            LOGGER.info("Saved {} used devices.", devices.size());
        }, executor);
    }

    private CompletableFuture<Void> persistCommMentions(final ReportDetails<List<CommMention>> commMentions) {
        if (commMentions == null || !commMentions.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<CommMention> mentions = commMentions.getData();
        if (mentions.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO gdpr_raw_comm_mentions(time,secured,from_agent,message) VALUES(?,?,?,?)";
            final List<List<CommMention>> batches = Lists.partition(new ArrayList<>(mentions), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final CommMention mention = batch.get(i);
                    ps.setLong(1, mention.getTime().toInstant().getEpochSecond());
                    ps.setBoolean(2, mention.isSecured());
                    ps.setString(3, mention.getFrom());
                    ps.setString(4, mention.getMessage());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} comm mentions.", mentions.size());
        }, executor);
    }

    private CompletableFuture<Void> persistGameLogs(final ReportDetails<List<GameLog>> gameLogs) {
        if (gameLogs == null || !gameLogs.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<GameLog> logs = gameLogs.getData();
        if (logs.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO gdpr_raw_game_logs(time,loc_latE6,loc_lngE6,tracker_trigger,comment) VALUES(?,?,?,?,?)";
            final List<List<GameLog>> batches = Lists.partition(new ArrayList<>(logs), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final GameLog log = batch.get(i);
                    ps.setLong(1, log.getTime().toInstant().getEpochSecond());
                    final Optional<Coordinate> loc = log.getLocation();
                    if (loc.isPresent()) {
                        ps.setInt(2, loc.get().getLatE6());
                        ps.setInt(3, loc.get().getLngE6());
                    } else {
                        ps.setInt(2, Types.INTEGER);
                        ps.setInt(3, Types.INTEGER);
                    }
                    ps.setString(4, log.getTrackerTrigger());
                    ps.setString(5, log.getComments());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} game logs into database.", logs.size());
        }, executor);
    }

    private CompletableFuture<Void> bulkSaveTimestampedInteger(final String sql, final ReportDetails<List<TimestampedRecord<Integer>>> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Integer>> records = report.getData();
        if (records.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Integer>>> batches = Lists.partition(new ArrayList<>(records), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final TimestampedRecord<Integer> record = batch.get(i);
                    ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                    ps.setInt(2, record.getValue());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} records.", records.size());
        }, executor);
    }

    private CompletableFuture<Void> bulkSaveTimestampedFloat(final String sql, final ReportDetails<List<TimestampedRecord<Float>>> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Float>> records = report.getData();
        if (records.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Float>>> batches = Lists.partition(new ArrayList<>(records), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final TimestampedRecord<Float> record = batch.get(i);
                    ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                    ps.setFloat(2, record.getValue());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} records.", records.size());
        }, executor);
    }

    private CompletableFuture<Void> bulkSaveTimestampedDouble(final String sql, final ReportDetails<List<TimestampedRecord<Double>>> report) {
        if (report == null || !report.isOk()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<TimestampedRecord<Double>> records = report.getData();
        if (records.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> {
            final List<List<TimestampedRecord<Double>>> batches = Lists.partition(new ArrayList<>(records), DEFAULT_BATCH_SIZE);
            batches.forEach(batch -> jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@SuppressWarnings("NullableProblems") final PreparedStatement ps, final int i) throws SQLException {
                    final TimestampedRecord<Double> record = batch.get(i);
                    ps.setLong(1, record.getTime().toInstant().getEpochSecond());
                    ps.setDouble(2, record.getValue());
                }

                @Override public int getBatchSize() {
                    return batch.size();
                }
            }));
            LOGGER.info("Saved {} records.", records.size());
        }, executor);
    }

    private void rebuildRawDateTables() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("db/rebuild_raw_tables.sql"));
        databasePopulator.execute(primaryDataSource);
    }

}
