package org.selyu.smp.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.scoreboard.CoreScoreboardAdapter;
import org.selyu.ui.UserInterfaceProvider;
import org.selyu.ui.scoreboard.adapter.ScoreboardAdapter;

public final class ProfileListener implements Listener {
    private final UserInterfaceProvider userInterfaceProvider = Core.getInstance().getUserInterfaceProvider();
    private final ScoreboardAdapter scoreboardAdapter = new CoreScoreboardAdapter();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        userInterfaceProvider.setBoard(event.getPlayer(), scoreboardAdapter);
    }
}
