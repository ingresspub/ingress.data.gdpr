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

import ingress.data.gdpr.models.CountBasedRecord;
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
import java.util.stream.Stream;

/**
 * @author SgrAlpha
 */
public class Summarizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Summarizer.class);

    public static SummarizedReport summarize(final Stream<Path> files) {
        notNull(files, "Missing files");
        final SummarizedReport report = new SummarizedReport();
        try {
            CompletableFuture
                    .allOf(
                            parseMentoringReport(report, files)
                    )
                    .get(10, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return report;
    }

    private static CompletableFuture<SummarizedReport> parseMentoringReport(final SummarizedReport report, final Stream<Path> files) {
        return CompletableFuture
                .supplyAsync(() -> {
                    Optional<Path> dataFile = files
                            .filter(file -> file.getFileName().startsWith("agents_recruited"))
                            .findFirst();
                    if (!dataFile.isPresent()) {
                        return report;
                    }
                    final ReportDetails<List<CountBasedRecord>> details = new CountBasedParser().parse(dataFile.get());
                    if (!details.isOk()) {
                        return report;
                    }
                    report.setMentoring(new MentoringReport(details));
                    return report;
                });
    }

}
