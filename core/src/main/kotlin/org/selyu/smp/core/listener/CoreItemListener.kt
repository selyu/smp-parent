package org.selyu.smp.core.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.selyu.smp.core.item.LeftClickEvent
import org.selyu.smp.core.item.RightClickEvent
import org.selyu.smp.core.manager.CoreItemManager

class CoreItemListener(private val coreItemManager: CoreItemManager) : Listener {
    @Suppress("NON_EXHAUSTIVE_WHEN")
    @EventHandler
    fun onItemInteract(event: PlayerInteractEvent) {
        if (event.item == null)
            return

        if (coreItemManager.isValid(event.item!!)) {
            when (event.action) {
                Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                    coreItemManager.runEvent(LeftClickEvent(event))
                }

                Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                    coreItemManager.runEvent(RightClickEvent(event))
                }
            }
        }
    }
}
