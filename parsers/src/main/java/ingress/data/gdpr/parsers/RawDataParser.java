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

package ingress.data.gdpr.parsers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static ingress.data.gdpr.parsers.utils.DataFileNames.AGENTS_RECRUITED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.ALL_PORTALS_APPROVED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.COMM_MENTIONS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.DEVICES_TXT;
import static ingress.data.gdpr.parsers.utils.DataFileNames.EXO_5_CONTROLLER_FIELDS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_CREATED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELD_HELD_DAYS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GAME_LOG_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_1_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_3_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_4_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_5_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_POINTS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.HACKS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.KILOMETERS_WALKED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINKS_CREATED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINKS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_HELD_DAYS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_LENGTH_IN_KILOMETERS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MAGNUS_BUILDER_SLOTS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MIND_UNITS_CONTROLLED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MIND_UNITS_CONTROLLED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MIND_UNITS_TIMES_DAYS_HELD_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MISSIONS_COMPLETED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MISSIONS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MISSION_DAY_POINTS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MODS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.NEUTRALIZER_UNIQUE_PORTALS_NEUTRALIZED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.OPR_AGREEMENTS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.OPR_ASSIGNMENT_LOG_CSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.OPR_PROFILE_CSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.OPR_SKIPPED_LOG_CSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.OPR_SUBMISSION_LOG_CSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_CAPTURED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_NEUTRALIZED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_OWNED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_VISITED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTAL_HELD_DAYS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PROFILE_TXT;
import static ingress.data.gdpr.parsers.utils.DataFileNames.RESONATORS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.RESONATORS_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.SEER_PORTALS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.STORE_PURCHASES_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.XM_COLLECTED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.XM_RECHARGED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.ZENDESK_RECORDS_TSV;

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
import ingress.data.gdpr.models.reports.RawDataReport;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.impl.AgentProfileParser;
import ingress.data.gdpr.parsers.impl.CommMentionParser;
import ingress.data.gdpr.parsers.impl.DeviceRecordsParser;
import ingress.data.gdpr.parsers.impl.GameLogParser;
import ingress.data.gdpr.parsers.impl.MissionsParser;
import ingress.data.gdpr.parsers.impl.OprAssignmentLogsParser;
import ingress.data.gdpr.parsers.impl.OprProfileParser;
import ingress.data.gdpr.parsers.impl.OprSkippedLogsParser;
import ingress.data.gdpr.parsers.impl.OprSubmissionLogsParser;
import ingress.data.gdpr.parsers.impl.StorePurchasesParser;
import ingress.data.gdpr.parsers.impl.TimestampedDataFileParser;
import ingress.data.gdpr.parsers.impl.ZendeskTicketParser;
import ingress.data.gdpr.parsers.utils.DoubleValueParser;
import ingress.data.gdpr.parsers.utils.FloatValueParser;
import ingress.data.gdpr.parsers.utils.IntegerValueParser;
import ingress.data.gdpr.parsers.utils.SingleLineValueParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author SgrAlpha
 */
