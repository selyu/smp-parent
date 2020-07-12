package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.ItemEventHandler
import org.selyu.smp.core.item.LeftClickEvent
import org.selyu.smp.core.item.RightClickEvent
import org.selyu.smp.core.util.color

class ExampleCoreItem : CoreItem(Material.DIAMOND_PICKAXE, 1) {
    override fun getItem(): ItemStack = itemOf("&bExample Pickaxe".color, "&cExample Pickaxe Lore".color)

    override fun getRecipe(plugin: JavaPlugin): Recipe? = ShapedRecipe(NamespacedKey(plugin, "example_pickaxe"), getItem()).also {
        it.shape(
                "ddd", " s ", " s "
        )
        it.setIngredient('d', Material.DIAMOND)
        it.setIngredient('s', Material.STICK)
    }

    @ItemEventHandler
    fun onLeftClick(event: LeftClickEvent) {
        event.clickedBlock?.let {
            event.player.sendMessage("You left clicked a block with the custom pickaxe!")
        }
    }

    @ItemEventHandler
    fun onRightClick(event: RightClickEvent) {
        event.clickedBlock?.let {
            event.player.sendMessage("You right clicked a block with the custom pickaxe!")
        }
    }
}