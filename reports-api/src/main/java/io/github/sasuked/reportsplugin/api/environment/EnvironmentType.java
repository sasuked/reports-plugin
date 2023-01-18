package io.github.sasuked.reportsplugin.api.environment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnvironmentType {

    PRODUCTION("PROD"),
    DEVELOPMENT("DEV");

    private final String tag;

    public static EnvironmentType fromTag(String tag) {
        if (tag.equalsIgnoreCase("DEV")) {
            return DEVELOPMENT;
        } else if (tag.equalsIgnoreCase("PROD")) {
            return PRODUCTION;
        } else {
            throw new IllegalStateException("Failed to find a environment by the " + tag + " tag.");
        }
    }
}
