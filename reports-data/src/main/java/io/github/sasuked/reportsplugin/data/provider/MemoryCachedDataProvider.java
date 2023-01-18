package io.github.sasuked.reportsplugin.data.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.sasuked.reportsplugin.data.ReportData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MemoryCachedDataProvider implements ReportDataProvider {

    private final Cache<UUID, ReportData> cachedReports = Caffeine.newBuilder()
      .expireAfterWrite(24, TimeUnit.HOURS)
      .build();

    @Override
    public CompletableFuture<List<ReportData>> selectAll() {
        return CompletableFuture.completedFuture(new ArrayList<>(cachedReports.asMap().values()));
    }

    @Override
    public CompletableFuture<@Nullable ReportData> deleteReport(@NotNull UUID reportUniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            ReportData report = cachedReports.getIfPresent(reportUniqueId);
            if (report != null) {
                cachedReports.invalidate(report);
            }

            return report;
        });
    }

    @Override
    public CompletableFuture<@Nullable ReportData> updateReport(@NotNull ReportData reportData) {
        return CompletableFuture.supplyAsync(() -> {
            cachedReports.put(reportData.getUniqueId(), reportData);
            return reportData;
        });
    }

    @Override
    public CompletableFuture<@Nullable ReportData> getReport(@NotNull UUID reportUniqueId) {
        return CompletableFuture.completedFuture(cachedReports.getIfPresent(reportUniqueId));
    }

}

