package org.selyu.smp.core.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.plugin.java.JavaPlugin

abstract class CoreItem(val material: Material, val modelData: Int) {
    protected fun itemOf(displayName: String = "", vararg lore: String = arrayOf()) = ItemStack(material).also {
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

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler

data class DamageEntityEvent(val player: Player, val itemStack: ItemStack, val damagedEntity: Entity)
data class LeftClickEvent(val player: Player, val itemStack: ItemStack, val clickedEntity: Entity?, val clickedBlock: Block?)
data class RightClickEvent(val player: Player, val itemStack: ItemStack, val clickedEntity: Entity?, val clickedBlock: Block?)