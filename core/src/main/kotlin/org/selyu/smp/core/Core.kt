package org.selyu.smp.core

import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import fr.minuskube.inv.InventoryManager
import fr.minuskube.inv.SmartInventory
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.command.BalanceCommand
import org.selyu.smp.core.command.RecipesCommand
import org.selyu.smp.core.command.resolver.ProfileContextResolver
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.data.mongo.MongoRepository
import org.selyu.smp.core.listener.CoreItemListener
import org.selyu.smp.core.listener.ProfileListener
import org.selyu.smp.core.listener.ScoreboardListener
import org.selyu.smp.core.manager.CoreItemManager
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.profile.Profile
import org.selyu.smp.core.settings.Settings
import org.selyu.smp.core.util.registerListeners
import org.selyu.ui.UserInterfaceProvider
import java.util.*

class Core : JavaPlugin() {
    lateinit var userInterfaceProvider: UserInterfaceProvider
    lateinit var repository: Repository
    lateinit var profileManager: ProfileManager
    lateinit var coreItemManager: CoreItemManager
    private lateinit var commandManager: PaperCommandManager
    private lateinit var inventoryManager: InventoryManager

    companion object {
        @JvmStatic
        lateinit var instance: Core

        @JvmStatic
        lateinit var audienceProvider: BukkitAudiences

        @JvmStatic
        fun keyOf(key: String): NamespacedKey = NamespacedKey(instance, key)
    }

    override fun onLoad() {
        Settings.init(dataFolder)
    }

    override fun onEnable() {
        instance = this
        audienceProvider = BukkitAudiences.create(this)
        userInterfaceProvider = UserInterfaceProvider(this, 1)
        repository = MongoRepository()
        profileManager = ProfileManager()
        coreItemManager = CoreItemManager()
        commandManager = PaperCommandManager(this)
        inventoryManager = InventoryManager(this)

        inventoryManager.init()

        commandManager.locales.addMessageBundle("acf-locale", Locale.US)
        commandManager.locales.defaultLocale = Locale.US

        commandManager.setFormat(MessageType.ERROR, ChatColor.RED)
        commandManager.setFormat(MessageType.SYNTAX, ChatColor.RED)
        commandManager.setFormat(MessageType.INFO, ChatColor.AQUA)
        commandManager.setFormat(MessageType.HELP, ChatColor.YELLOW)

        commandManager.commandContexts.registerContext(Profile::class.java, ProfileContextResolver())

        commandManager.registerCommand(BalanceCommand())
        commandManager.registerCommand(RecipesCommand())

        registerListeners(
                ProfileListener(),
                ScoreboardListener(),
                CoreItemListener()
        )

        server.onlinePlayers.forEach {
            it.kickPlayer(Errors.CORE_LOADED)
        }
    }

    override fun onDisable() {
        repository.closeConnections()
    }

    fun buildInventory(): SmartInventory.Builder = SmartInventory.builder().manager(inventoryManager)
}