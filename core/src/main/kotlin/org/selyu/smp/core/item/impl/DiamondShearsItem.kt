package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.selyu.smp.core.item.CoreItemType
import org.selyu.smp.core.item.DurableCoreItem
import org.selyu.smp.core.item.recipe.CoreRecipe
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey

class DiamondShearsItem : DurableCoreItem("diamond_shears", CoreItemType.SHEARS, Material.SHEARS, 1, 1487) {
    override fun getDisplayName(): String = "&fDiamond Shears"
    override fun getLore(): MutableList<String> = mutableListOf("&7&oEspecially durable!")

    override fun getRecipe() = CoreRecipe(this).also {
        it.addKey(MaterialRecipeKey(Material.DIAMOND, 2))
    }
}