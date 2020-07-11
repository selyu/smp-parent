package org.selyu.smp.core

import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.command.BalanceCommand
import org.selyu.smp.core.command.resolver.ProfileContextResolver
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.data.mongo.MongoRepository
import org.selyu.smp.core.listener.ProfileListener
import org.selyu.smp.core.listener.ScoreboardListener
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.profile.Profile
import org.selyu.smp.core.settings.Settings
import org.selyu.smp.core.util.registerListeners
import org.selyu.ui.UserInterfaceProvider
import java.util.*

class Core : JavaPlugin() {
    private lateinit var userInterfaceProvider: UserInterfaceProvider
    private lateinit var repository: Repository
    private lateinit var profileManager: ProfileManager
    private lateinit var commandManager: PaperCommandManager

    companion object {
        @JvmStatic
        lateinit var audienceProvider: BukkitAudiences
    }

    override fun onLoad() {
        Settings.init(dataFolder)
    }

    override fun onEnable() {
        userInterfaceProvider = UserInterfaceProvider(this, 1)
        repository = MongoRepository()
        profileManager = ProfileManager(this, repository)
        audienceProvider = BukkitAudiences.create(this)
        commandManager = PaperCommandManager(this)

        commandManager.locales.addMessageBundle("acf-locale", Locale.US)
        commandManager.locales.defaultLocale = Locale.US

        commandManager.setFormat(MessageType.ERROR, ChatColor.RED)
        commandManager.setFormat(MessageType.SYNTAX, ChatColor.RED)
        commandManager.setFormat(MessageType.INFO, ChatColor.AQUA)
        commandManager.setFormat(MessageType.HELP, ChatColor.YELLOW)

        commandManager.commandContexts.registerContext(Profile::class.java, ProfileContextResolver(profileManager, repository))

        commandManager.registerCommand(BalanceCommand(profileManager, repository))

        registerListeners(
                ProfileListener(profileManager),
                ScoreboardListener(userInterfaceProvider)
        )

        server.onlinePlayers.forEach {
            it.kickPlayer(Errors.CORE_LOADED)
        }
    }

    override fun onDisable() {
        repository.closeConnections()
    }
}