package org.selyu.smp.core.listener

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.item.getValidItems
import org.selyu.smp.core.manager.CoreItemManager

class CoreItemListener(private val coreItemManager: CoreItemManager) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.useInteractedBlock() == Event.Result.DENY || !isPossibleItem(event.item))
            return

        runEvent(event, event.item!!)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.isCancelled || event.player.equipment == null || !isPossibleItem(event.player.equipment!!.itemInMainHand))
            return

        runEvent(event, event.player.equipment!!.itemInMainHand)
    }

    private fun isPossibleItem(itemStack: ItemStack?): Boolean {
        return itemStack != null && itemStack.hasItemMeta() && itemStack.itemMeta!!.hasCustomModelData()
    }

    private fun runEvent(event: Event, itemStack: ItemStack) {
        coreItemManager.items.getValidItems(itemStack).forEach { it.runEvent(event) }
    }
}
