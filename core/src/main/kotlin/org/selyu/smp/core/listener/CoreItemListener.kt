package org.selyu.smp.core.listener

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.manager.CoreItemManager
import org.selyu.smp.core.util.isCustomItem

class CoreItemListener(private val coreItemManager: CoreItemManager) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.useInteractedBlock() == Event.Result.DENY || !isPossibleItem(event.item))
            return

        coreItemManager.runEvent(event, event.item!!)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.isCancelled || event.player.equipment == null || !isPossibleItem(event.player.equipment!!.itemInMainHand))
            return

        coreItemManager.runEvent(event, event.player.equipment!!.itemInMainHand)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onShear(event: PlayerShearEntityEvent) {
        if (event.isCancelled || !isPossibleItem(event.item))
            return

        coreItemManager.runEvent(event, event.item)
    }

    private fun isPossibleItem(itemStack: ItemStack?): Boolean {
        return itemStack != null && itemStack.isCustomItem()
    }
}
