package org.selyu.smp.core.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.selyu.smp.core.manager.CoreItemManager

class CoreItemListener(private val coreItemManager: CoreItemManager) : Listener {
    @EventHandler
    fun onItemInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        println(coreItemManager.isValid(item))
    }
}
