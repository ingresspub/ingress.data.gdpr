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

import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_CREATED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINKS_CREATED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINKS_CREATED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_LENGTH_IN_KILOMETERS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MIND_UNITS_CONTROLLED_ACTIVE_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MIND_UNITS_CONTROLLED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MODS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_CAPTURED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_OWNED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.RESONATORS_DEPLOYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.XM_RECHARGED_TSV;

import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.BuildingReport;
import ingress.data.gdpr.models.reports.ReportDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author SgrAlpha
 */
public class BuildingReportParser implements MultipleFilesParser<BuildingReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingReportParser.class);

    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private final Executor executor;

    public BuildingReportParser() {
        final int cores = Runtime.getRuntime().availableProcessors();
        this.executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(12)
        );
    }

    @Override public CompletableFuture<BuildingReport> parse(final List<Path> dataFiles) {
        final BuildingReport buildingReport = new BuildingReport();
        return CompletableFuture
                .allOf(
                        parseMindsUnitControlled(buildingReport, dataFiles),
                        parseMindsUnitControlledActive(buildingReport, dataFiles),
                        parseFieldsControlled(buildingReport, dataFiles),
                        parseFieldsControlledActive(buildingReport, dataFiles),
                        parseLinksCreated(buildingReport, dataFiles),
                        parseLinkLengthInKm(buildingReport, dataFiles),
                        parseLinksCreatedActive(buildingReport, dataFiles),
                        parsePortalsCaptured(buildingReport, dataFiles),
                        parsePortalsOwned(buildingReport, dataFiles),
                        parseResonatorsDeployed(buildingReport, dataFiles),
                        parseModsDeployed(buildingReport, dataFiles),
                        parseXmRecharged(buildingReport, dataFiles)
                )
                .thenApplyAsync(unused -> buildingReport);
    }

    private CompletableFuture<BuildingReport> parseMindsUnitControlled(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(MIND_UNITS_CONTROLLED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", MIND_UNITS_CONTROLLED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", MIND_UNITS_CONTROLLED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), MIND_UNITS_CONTROLLED_TSV);
                    report.setMindUnitsControlled(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseMindsUnitControlledActive(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(MIND_UNITS_CONTROLLED_ACTIVE_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", MIND_UNITS_CONTROLLED_ACTIVE_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", MIND_UNITS_CONTROLLED_ACTIVE_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), MIND_UNITS_CONTROLLED_ACTIVE_TSV);
                    report.setMindUnitsControlledActive(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseFieldsControlled(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(FIELDS_CREATED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", FIELDS_CREATED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", FIELDS_CREATED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), FIELDS_CREATED_TSV);
                    report.setFieldsCreated(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseFieldsControlledActive(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(FIELDS_CREATED_ACTIVE_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", FIELDS_CREATED_ACTIVE_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", FIELDS_CREATED_ACTIVE_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), FIELDS_CREATED_ACTIVE_TSV);
                    report.setFieldsCreatedActive(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseLinksCreated(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(LINKS_CREATED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", LINKS_CREATED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", LINKS_CREATED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), LINKS_CREATED_TSV);
                    report.setLinksCreated(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseLinkLengthInKm(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(LINK_LENGTH_IN_KILOMETERS_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", LINK_LENGTH_IN_KILOMETERS_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Double> parser = new NumericBasedRecordParser<>(TIME_PARSER, DoubleValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Double>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", LINK_LENGTH_IN_KILOMETERS_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), LINK_LENGTH_IN_KILOMETERS_TSV);
                    report.setLinkLengthInKm(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseLinksCreatedActive(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(LINKS_CREATED_ACTIVE_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", LINKS_CREATED_ACTIVE_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", LINKS_CREATED_ACTIVE_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), LINKS_CREATED_ACTIVE_TSV);
                    report.setLinksCreatedActive(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parsePortalsCaptured(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(PORTALS_CAPTURED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", PORTALS_CAPTURED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", PORTALS_CAPTURED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), PORTALS_CAPTURED_TSV);
                    report.setPortalsCaptured(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parsePortalsOwned(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(PORTALS_OWNED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", PORTALS_OWNED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", PORTALS_OWNED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), PORTALS_OWNED_TSV);
                    report.setPortalsOwned(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseResonatorsDeployed(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(RESONATORS_DEPLOYED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", RESONATORS_DEPLOYED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", RESONATORS_DEPLOYED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), RESONATORS_DEPLOYED_TSV);
                    report.setResonatorsDeployed(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseModsDeployed(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(MODS_DEPLOYED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", MODS_DEPLOYED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", MODS_DEPLOYED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), MODS_DEPLOYED_TSV);
                    report.setModsDeployed(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<BuildingReport> parseXmRecharged(
            final BuildingReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(XM_RECHARGED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", XM_RECHARGED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", XM_RECHARGED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), XM_RECHARGED_TSV);
                    report.setXmRecharged(details);
                    return report;
                }, executor);
    }

}
