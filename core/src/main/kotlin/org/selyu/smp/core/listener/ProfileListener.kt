package org.selyu.smp.core.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.selyu.smp.core.manager.ProfileManager

class ProfileListener(private val profileManager: ProfileManager) : Listener {
    @EventHandler
    fun onJoin(event: AsyncPlayerPreLoginEvent) = profileManager.login(event)

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) = profileManager.quit(event)
}