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

import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELDS_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_DESTROYED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTALS_NEUTRALIZED_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.RESONATORS_DESTROYED_TSV;

import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.CombatReport;
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
public class CombatReportParser implements MultipleFilesParser<CombatReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatReportParser.class);

    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private final Executor executor;

    public CombatReportParser() {
        final int cores = Runtime.getRuntime().availableProcessors();
        this.executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(4)
        );
    }

    @Override public CompletableFuture<CombatReport> parse(final List<Path> dataFiles) {
        final CombatReport combatReport = new CombatReport();
        return CompletableFuture
                .allOf(
                        parseResonatorsDestroyed(combatReport, dataFiles),
                        parsePortalsNeutralized(combatReport, dataFiles),
                        parseLinksDestroyed(combatReport, dataFiles),
                        parseFieldsDestroyed(combatReport, dataFiles)
                )
                .thenApplyAsync(unused -> combatReport);
    }

    private CompletableFuture<CombatReport> parseResonatorsDestroyed(
            final CombatReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(RESONATORS_DESTROYED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", RESONATORS_DESTROYED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", RESONATORS_DESTROYED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), RESONATORS_DESTROYED_TSV);
                    report.setResonatorsDestroyed(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<CombatReport> parsePortalsNeutralized(
            final CombatReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(PORTALS_NEUTRALIZED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", PORTALS_NEUTRALIZED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", PORTALS_NEUTRALIZED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), PORTALS_NEUTRALIZED_TSV);
                    report.setPortalsNeutralized(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<CombatReport> parseLinksDestroyed(
            final CombatReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(LINK_DESTROYED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", LINK_DESTROYED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", LINK_DESTROYED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), LINK_DESTROYED_TSV);
                    report.setLinksDestroyed(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<CombatReport> parseFieldsDestroyed(
            final CombatReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(FIELDS_DESTROYED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", FIELDS_DESTROYED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", FIELDS_DESTROYED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), FIELDS_DESTROYED_TSV);
                    report.setFieldsDestroyed(details);
                    return report;
                }, executor);
    }

}
