package org.selyu.smp.core.listener

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.item.getValidItems
import org.selyu.smp.core.manager.CoreItemManager

class CoreItemListener(private val coreItemManager: CoreItemManager) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.useInteractedBlock() == Event.Result.DENY || !isPossibleItem(event.item))
            return

        coreItemManager.items.getValidItems(event.item!!).forEach { it.runEvent(event) }
    }

    private fun isPossibleItem(itemStack: ItemStack?): Boolean {
        return itemStack != null && itemStack.hasItemMeta() && itemStack.itemMeta!!.hasCustomModelData()
    }
}
