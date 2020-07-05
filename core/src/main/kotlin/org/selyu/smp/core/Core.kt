package org.selyu.smp.core

import me.mattstudios.mf.base.CommandManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.command.BalanceCommand
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
    private lateinit var audiences: BukkitAudiences
    private lateinit var commandManager: CommandManager

    override fun onLoad() {
        Settings.init(dataFolder)
    }

    override fun onEnable() {
        ScoreboardTitle.frameUpdateSpeed = 1
        userInterfaceProvider = UserInterfaceProvider(this, 1)
        repository = MongoRepository()
        profileManager = ProfileManager(this, repository)
        audiences = BukkitAudiences.create(this)
        commandManager = CommandManager(this, true)

        server.onlinePlayers.forEach {
            it.kickPlayer(Errors.PLEASE_RE_LOGIN)
        }

        commandManager.register(BalanceCommand(profileManager))

        registerListeners(
                ProfileListener(this, profileManager),
                ScoreboardListener(userInterfaceProvider)
        )
    }

    override fun onDisable() {
        repository.closeConnections()
    }

    fun sendComponentMessage(player: Player, component: Component) = audiences.player(player).sendMessage(component)
}