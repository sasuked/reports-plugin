package io.github.sasuked.reportsplugin.api.service;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ReportService {

    @NotNull CompletableFuture<List<Report>> getAllReports();

    @NotNull CompletableFuture<List<Report>> getAllFromReportedId(@NotNull UUID reportedPlayerId);

    @NotNull CompletableFuture<List<Report>> getAllFromAuthorId(@NotNull UUID authorId);

    @NotNull CompletableFuture<Report> createReport(
      @NotNull UUID reportedPlayerId,
      @NotNull UUID authorId,
      @NotNull ReportType type
    );

    @NotNull CompletableFuture<Report> deleteReport(@NotNull UUID reportId);
}
