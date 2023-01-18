package io.github.sasuked.reportsplugin.implementation;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.util.Skulls;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class ReportImpl implements Report {

    private static final Supplier<Long> REPORT_LIFETIME = () -> System.currentTimeMillis() + (TimeUnit.DAYS.toSeconds(24) * 1000);


    public static ReportImpl createNewReport(
      @NotNull String reportedPlayerName,
      @NotNull String authorName,
      @NotNull ReportType type
    ) {
        return new ReportImpl(
          UUID.randomUUID(),
          reportedPlayerName,
          authorName,
          type,
          System.currentTimeMillis(),
          REPORT_LIFETIME.get()
        );
    }

    private final UUID uniqueId;
    private final String reportedPlayerName;
    private final String authorName;
    private final ReportType type;
    private final long creationTime;
    private final long expirationTime;

    @Override
    public @NotNull ItemStack getPlayerHead() {
        return Skulls.skullFromName(reportedPlayerName);
    }
}
