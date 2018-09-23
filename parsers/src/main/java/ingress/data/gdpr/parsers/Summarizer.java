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

import ingress.data.gdpr.models.DeviceRecord;
import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.CombatReport;
import ingress.data.gdpr.models.reports.HealthReport;
import ingress.data.gdpr.models.reports.MentoringReport;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.models.reports.SummarizedReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author SgrAlpha
 */
public class Summarizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Summarizer.class);

    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefaultInstance();

    public static SummarizedReport summarize(final List<Path> files) {
        notNull(files, "Missing files");
        final SummarizedReport report = new SummarizedReport();
        try {
            CompletableFuture
                    .allOf(
                            parseUsedDevices(report, files),
                            parseCombatReport(report, files),
                            parseMentoringReport(report, files),
                            parseHealthReport(report, files)
                    )
                    .get(10, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return report;
    }

    private static CompletableFuture<SummarizedReport> parseCombatReport(
            final SummarizedReport report, final List<Path> files) {
        final CombatReport combatReport = new CombatReport();
        return CompletableFuture
                .allOf(
                        parseResonatorsDestroyed(combatReport, files),
                        parsePortalsNeutralized(combatReport, files),
                        parseLinkssDestroyed(combatReport, files),
                        parseFieldsDestroyed(combatReport, files)
                )
                .thenApplyAsync(v -> {
                    report.setCombat(combatReport);
                    return report;
                });
    }

    private static CompletableFuture<CombatReport> parseResonatorsDestroyed(
            final CombatReport report, final List<Path> files) {
        final String reportName = "resonators_destroyed";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefaultInstance());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setResonatorsDestroyed(details);
                    return report;
                });
    }

    private static CompletableFuture<CombatReport> parsePortalsNeutralized(
            final CombatReport report, final List<Path> files) {
        final String reportName = "portals_neutralized";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefaultInstance());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setPortalsNeutralized(details);
                    return report;
                });
    }

    private static CompletableFuture<CombatReport> parseLinkssDestroyed(
            final CombatReport report, final List<Path> files) {
        final String reportName = "link_destroyed";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefaultInstance());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setLinksDestroyed(details);
                    return report;
                });
    }

    private static CompletableFuture<CombatReport> parseFieldsDestroyed(
            final CombatReport report, final List<Path> files) {
        final String reportName = "fields_destroyed";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefaultInstance());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setFieldsDestroyed(details);
                    return report;
                });
    }

    private static CompletableFuture<SummarizedReport> parseUsedDevices(
            final SummarizedReport report, final List<Path> files) {
        final String reportName = "devices";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final DeviceRecordsParser parser = new DeviceRecordsParser();
                    final ReportDetails<List<DeviceRecord>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setUsedDevices(details.getData());
                    return report;
                });
    }

    private static CompletableFuture<SummarizedReport> parseMentoringReport(
            final SummarizedReport report, final List<Path> files) {
        final String reportName = "agents_recruited";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefaultInstance());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setMentoring(new MentoringReport(details));
                    return report;
                });
    }

    private static CompletableFuture<SummarizedReport> parseHealthReport(
            final SummarizedReport report, final List<Path> files) {
        final String reportName = "kilometers_walked";
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(reportName))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", reportName);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefaultInstance());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", reportName, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), reportName);
                    report.setHealth(new HealthReport(details));
                    return report;
                });
    }

}
