package io.github.sasuked.reportsplugin.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public enum ReportType {

    HACKING("Uso de hack"),
    INAPPROPRIATE_NICKNAME("Nick inapropriado"),
    CHAT_INFRACTION("Má conduta no bate-papo"),
    GUILD_TAG_INFRACTION("Tag de guilda inapropriada");


    public static @Nullable ReportType fromName(String name) {
        for (ReportType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

    private final String name;
}
