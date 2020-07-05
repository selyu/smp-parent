package org.selyu.smp.core

import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.data.mongo.MongoRepository
import org.selyu.smp.core.listener.ProfileListener
import org.selyu.smp.core.listener.ScoreboardListener
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.settings.Settings
import org.selyu.smp.core.util.registerListeners
import org.selyu.ui.UserInterfaceProvider
import org.selyu.ui.scoreboard.title.ScoreboardTitle

class Core : JavaPlugin() {
    private lateinit var userInterfaceProvider: UserInterfaceProvider
    private lateinit var repository: Repository
    private lateinit var profileManager: ProfileManager

    override fun onLoad() {
        Settings.init(dataFolder)
    }

    override fun onEnable() {
        ScoreboardTitle.frameUpdateSpeed = 1
        userInterfaceProvider = UserInterfaceProvider(this, 1)
        repository = MongoRepository()
        profileManager = ProfileManager(repository)

        registerListeners(
                ProfileListener(profileManager),
                ScoreboardListener(userInterfaceProvider)
        )
    }

    override fun onDisable() {
        repository.closeConnections()
    }
}