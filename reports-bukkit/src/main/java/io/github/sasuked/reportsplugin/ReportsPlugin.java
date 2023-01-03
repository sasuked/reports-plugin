package io.github.sasuked.reportsplugin;

import com.google.common.collect.ImmutableMap;
import io.github.sasuked.reportsplugin.api.service.ReportService;
import io.github.sasuked.reportsplugin.service.ReportServiceImplementation;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
public class ReportsPlugin extends JavaPlugin {

    private ReportService reportService;

    @Override
    public void onEnable() {
        reportService = new ReportServiceImplementation(this.readCredentials(getConfig()));
    }

    @Override
    public void onDisable() {

    }

    private Map<String, Object> readCredentials(FileConfiguration config) {
        return ImmutableMap.of(
          "redis.host", config.getString("redis.host"),
          "redis.port", config.getInt("redis.port"),
          "mongo.user", config.getString("mongo.user"),
          "mongo.password", config.getString("mongo.password"),
          "mongo.port", config.getInt("mongo.port")
        );
    }
}
