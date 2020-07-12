package org.selyu.smp.core.manager

import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.Core
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.Event
import org.selyu.smp.core.item.WrappedCoreItem
import org.selyu.smp.core.item.impl.ExampleCoreItem

class CoreItemManager(core: Core) {
    private val items = wrap(
            ExampleCoreItem()
    )

    init {
        items.forEach {
            val recipe = it.parent.getRecipe(core)
            if (recipe != null)
                core.server.addRecipe(recipe)
        }
    }

    fun isValid(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta())
            return false

        return items
                .filter { wrapped -> wrapped.parent.material == itemStack.type }
                .filter { wrapped -> wrapped.parent.modelData == itemStack.itemMeta!!.customModelData }
                .filter { wrapped -> wrapped.parent.validate(itemStack) }
                .any()
    }

    fun runEvent(event: Event) {
        items.forEach { it.runEvent(event) }
    }

    private fun wrap(vararg coreItems: CoreItem): List<WrappedCoreItem> = coreItems
            .map { WrappedCoreItem(it) }
}