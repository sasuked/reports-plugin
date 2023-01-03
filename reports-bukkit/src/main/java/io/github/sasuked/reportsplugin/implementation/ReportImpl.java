package io.github.sasuked.reportsplugin.implementation;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class ReportImpl implements Report {

    private static final Supplier<Long> REPORT_LIFETIME = () -> System.currentTimeMillis() + (TimeUnit.DAYS.toSeconds(24) * 1000);


    public static ReportImpl createNewReport(UUID reportedPlayerId, UUID authorId, ReportType type) {
        return new ReportImpl(
          UUID.randomUUID(),
          reportedPlayerId,
          authorId,
          type,
          System.currentTimeMillis(),
          REPORT_LIFETIME.get()
        );
    }

    private final UUID uniqueId;
    private final UUID reportedPlayerId;
    private final UUID authorId;
    private final ReportType type;
    private final long creationTime;
    private final long expirationTime;

}
