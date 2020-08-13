package org.selyu.smp.core;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.selyu.commands.api.lang.Lang;
import org.selyu.commands.spigot.SpigotCommandService;
import org.selyu.commands.spigot.lang.SpigotLang;
import org.selyu.smp.core.command.RecipesCommand;
import org.selyu.smp.core.listener.CustomItemListener;
import org.selyu.smp.core.listener.ProfileListener;
import org.selyu.smp.core.manager.CustomItemManager;
import org.selyu.ui.UserInterfaceProvider;

import static org.selyu.smp.core.util.MessageUtil.error;

public final class Core extends JavaPlugin {
    private static Core instance;

    private UserInterfaceProvider userInterfaceProvider;
    private CustomItemManager customItemManager;
    private InventoryManager inventoryManager;
    private BukkitAudiences bukkitAudiences;

    public static Core getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        userInterfaceProvider = new UserInterfaceProvider(this, 1);
        customItemManager = new CustomItemManager();
        inventoryManager = new InventoryManager(this);
        bukkitAudiences = BukkitAudiences.create(this);

        customItemManager.addRecipes();
        inventoryManager.init();

        SpigotCommandService commandService = new SpigotCommandService(this);
        commandService.register(new RecipesCommand(), "recipes");

        for (Lang.Type value : Lang.Type.values()) {
            commandService.getLang().set(value, error(commandService.getLang().get(value)));
        }

        for (SpigotLang.Type value : SpigotLang.Type.values()) {
            commandService.getLang().set(value, error(commandService.getLang().get(value)));
        }

        registerListeners(
                new ProfileListener(),
                new CustomItemListener()
        );

        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            onlinePlayer.kickPlayer(Errors.CORE_LOADED);
        }

        commandService.registerCommands();
    }

    private void registerListeners(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public UserInterfaceProvider getUserInterfaceProvider() {
        return userInterfaceProvider;
    }

    public CustomItemManager getCustomItemManager() {
        return customItemManager;
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
