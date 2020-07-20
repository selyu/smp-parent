package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.CoreItemType
import org.selyu.smp.core.item.CoreRecipe
import org.selyu.smp.core.item.MaterialRecipeKey

class HellstoneIngotItem : CoreItem("hellstone_ingot", CoreItemType.INGOT, Material.PAPER, 1) {
    override fun getDisplayName(): String = "Hellstone Ingot"
    override fun getLore(): MutableList<String> = mutableListOf("&c&oForged from fiery depths...")

    override fun getRecipe(): CoreRecipe? = CoreRecipe(this).also {
        it.addKey(MaterialRecipeKey(Material.MAGMA_BLOCK, 8))
        it.addKey(MaterialRecipeKey(Material.IRON_INGOT))
    }
}