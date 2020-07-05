package org.selyu.smp.core.settings

import me.idriz.oss.config.annotation.Value
import me.idriz.oss.config.yaml.YamlConfig
import java.io.File

object Settings {
    private lateinit var yamlConfig: YamlConfig

    @Value("connection string")
    const val CONNECTION_STRING = "mongodb://127.0.0.1:27017/?authSource=admin"

    @Value("database name")
    const val DATABASE_NAME = "core"

    fun init(directory: File) {
        yamlConfig = YamlConfig(directory, "settings")
        yamlConfig.addHook(Settings)
    }
}