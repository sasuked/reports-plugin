package io.github.sasuked.reportsplugin.data.provider;

import io.github.sasuked.reportsplugin.data.ReportData;
import io.github.sasuked.reportsplugin.data.cache.ReportRedisCache;
import io.github.sasuked.reportsplugin.data.repository.ReportMongoRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class ReportDataProvider {

    private static final ExecutorService ASYNC_EXECUTOR = new ForkJoinPool(2);

    private final ReportMongoRepository repository;
    private final ReportRedisCache cache;

    public ReportDataProvider(@NotNull Map<String, Object> credentials) {
        this.repository = ReportMongoRepository.fromCredentials(credentials);
        this.cache = ReportRedisCache.fromCredentials(credentials);
    }

    public CompletableFuture<List<ReportData>> selectAll() {
        return CompletableFuture.supplyAsync(cache::getCachedReports, ASYNC_EXECUTOR);
    }

    public CompletableFuture<@Nullable ReportData> deleteReport(@NotNull UUID reportUniqueId) {
        return CompletableFuture.supplyAsync(() -> repository.deleteReport(reportUniqueId), ASYNC_EXECUTOR)
          .whenComplete((reportData, throwable) -> {
              if (reportData != null) {
                  cache.invalidateReport(reportData);
              }
          });
    }

    public CompletableFuture<@Nullable ReportData> updateReport(@NotNull ReportData reportData) {
        return CompletableFuture.supplyAsync(() -> repository.updateReport(reportData), ASYNC_EXECUTOR)
          .whenComplete((updatedData, throwable) -> {
              if (updatedData != null) {
                  cache.put(reportData);
              }
          });
    }

    public CompletableFuture<@Nullable ReportData> getReport(@NotNull UUID reportUniqueId) {
        return CompletableFuture.supplyAsync(() -> cache.getReportData(reportUniqueId), ASYNC_EXECUTOR)
          .thenApplyAsync(reportData -> (reportData == null ? repository.findOne(reportUniqueId) : reportData), ASYNC_EXECUTOR)
          .whenCompleteAsync((reportData, throwable) -> {
              if (reportData != null) {
                  cache.put(reportData);
              }
          }, ASYNC_EXECUTOR);
    }
}
