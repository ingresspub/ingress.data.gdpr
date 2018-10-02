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

package ingress.data.gdpr.parsers;

import static ingress.data.gdpr.models.utils.Preconditions.notNull;
import static ingress.data.gdpr.parsers.utils.DataFileNames.AGENTS_RECRUITED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.ALL_PORTALS_APPROVED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.DEVICES_TXT;
import static ingress.data.gdpr.parsers.utils.DataFileNames.EXO_5_CONTROLLER_FIELDS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_CREATED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELD_HELD_DAYS_TSV;
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
import static ingress.data.gdpr.parsers.utils.DataFileNames.MISSION_DAY_POINTS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MODS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.NEUTRALIZER_UNIQUE_PORTALS_NEUTRALIZED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.OPR_AGREEMENTS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_CAPTURED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_NEUTRALIZED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_OWNED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_VISITED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTAL_HELD_DAYS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.RESONATORS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.RESONATORS_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.SEER_PORTALS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.XM_COLLECTED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.XM_RECHARGED_TSV;

import ingress.data.gdpr.models.records.DeviceRecord;
import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.models.reports.BuildingReport;
import ingress.data.gdpr.models.reports.CombatReport;
import ingress.data.gdpr.models.reports.DefenseReport;
import ingress.data.gdpr.models.reports.DiscoveryReport;
import ingress.data.gdpr.models.reports.EventsReport;
import ingress.data.gdpr.models.reports.HealthReport;
import ingress.data.gdpr.models.reports.MentoringReport;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.models.reports.ResourceGatheringReport;
import ingress.data.gdpr.models.reports.SummarizedReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
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
public class Summarizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Summarizer.class);

    private static Executor executor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(),
            10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50)
    );

    public static SummarizedReport summarize(final List<Path> files) {
        notNull(files, "Missing files");
        if (files.isEmpty()) {
            throw new IllegalArgumentException("No file to summarize from");
        }
        final SummarizedReport report = new SummarizedReport();
        try {
            CompletableFuture
                    .allOf(
                            parseUsedDevices(report, files),
                            parseDiscoveryReport(report, files),
                            CompletableFuture
                                    .supplyAsync(() -> {
                                        final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                                files, KILOMETERS_WALKED_TSV,
                                                TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                        report.setHealth(new HealthReport(details));
                                        return report;
                                    }, executor),
                            parseBuildingReport(report, files),
                            parseCombatReport(report, files),
                            parseDefenseReport(report, files),
                            parseResourceGathering(report, files),
                            CompletableFuture
                                    .supplyAsync(() -> {
                                        final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                                files, AGENTS_RECRUITED_TSV,
                                                TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                        report.setMentoring(new MentoringReport(details));
                                        return report;
                                    }, executor),
                            parseEventsReport(report, files)
                    )
                    .get(10, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return report;
    }

    private static CompletableFuture<SummarizedReport> parseUsedDevices(
            final SummarizedReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(DEVICES_TXT))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", DEVICES_TXT);
                        return report;
                    }
                    final DeviceRecordsParser parser = new DeviceRecordsParser();
                    final ReportDetails<List<DeviceRecord>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", DEVICES_TXT, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), DEVICES_TXT);
                    report.setUsedDevices(details.getData());
                    return report;
                }, executor);
    }

    private static CompletableFuture<SummarizedReport> parseDiscoveryReport(
            final SummarizedReport report, final List<Path> files) {
        final DiscoveryReport discoveryReport = new DiscoveryReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, OPR_AGREEMENTS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    discoveryReport.setOprAgreements(details);
                                    return discoveryReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, ALL_PORTALS_APPROVED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    discoveryReport.setAllPortalsApproved(details);
                                    return discoveryReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, SEER_PORTALS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    discoveryReport.setSeerPortals(details);
                                    return discoveryReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, PORTALS_VISITED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    discoveryReport.setPortalsVisited(details);
                                    return discoveryReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, XM_COLLECTED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    discoveryReport.setXmCollected(details);
                                    return discoveryReport;
                                }, executor)
                        )
                .thenApplyAsync(unused -> {
                    report.setDiscovery(discoveryReport);
                    return report;
                });
    }


        private static CompletableFuture<SummarizedReport> parseBuildingReport(
            final SummarizedReport report, final List<Path> files) {
        final BuildingReport buildingReport = new BuildingReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, MIND_UNITS_CONTROLLED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    buildingReport.setMindUnitsControlled(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, MIND_UNITS_CONTROLLED_ACTIVE_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    buildingReport.setMindUnitsControlledActive(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, FIELDS_CREATED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    buildingReport.setFieldsCreated(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, FIELDS_CREATED_ACTIVE_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    buildingReport.setFieldsCreatedActive(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, LINKS_CREATED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    buildingReport.setLinksCreated(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Double>>> details = ReportParser.parse(
                                            files, LINK_LENGTH_IN_KILOMETERS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), DoubleValueParser.getDefault()));
                                    buildingReport.setLinkLengthInKm(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, LINKS_CREATED_ACTIVE_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    buildingReport.setLinksCreatedActive(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, PORTALS_CAPTURED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    buildingReport.setPortalsCaptured(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, PORTALS_OWNED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    buildingReport.setPortalsOwned(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, RESONATORS_DEPLOYED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    buildingReport.setResonatorsDeployed(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, MODS_DEPLOYED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    buildingReport.setModsDeployed(details);
                                    return buildingReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, XM_RECHARGED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    buildingReport.setXmRecharged(details);
                                    return buildingReport;
                                }, executor)
                )
                .thenApplyAsync(unused -> {
                    report.setBuilding(buildingReport);
                    return report;
                });
    }

    private static CompletableFuture<SummarizedReport> parseCombatReport(
            final SummarizedReport report, final List<Path> files) {
        final CombatReport combatReport = new CombatReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, RESONATORS_DESTROYED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    combatReport.setResonatorsDestroyed(details);
                                    return combatReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, PORTALS_NEUTRALIZED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    combatReport.setPortalsNeutralized(details);
                                    return combatReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, LINK_DESTROYED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    combatReport.setLinksDestroyed(details);
                                    return combatReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, FIELDS_DESTROYED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    combatReport.setFieldsDestroyed(details);
                                    return combatReport;
                                }, executor)
                )
                .thenApplyAsync(unused -> {
                    report.setCombat(combatReport);
                    return report;
                });
    }

    private static CompletableFuture<?> parseDefenseReport(
            final SummarizedReport report, final List<Path> files) {
        final DefenseReport defenseReport = new DefenseReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Double>>> details = ReportParser.parse(
                                            files, PORTAL_HELD_DAYS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), DoubleValueParser.getDefault()));
                                    defenseReport.setPortalHeldDays(details);
                                    return defenseReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Double>>> details = ReportParser.parse(
                                            files, LINK_HELD_DAYS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), DoubleValueParser.getDefault()));
                                    defenseReport.setLinkHeldDays(details);
                                    return defenseReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Double>>> details = ReportParser.parse(
                                            files, LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), DoubleValueParser.getDefault()));
                                    defenseReport.setLinkLengthInKmTimesDaysHeld(details);
                                    return defenseReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Double>>> details = ReportParser.parse(
                                            files, FIELD_HELD_DAYS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), DoubleValueParser.getDefault()));
                                    defenseReport.setFieldHeldDays(details);
                                    return defenseReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Double>>> details = ReportParser.parse(
                                            files, MIND_UNITS_TIMES_DAYS_HELD_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), DoubleValueParser.getDefault()));
                                    defenseReport.setMindUnitsTimesDaysHeld(details);
                                    return defenseReport;
                                }, executor)
                )
                .thenApplyAsync(unused -> {
                    report.setDefense(defenseReport);
                    return report;
                });
    }

    private static CompletableFuture<?> parseResourceGathering(
            final SummarizedReport report, final List<Path> files) {
        final ResourceGatheringReport resourceGatheringReport = new ResourceGatheringReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, HACKS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    resourceGatheringReport.setHacks(details);
                                    return resourceGatheringReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, GLYPH_HACK_POINTS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    resourceGatheringReport.setGlyphHackPoints(details);
                                    return resourceGatheringReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, GLYPH_HACK_1_PERFECT_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    resourceGatheringReport.setGlyphHackOnePerfect(details);
                                    return resourceGatheringReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, GLYPH_HACK_3_PERFECT_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    resourceGatheringReport.setGlyphHackThreePerfect(details);
                                    return resourceGatheringReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, GLYPH_HACK_4_PERFECT_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    resourceGatheringReport.setGlyphHackFourPerfect(details);
                                    return resourceGatheringReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, GLYPH_HACK_5_PERFECT_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    resourceGatheringReport.setGlyphHackFivePerfect(details);
                                    return resourceGatheringReport;
                                }, executor)
                )
                .thenApplyAsync(unused -> {
                    report.setResourceGathering(resourceGatheringReport);
                    return report;
                });
    }

    private static CompletableFuture<?> parseEventsReport(
            final SummarizedReport report, final List<Path> files) {
        final EventsReport eventsReport = new EventsReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Float>>> details = ReportParser.parse(
                                            files, EXO_5_CONTROLLER_FIELDS_CREATED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), FloatValueParser.getDefault()));
                                    eventsReport.setExo5ControlFieldsCreated(details);
                                    return eventsReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, MAGNUS_BUILDER_SLOTS_DEPLOYED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    eventsReport.setMagusBuilderSlotsDeployed(details);
                                    return eventsReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, NEUTRALIZER_UNIQUE_PORTALS_NEUTRALIZED_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    eventsReport.setNeutralizerUniquePortalsDestroyed(details);
                                    return eventsReport;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<TimestampedRecord<Integer>>> details = ReportParser.parse(
                                            files, MISSION_DAY_POINTS_TSV,
                                            TimestampedRecordParser.using(ZonedDateTimeParser.getDefault(), IntValueParser.getDefault()));
                                    eventsReport.setMissionDayPoints(details);
                                    return eventsReport;
                                }, executor)
                )
                .thenApplyAsync(unused -> {
                    report.setEvents(eventsReport);
                    return report;
                });
    }

}
