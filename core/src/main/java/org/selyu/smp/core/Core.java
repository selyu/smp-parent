package org.selyu.smp.core;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.command.BalanceCommand;
import org.selyu.smp.core.command.RecipesCommand;
import org.selyu.smp.core.command.resolver.ProfileContextResolver;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.data.mongo.MongoRepository;
import org.selyu.smp.core.listener.CoreItemListener;
import org.selyu.smp.core.listener.ProfileListener;
import org.selyu.smp.core.listener.ScoreboardListener;
import org.selyu.smp.core.manager.CoreItemManager;
import org.selyu.smp.core.manager.ProfileManager;
import org.selyu.smp.core.profile.Profile;
import org.selyu.smp.core.settings.Settings;
import org.selyu.ui.UserInterfaceProvider;

import java.util.Locale;

public final class Core extends JavaPlugin {
    private static Core instance;

    private UserInterfaceProvider userInterfaceProvider;
    private Repository repository;
    private ProfileManager profileManager;
    private CoreItemManager coreItemManager;
    private PaperCommandManager paperCommandManager;
    private InventoryManager inventoryManager;
    private BukkitAudiences bukkitAudiences;

    public static Core getInstance() {
        return instance;
    }

    public static NamespacedKey keyOf(String key) {
        return new NamespacedKey(instance, key);
    }

    @Override
    public void onLoad() {
        new Settings().init(getDataFolder());
    }

    @Override
    public void onEnable() {
        instance = this;
        userInterfaceProvider = new UserInterfaceProvider(this, 1);
        repository = new MongoRepository();
        profileManager = new ProfileManager();
        coreItemManager = new CoreItemManager();
        paperCommandManager = new PaperCommandManager(this);
        inventoryManager = new InventoryManager(this);
        bukkitAudiences = BukkitAudiences.create(this);

        inventoryManager.init();

        paperCommandManager.getLocales().addMessageBundle("acf-locale", Locale.US);
        paperCommandManager.getLocales().setDefaultLocale(Locale.US);

        paperCommandManager.setFormat(MessageType.ERROR, ChatColor.RED);
        paperCommandManager.setFormat(MessageType.SYNTAX, ChatColor.RED);
        paperCommandManager.setFormat(MessageType.INFO, ChatColor.AQUA);
        paperCommandManager.setFormat(MessageType.HELP, ChatColor.YELLOW);

        paperCommandManager.getCommandContexts().registerContext(Profile.class, new ProfileContextResolver());
        paperCommandManager.registerCommand(new BalanceCommand());
        paperCommandManager.registerCommand(new RecipesCommand());

        registerListeners(
                new ProfileListener(),
                new ScoreboardListener(),
                new CoreItemListener()
        );

        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            onlinePlayer.kickPlayer(Errors.CORE_LOADED);
        }
    }

    @Override
    public void onDisable() {
        repository.closeConnections();
    }

    private void registerListeners(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public UserInterfaceProvider getUserInterfaceProvider() {
        return userInterfaceProvider;
    }

    public Repository getRepository() {
        return repository;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public CoreItemManager getCoreItemManager() {
        return coreItemManager;
    }

    public PaperCommandManager getPaperCommandManager() {
        return paperCommandManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }

    public SmartInventory.Builder buildInventory() {
        return SmartInventory.builder()
                .manager(inventoryManager);
    }
}
