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

import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_1_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_2_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_3_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_4_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_5_PERFECT_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.GLYPH_HACK_POINTS_TSV;
import static ingress.data.gdpr.parsers.utils.DataFileNames.HACKS_TSV;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.FILE_NOT_FOUND;

import ingress.data.gdpr.models.NumericBasedRecord;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.models.reports.ResourceGatheringReport;
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
public class ResourceGatheringReportParser implements MultipleFilesParser<ResourceGatheringReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceGatheringReportParser.class);

    private static final ZonedDateTimeParser TIME_PARSER = ZonedDateTimeParser.getDefault();

    private final Executor executor;

    public ResourceGatheringReportParser() {
        final int cores = Runtime.getRuntime().availableProcessors();
        this.executor = new ThreadPoolExecutor(
                cores, cores,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(7)
        );
    }

    @Override public CompletableFuture<ResourceGatheringReport> parse(final List<Path> files) {
        final ResourceGatheringReport report = new ResourceGatheringReport();
        return CompletableFuture
                .allOf(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(HACKS_TSV ,files);
                                    report.setHacks(details);
                                    return report;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(GLYPH_HACK_POINTS_TSV, files);
                                    report.setGlyphHackPoints(details);
                                    return report;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(GLYPH_HACK_1_PERFECT_TSV, files);
                                    report.setGlyphHackOnePerfect(details);
                                    return report;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(GLYPH_HACK_2_PERFECT_TSV, files);
                                    report.setGlyphHackTwoPerfect(details);
                                    return report;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(GLYPH_HACK_3_PERFECT_TSV, files);
                                    report.setGlyphHackThreePerfect(details);
                                    return report;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(GLYPH_HACK_4_PERFECT_TSV, files);
                                    report.setGlyphHackFourPerfect(details);
                                    return report;
                                }, executor),
                        CompletableFuture
                                .supplyAsync(() -> {
                                    final ReportDetails<List<NumericBasedRecord<Float>>> details = parse(GLYPH_HACK_5_PERFECT_TSV, files);
                                    report.setGlyphHackFivePerfect(details);
                                    return report;
                                }, executor)
                )
                .thenApplyAsync(unused -> report);
    }

    private static ReportDetails<List<NumericBasedRecord<Float>>> parse(final String targetFileName, final List<Path> files) {
        Optional<Path> dataFile = files.stream()
                .filter(file -> file.getFileName().toString().equals(targetFileName))
                .findFirst();
        if (!dataFile.isPresent()) {
            LOGGER.warn("Can not find report named '{}', skipping ...", targetFileName);
            return ReportDetails.error(FILE_NOT_FOUND);
        }
        final NumericBasedRecordParser<Float> parser = new NumericBasedRecordParser<>(TIME_PARSER, FloatValueParser.getDefault());
        final ReportDetails<List<NumericBasedRecord<Float>>> details = parser.parse(dataFile.get());
        if (details.isOk()) {
            LOGGER.info("Parsed {} records in {}", details.getData().size(), targetFileName);
        } else {
            LOGGER.warn("Ran into error when parsing {}: {}", targetFileName, details.getError());
        }
        return details;
    }

}
