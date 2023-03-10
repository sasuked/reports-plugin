package io.github.sasuked.reportsplugin.data;

import lombok.Data;

import java.util.UUID;

@Data
public class ReportData {

    private final UUID uniqueId;
    private final String reportedPlayerName;
    private final String authorName;
    private final String type;
    private final long creationTime;
    private final long expirationTime;

}
