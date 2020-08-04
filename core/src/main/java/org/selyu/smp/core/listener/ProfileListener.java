package org.selyu.smp.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.Errors;
import org.selyu.smp.core.manager.ProfileManager;
import org.selyu.smp.core.scoreboard.CoreScoreboardAdapter;
import org.selyu.ui.UserInterfaceProvider;
import org.selyu.ui.scoreboard.adapter.ScoreboardAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.selyu.smp.core.util.MessageUtil.warning;

public final class ProfileListener implements Listener {
    private final ProfileManager profileManager = Core.getInstance().getProfileManager();
    private final UserInterfaceProvider userInterfaceProvider = Core.getInstance().getUserInterfaceProvider();
    private final ScoreboardAdapter scoreboardAdapter = new CoreScoreboardAdapter();
    private final Map<UUID, Long> loginTimes = new HashMap<>();

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            long start = System.currentTimeMillis();
            profileManager.login(event);
            loginTimes.put(event.getUniqueId(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Errors.ERROR_LOADING_PROFILE);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("core.debug"))
            warning(event.getPlayer(), String.format("Your profile took %sms to load!", loginTimes.get(event.getPlayer().getUniqueId())));

        loginTimes.remove(event.getPlayer().getUniqueId());
        userInterfaceProvider.setBoard(event.getPlayer(), scoreboardAdapter);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        profileManager.quit(event);
    }
}
