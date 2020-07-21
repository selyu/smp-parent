package org.selyu.smp.core.settings;

import me.idriz.oss.config.annotation.Value;
import me.idriz.oss.config.yaml.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class Settings {
    @Value("connection string")
    public static String CONNECTION_STRING = "mongodb://127.0.0.1:27017/?authSource=admin";

    @Value("database name")
    public static String DATABASE_NAME = "core";

    public void init(@NotNull File directory) {
        YamlConfig yamlConfig = new YamlConfig(directory, "settings");
        yamlConfig.addHook(this);
    }
}