public class RawDataParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(RawDataParser.class);

    public static RawDataReport parse(final List<Path> files) {
        checkNotNull(files, "Missing files");
        checkArgument(!files.isEmpty(), "No file to parse from");
        final int cores = Runtime.getRuntime().availableProcessors();
        final Executor executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(files.size())
        );

        final RawDataReport report = new RawDataReport();
        final List<CompletableFuture<RawDataReport>> futuresList = new LinkedList<>();
        files
                .stream()
                .filter(dataFile -> Objects.nonNull(dataFile) && Objects.nonNull(dataFile.getFileName()))
                .forEach(dataFile -> futuresList.add(locateAndParse(dataFile, report, executor)));
        try {
            CompletableFuture
                    .allOf(futuresList.toArray(new CompletableFuture[0]))
                    .get(10, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return report;
    }

    private static CompletableFuture<RawDataReport> locateAndParse(
            final Path dataFile, final RawDataReport report, final Executor executor) {
        checkNotNull(dataFile, "Data file needs to be specified");
        final Path fileNamePath = dataFile.getFileName();
        // fix: Maven find-bug plugin keep complaining about NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE
        if (fileNamePath == null) {
            return CompletableFuture.completedFuture(report);
        }
        final String fileName = fileNamePath.toString();
        switch (fileName) {
            case PROFILE_TXT:
                return parseAgentProfile(dataFile, executor)
                        .thenApplyAsync(report::setAgentProfile);
            case DEVICES_TXT:
                return parseUsedDevices(dataFile, executor)
                        .thenApplyAsync(report::setUsedDevices);
            case GAME_LOG_TSV:
                return parseGameLogs(dataFile, executor)
                        .thenApplyAsync(report::setGameLogs);
            case COMM_MENTIONS_TSV:
                return parseCommMention(dataFile, executor)
                        .thenApplyAsync(report::setCommMentions);
            case OPR_PROFILE_CSV:
                return parseOprProfile(dataFile, executor)
                        .thenApplyAsync(report::setOprProfile);
            case OPR_AGREEMENTS_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setOprAgreements);
            case OPR_SKIPPED_LOG_CSV:
                return parseOprSkippedLogs(dataFile, executor)
                        .thenApplyAsync(report::setOprSkippedLogs);
            case OPR_ASSIGNMENT_LOG_CSV:
                return parseOprAssignmentLogs(dataFile, executor)
                        .thenApplyAsync(report::setOprAssignmentLogs);
            case OPR_SUBMISSION_LOG_CSV:
                return parseOprSubmissionLogs(dataFile, executor)
                        .thenApplyAsync(report::setOprSubmissionLogs);
            case ALL_PORTALS_APPROVED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setAllPortalsApproved);
            case SEER_PORTALS_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setSeerPortals);
            case PORTALS_VISITED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setPortalsVisited);
            case XM_COLLECTED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setXmCollected);
            case KILOMETERS_WALKED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setKilometersWalked);
            case MIND_UNITS_CONTROLLED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setMindUnitsControlled);
            case MIND_UNITS_CONTROLLED_ACTIVE_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setMindUnitsControlledActive);
            case FIELDS_CREATED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setFieldsCreated);
            case FIELDS_CREATED_ACTIVE_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setFieldsCreatedActive);
            case LINKS_CREATED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setLinksCreated);
            case LINK_LENGTH_IN_KILOMETERS_TSV:
                return parseTimestampDataFileWith(dataFile, DoubleValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setLinkLengthInKm);
            case LINKS_CREATED_ACTIVE_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setLinksCreatedActive);
            case PORTALS_CAPTURED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setPortalsCaptured);
            case PORTALS_OWNED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setPortalsOwned);
            case RESONATORS_DEPLOYED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setResonatorsDeployed);
            case MODS_DEPLOYED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setModsDeployed);
            case XM_RECHARGED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setXmRecharged);
            case RESONATORS_DESTROYED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setResonatorsDestroyed);
            case PORTALS_NEUTRALIZED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setPortalsNeutralized);
            case LINK_DESTROYED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setLinksDestroyed);
            case FIELDS_DESTROYED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setFieldsDestroyed);
            case PORTAL_HELD_DAYS_TSV:
                return parseTimestampDataFileWith(dataFile, DoubleValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setPortalHeldDays);
            case LINK_HELD_DAYS_TSV:
                return parseTimestampDataFileWith(dataFile, DoubleValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setLinkHeldDays);
            case LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV:
                return parseTimestampDataFileWith(dataFile, DoubleValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setLinkLengthInKmTimesDaysHeld);
            case FIELD_HELD_DAYS_TSV:
                return parseTimestampDataFileWith(dataFile, DoubleValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setFieldHeldDays);
            case MIND_UNITS_TIMES_DAYS_HELD_TSV:
                return parseTimestampDataFileWith(dataFile, DoubleValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setMindUnitsTimesDaysHeld);
            case HACKS_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setHacks);
            case GLYPH_HACK_POINTS_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setGlyphHackPoints);
            case GLYPH_HACK_1_PERFECT_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setGlyphHackOnePerfect);
            case GLYPH_HACK_3_PERFECT_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setGlyphHackThreePerfect);
            case GLYPH_HACK_4_PERFECT_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setGlyphHackFourPerfect);
            case GLYPH_HACK_5_PERFECT_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setGlyphHackFivePerfect);
            case AGENTS_RECRUITED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setAgentsRecruited);
            case EXO_5_CONTROLLER_FIELDS_CREATED_TSV:
                return parseTimestampDataFileWith(dataFile, FloatValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setExo5ControlFieldsCreated);
            case MAGNUS_BUILDER_SLOTS_DEPLOYED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setMagusBuilderSlotsDeployed);
            case NEUTRALIZER_UNIQUE_PORTALS_NEUTRALIZED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setNeutralizerUniquePortalsDestroyed);
            case MISSION_DAY_POINTS_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setMissionDayPoints);
            case MISSIONS_COMPLETED_TSV:
                return parseTimestampDataFileWith(dataFile, IntegerValueParser.getDefault(), executor)
                        .thenApplyAsync(report::setMissionsCompleted);
            case MISSIONS_TSV:
                return parseMissionsCreated(dataFile, executor)
                        .thenApplyAsync(report::setMissionsCreated);
            case ZENDESK_RECORDS_TSV:
                return parseZendeskTickets(dataFile, executor)
                        .thenApplyAsync(report::setZendeskTickets);
            case STORE_PURCHASES_TSV:
                return parseStorePurchases(dataFile, executor)
                        .thenApplyAsync(report::setStorePurchases);
            default:
                LOGGER.warn("Unsupported data file: {}", fileName);
                return CompletableFuture.completedFuture(report);
        }
    }

    private static CompletableFuture<ReportDetails<AgentProfile>> parseAgentProfile(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<AgentProfile> details = AgentProfileParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<GameLog>>> parseGameLogs(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<GameLog>> details = GameLogParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<CommMention>>> parseCommMention(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<CommMention>> details = CommMentionParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<UsedDevice>>> parseUsedDevices(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<UsedDevice>> details = DeviceRecordsParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<OprProfile>> parseOprProfile(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<OprProfile> details = OprProfileParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<Mission>>> parseMissionsCreated(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<Mission>> details = MissionsParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<ZendeskTicket>>> parseZendeskTickets(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<ZendeskTicket>> details = ZendeskTicketParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<StorePurchase>>> parseStorePurchases(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<StorePurchase>> details = StorePurchasesParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<OprAssignmentLogItem>>> parseOprAssignmentLogs(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<OprAssignmentLogItem>> details = OprAssignmentLogsParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<OprSkippedLogItem>>> parseOprSkippedLogs(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<OprSkippedLogItem>> details = OprSkippedLogsParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static CompletableFuture<ReportDetails<List<OprSubmissionLogItem>>> parseOprSubmissionLogs(
            final Path dataFile, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<OprSubmissionLogItem>> details = OprSubmissionLogsParser.getDefault().parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

    private static <T> CompletableFuture<ReportDetails<List<TimestampedRecord<T>>>> parseTimestampDataFileWith(
            final Path dataFile, final SingleLineValueParser<T> valueParser, final Executor executor) {
        return CompletableFuture
                .supplyAsync(() -> {
                    final ReportDetails<List<TimestampedRecord<T>>> details = new TimestampedDataFileParser<>(valueParser).parse(dataFile);
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", dataFile.getFileName(), details.getError());
                    }
                    return details;
                }, executor);
    }

}
