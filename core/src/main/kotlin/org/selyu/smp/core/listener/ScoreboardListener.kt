package org.selyu.smp.core.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.selyu.smp.core.Core
import org.selyu.smp.core.scoreboard.CoreScoreboardAdapter

class ScoreboardListener : Listener {
    private val userInterfaceProvider = Core.instance.userInterfaceProvider

    companion object {
        private val scoreboardAdapter: CoreScoreboardAdapter = CoreScoreboardAdapter()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        userInterfaceProvider.setBoard(event.player, scoreboardAdapter)
    }
}