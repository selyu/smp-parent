package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.Core
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.ItemEventHandler
import org.selyu.smp.core.util.color

class DiamondShearsItem : CoreItem(Material.SHEARS, 1) {
    companion object {
        const val MAX_DURABILITY = 1487
        val DURABILITY_KEY = Core.keyOf("durability")
    }

    override fun getItem(): ItemStack = itemOf("&bDiamond Shears".color).also {
        val meta = it.itemMeta ?: throw NullPointerException("Meta is null!")
        meta.isUnbreakable = true
        meta.lore = listOf("&fDurability: $MAX_DURABILITY / $MAX_DURABILITY".color)
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        meta.persistentDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, MAX_DURABILITY)

        it.itemMeta = meta
    }

    override fun getRecipe(plugin: JavaPlugin): Recipe? = ShapedRecipe(NamespacedKey(plugin, "diamond_shears"), getItem()).also {
        it.shape(
                "#d", "d#"
        )

        it.setIngredient('d', Material.DIAMOND)
    }

    @ItemEventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val item = event.player.equipment!!.itemInMainHand
        val meta = item.itemMeta ?: throw NullPointerException("Meta is null!")

        val newDurability = meta.persistentDataContainer.getOrDefault(DURABILITY_KEY, PersistentDataType.INTEGER, MAX_DURABILITY) - 1

        if (newDurability == -1) {
            player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            player.equipment!!.setItemInMainHand(null)
        } else {
            meta.lore = listOf("&fDurability: $newDurability / $MAX_DURABILITY".color)
            meta.persistentDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, newDurability)
            item.itemMeta = meta
        }

        player.updateInventory()
    }
}