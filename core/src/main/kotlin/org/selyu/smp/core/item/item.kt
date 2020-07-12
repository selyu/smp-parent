package org.selyu.smp.core.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.Core
import org.selyu.smp.core.util.color

abstract class CoreItem(val material: Material, val modelData: Int) {
    protected open fun itemOf(displayName: String = "", vararg lore: String = arrayOf()) = ItemStack(material).also {
        if (!it.hasItemMeta()) {
            it.itemMeta = Bukkit.getServer().itemFactory.getItemMeta(material) ?: return@also
        }

        val meta = it.itemMeta!!
        if (displayName.isNotBlank())
            meta.setDisplayName(displayName)
        if (lore.isNotEmpty())
            meta.lore = lore.toMutableList()
        meta.setCustomModelData(modelData)

        it.itemMeta = meta
    }

    abstract fun getItem(): ItemStack

    open fun validate(itemStack: ItemStack): Boolean = true
    open fun getRecipe(plugin: JavaPlugin): Recipe? = null
}

val DURABILITY_KEY = Core.keyOf("durability")

abstract class DurableCoreItem(material: Material, modelData: Int, private val maxDurability: Int) : CoreItem(material, modelData) {
    private fun addDurability(list: MutableList<String>, durability: Int): MutableList<String> {
        list.add("")
        list.add("&fDurability: $durability / $maxDurability".color)
        return list
    }

    override fun itemOf(displayName: String, vararg lore: String): ItemStack {
        return super.itemOf(displayName, *lore).also {
            val meta = it.itemMeta ?: throw NullPointerException("Meta is null!")
            meta.isUnbreakable = true

            meta.lore = addDurability(lore.toMutableList(), maxDurability)
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
            meta.persistentDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, maxDurability)

            it.itemMeta = meta
        }
    }

    private fun handleDurability(player: Player, itemStack: ItemStack) {
        val meta = itemStack.itemMeta ?: throw NullPointerException("Meta is null!")
        val newDurability = meta.persistentDataContainer.getOrDefault(DURABILITY_KEY, PersistentDataType.INTEGER, maxDurability) - 1
        if (newDurability == -1) {
            player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            player.equipment!!.setItemInMainHand(null)
        } else {
            meta.lore = addDurability(getLore(), newDurability)
            meta.persistentDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, newDurability)
            itemStack.itemMeta = meta
        }
    }

    @ItemEventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        handleDurability(event.player, event.player.equipment!!.itemInMainHand)
    }

    abstract fun getLore(): MutableList<String>
}

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler