package io.github.sasuked.reportsplugin.data.provider;

import io.github.sasuked.reportsplugin.data.ReportData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ReportDataProvider {

    static ReportDataProvider fromProductionServer(Map<String, Object> credentials) {
        return new DatabaseDrivenDataProvider(credentials);
    }

    static ReportDataProvider fromDevelopmentServer() {
        return new MemoryCachedDataProvider();
    }

    CompletableFuture<List<ReportData>> selectAll();

    CompletableFuture<@Nullable ReportData> deleteReport(@NotNull UUID reportUniqueId);

    CompletableFuture<@Nullable ReportData> updateReport(@NotNull ReportData reportData);

    CompletableFuture<@Nullable ReportData> getReport(@NotNull UUID reportUniqueId);
}
