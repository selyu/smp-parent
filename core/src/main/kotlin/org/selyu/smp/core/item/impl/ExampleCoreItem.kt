package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.ItemData

@ItemData(material = Material.DIAMOND_PICKAXE, modelData = 1)
class ExampleCoreItem : CoreItem {
    override fun getItem(): ItemStack = ItemStack(Material.DIAMOND_PICKAXE).also {
        val meta = it.itemMeta ?: return@also
        meta.setDisplayName("&cExample Item")
        meta.setCustomModelData(1)

        it.itemMeta = meta
    }
}