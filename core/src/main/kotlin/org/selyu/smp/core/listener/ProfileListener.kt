package org.selyu.smp.core.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.selyu.smp.core.Core
import org.selyu.smp.core.Errors
import org.selyu.smp.core.util.warning
import java.util.*

class ProfileListener : Listener {
    private val profileManager = Core.getInstance().profileManager
    private val loginTimes = hashMapOf<UUID, Long>()

    @EventHandler
    fun onLogin(event: AsyncPlayerPreLoginEvent) {
        try {
            val start = System.currentTimeMillis()
            profileManager.login(event)
            loginTimes[event.uniqueId] = System.currentTimeMillis() - start
        } catch (e: Exception) {
            e.printStackTrace()
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Errors.ERROR_LOADING_PROFILE)
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (event.player.hasPermission("core.debug"))
            event.player.warning("Your profile took ${loginTimes[event.player.uniqueId]}ms to load!")
        loginTimes.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) = profileManager.quit(event)
}