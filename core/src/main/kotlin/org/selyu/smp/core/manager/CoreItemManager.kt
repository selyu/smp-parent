package org.selyu.smp.core.manager

import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.Core
import org.selyu.smp.core.item.impl.ExampleCoreItem

class CoreItemManager(core: Core) {
    private val items = arrayOf(
            ExampleCoreItem()
    )

    init {
        items.forEach {
            val recipe = it.getRecipe(core)
            if (recipe != null)
                core.server.addRecipe(recipe)
        }
    }

    fun isValid(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta())
            return false

        return items
                .filter { coreItem -> coreItem.material == itemStack.type }
                .filter { coreItem -> coreItem.modelData == itemStack.itemMeta!!.customModelData }
                .filter { coreItem -> coreItem.validate(itemStack) }
                .any()
    }
}