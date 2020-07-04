package org.selyu.smp.core

import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.listener.ScoreboardListener
import org.selyu.smp.core.util.registerListeners
import org.selyu.ui.UserInterfaceProvider
import org.selyu.ui.scoreboard.title.ScoreboardTitle

class Core : JavaPlugin() {
    private lateinit var userInterfaceProvider: UserInterfaceProvider

    override fun onEnable() {
        ScoreboardTitle.frameUpdateSpeed = 1
        userInterfaceProvider = UserInterfaceProvider(this, 1)

        registerListeners(
                ScoreboardListener(userInterfaceProvider)
        )
    }
}