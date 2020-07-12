package org.selyu.smp.core.manager

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.selyu.smp.core.Core
import org.selyu.smp.core.item.impl.ExampleCoreItem

class CoreItemManager(core: Core) {
    private val customItems = arrayOf(
            ExampleCoreItem()
    )

    init {
        // TODO: Better way to handle recipes for custom items?
        var item = customItems[0].getItem()
        item = customItems[0].applyExtraData(item)

        val exampleItemRecipe = ShapelessRecipe(NamespacedKey(core, "example_item"), item)
        exampleItemRecipe.addIngredient(1, Material.DIAMOND)

        core.server.addRecipe(exampleItemRecipe)
    }

    fun isValid(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta())
            return false

        return customItems
                .filter { coreItem -> coreItem.material == itemStack.type }
                .filter { coreItem -> coreItem.modelData == itemStack.itemMeta!!.customModelData }
                .filter { coreItem -> coreItem.validate(itemStack) }
                .any()
    }
}