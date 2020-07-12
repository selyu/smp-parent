package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.item.DurableCoreItem
import org.selyu.smp.core.util.color

class DiamondShearsItem : DurableCoreItem(Material.SHEARS, 1, 1487) {
    override fun getLore(): MutableList<String> = mutableListOf("&7&oEspecially durable!".color)

    override fun getItem(): ItemStack = itemOf("&bDiamond Shears".color)

    override fun getRecipe(plugin: JavaPlugin): Recipe? = ShapedRecipe(NamespacedKey(plugin, "diamond_shears"), getItem()).also {
        it.shape(
                "#d", "d#"
        )

        it.setIngredient('d', Material.DIAMOND)
    }
}