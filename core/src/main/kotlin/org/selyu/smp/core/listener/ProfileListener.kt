package org.selyu.smp.core.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.selyu.smp.core.Core
import org.selyu.smp.core.Errors
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.util.asComponent

class ProfileListener(private val core: Core, private val profileManager: ProfileManager) : Listener {
    @EventHandler
    fun onLogin(event: AsyncPlayerPreLoginEvent) {
        try {
            profileManager.login(event)
        } catch (e: Exception) {
            e.printStackTrace()
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Errors.ERROR_LOADING_PROFILE)
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val profileLoadedComponent = "<red>Your profile has loaded successfully!".asComponent
        core.sendComponentMessage(player, profileLoadedComponent)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) = profileManager.quit(event)
}