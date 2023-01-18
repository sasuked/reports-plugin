package io.github.sasuked.reportsplugin;

import com.google.common.collect.ImmutableMap;
import io.github.sasuked.reportsplugin.api.environment.EnvironmentType;
import io.github.sasuked.reportsplugin.api.service.ReportService;
import io.github.sasuked.reportsplugin.command.ReportCheckCommand;
import io.github.sasuked.reportsplugin.command.ReportCommand;
import io.github.sasuked.reportsplugin.service.ReportServiceImplementation;
import io.github.sasuked.reportsplugin.view.ReportListView;
import lombok.Getter;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;

import static io.github.sasuked.reportsplugin.data.provider.ReportDataProvider.fromDevelopmentServer;
import static io.github.sasuked.reportsplugin.data.provider.ReportDataProvider.fromProductionServer;

@Getter
public class ReportsPlugin extends JavaPlugin {

    private ViewFrame viewFrame;
    private ReportService reportService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        viewFrame = ViewFrame.of(this, new ReportListView()).register();

        EnvironmentType environment = this.getServerEnvironment();
        if (environment == EnvironmentType.PRODUCTION) {
            reportService = new ReportServiceImplementation(fromProductionServer(readCredentials(getConfig())));
        } else {
            reportService = new ReportServiceImplementation(fromDevelopmentServer());
        }

        ((CraftServer) getServer()).getCommandMap().registerAll("screenreports", Arrays.asList(
          new ReportCommand(this),
          new ReportCheckCommand(this)
        ));
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

    public EnvironmentType getServerEnvironment() {
        return EnvironmentType.fromTag(getConfig().getString("environment"));
    }
}
