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

import static ingress.data.gdpr.parsers.utils.DataFileNames.FIELD_HELD_DAYS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_HELD_DAYS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.MIND_UNITS_TIMES_DAYS_HELD_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.PORTAL_HELD_DAYS_TSV;

import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.DefenseReport;
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
public class DefenseReportParser implements MultipleFilesParser<DefenseReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefenseReportParser.class);

    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private final Executor executor;

    public DefenseReportParser() {
        final int cores = Runtime.getRuntime().availableProcessors();
        this.executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(5)
        );
    }

    @Override public CompletableFuture<DefenseReport> parse(final List<Path> files) {
        final DefenseReport report = new DefenseReport();
        return CompletableFuture
                .allOf(
                        parsePortalHeldDays(report, files),
                        parseLinkHeldDays(report, files),
                        parseLinkLengthInKmTimesDaysHeld(report, files),
                        parseFieldHeldDays(report, files),
                        parseMindUnitsTimesDaysHeld(report, files)
                )
                .thenApplyAsync(unused -> report);
    }

    private CompletableFuture<?> parsePortalHeldDays(
            final DefenseReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(PORTAL_HELD_DAYS_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", PORTAL_HELD_DAYS_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Double> parser = new NumericBasedRecordParser<>(TIME_PARSER, DoubleValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Double>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", PORTAL_HELD_DAYS_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), PORTAL_HELD_DAYS_TSV);
                    report.setPortalHeldDays(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<?> parseLinkHeldDays(
            final DefenseReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(LINK_HELD_DAYS_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", LINK_HELD_DAYS_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Double> parser = new NumericBasedRecordParser<>(TIME_PARSER, DoubleValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Double>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", LINK_HELD_DAYS_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), LINK_HELD_DAYS_TSV);
                    report.setLinkHeldDays(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<?> parseLinkLengthInKmTimesDaysHeld(
            final DefenseReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Double> parser = new NumericBasedRecordParser<>(TIME_PARSER, DoubleValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Double>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), LINK_LENGTH__IN_KILOMETERS_TIMES_DAYS_HELD_TSV);
                    report.setLinkLengthInKmTimesDaysHeld(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<?> parseFieldHeldDays(
            final DefenseReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(FIELD_HELD_DAYS_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", FIELD_HELD_DAYS_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Double> parser = new NumericBasedRecordParser<>(TIME_PARSER, DoubleValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Double>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", FIELD_HELD_DAYS_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), FIELD_HELD_DAYS_TSV);
                    report.setFieldHeldDays(details);
                    return report;
                }, executor);
    }

    private CompletableFuture<?> parseMindUnitsTimesDaysHeld(
            final DefenseReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().equals(MIND_UNITS_TIMES_DAYS_HELD_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", MIND_UNITS_TIMES_DAYS_HELD_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Double> parser = new NumericBasedRecordParser<>(TIME_PARSER, DoubleValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Double>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", MIND_UNITS_TIMES_DAYS_HELD_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), MIND_UNITS_TIMES_DAYS_HELD_TSV);
                    report.setMindUnitsTimesDaysHeld(details);
                    return report;
                }, executor);
    }

}
