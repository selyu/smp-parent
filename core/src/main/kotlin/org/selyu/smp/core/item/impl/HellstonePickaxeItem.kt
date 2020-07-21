package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.selyu.smp.core.item.CoreItemType
import org.selyu.smp.core.item.DurableCoreItem
import org.selyu.smp.core.item.recipe.CoreRecipe
import org.selyu.smp.core.item.recipe.key.impl.InternalNameRecipeKey
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey

class HellstonePickaxeItem : DurableCoreItem("hellstone_pickaxe", CoreItemType.PICKAXE, Material.IRON_PICKAXE, 1, 1000) {
    override fun getDisplayName(): String = "&fHellstone Pickaxe"

    override fun getRecipe(): CoreRecipe? = CoreRecipe(this).also {
        it.addKey(InternalNameRecipeKey("hellstone_ingot", 3))
        it.addKey(MaterialRecipeKey(Material.STICK, 2))
    }
}