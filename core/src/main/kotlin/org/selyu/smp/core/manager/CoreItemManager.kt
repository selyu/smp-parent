package org.selyu.smp.core.manager

import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.ItemData
import org.selyu.smp.core.item.WrappedCoreItem
import org.selyu.smp.core.item.impl.ExampleCoreItem

class CoreItemManager {
    private val customItems = of(
            ExampleCoreItem()
    )

    fun isValid(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta())
            return false

        return customItems
                .filter { wrapped -> wrapped.itemData.material == itemStack.type }
                .filter { wrapped -> wrapped.itemData.modelData == itemStack.itemMeta!!.customModelData }
                .any()
    }

    private fun of(vararg coreItems: CoreItem): List<WrappedCoreItem> {
        return coreItems.map { coreItem ->
            val annotation = coreItem.javaClass.getAnnotation(ItemData::class.java) ?: return@map null
            WrappedCoreItem(coreItem, annotation)
        }.filterNotNull()
    }
}