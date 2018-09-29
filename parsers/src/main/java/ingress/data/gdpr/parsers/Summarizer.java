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
import static ingress.data.gdpr.parsers.utils.DataFileNames.DEVICES_TXT;
import static ingress.data.gdpr.parsers.utils.DataFileNames.KILOMETERS_WALKED_TSV;

import ingress.data.gdpr.models.DeviceRecord;
import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.HealthReport;
import ingress.data.gdpr.models.reports.MentoringReport;
import ingress.data.gdpr.models.reports.ReportDetails;
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

    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private static Executor executor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), 50,
            1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(50)
    );

    public static SummarizedReport summarize(final List<Path> files) {
        notNull(files, "Missing files");
        final SummarizedReport report = new SummarizedReport();
        try {
            CompletableFuture
                    .allOf(
                            parseUsedDevices(report, files),
                            parseBuildingReport(report, files),
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

    private static CompletableFuture<SummarizedReport> parseBuildingReport(
            final SummarizedReport report, final List<Path> files) {
        return new BuildingReportParser().parse(files)
                .thenApplyAsync(buildingReport -> {
                    report.setBuilding(buildingReport);
                    return report;
                });
    }

    private static CompletableFuture<SummarizedReport> parseCombatReport(
            final SummarizedReport report, final List<Path> files) {
        return new CombatReportParser().parse(files)
                .thenApplyAsync(combatReport -> {
                    report.setCombat(combatReport);
                    return report;
                });
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

    private static CompletableFuture<SummarizedReport> parseMentoringReport(
            final SummarizedReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(AGENTS_RECRUITED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", AGENTS_RECRUITED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Integer> parser = new NumericBasedRecordParser<>(TIME_PARSER, IntValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Integer>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", AGENTS_RECRUITED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), AGENTS_RECRUITED_TSV);
                    report.setMentoring(new MentoringReport(details));
                    return report;
                }, executor);
    }

    private static CompletableFuture<SummarizedReport> parseHealthReport(
            final SummarizedReport report, final List<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files.stream()
                            .filter(file -> file.getFileName().toString().startsWith(KILOMETERS_WALKED_TSV))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        LOGGER.warn("Can not find report named '{}', skipping ...", KILOMETERS_WALKED_TSV);
                        return report;
                    }
                    final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
                    if (!details.isOk()) {
                        LOGGER.warn("Ran into error when parsing {}: {}", KILOMETERS_WALKED_TSV, details.getError());
                        return report;
                    }
                    LOGGER.info("Parsed {} records in {}", details.getData().size(), KILOMETERS_WALKED_TSV);
                    report.setHealth(new HealthReport(details));
                    return report;
                }, executor);
    }

}
