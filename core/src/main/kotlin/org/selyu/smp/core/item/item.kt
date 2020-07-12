package org.selyu.smp.core.item

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class CoreItem(val material: Material, val modelData: Int) {
    abstract fun getDisplayName(): String
    abstract fun getLore(): MutableList<String>

    fun getItem(): ItemStack = ItemStack(material).also {
        if (it.itemMeta == null)
            return@also

        val meta = it.itemMeta!!
        meta.setCustomModelData(modelData)
        meta.setDisplayName(getDisplayName())
        meta.lore = getLore()

        it.itemMeta = meta
    }

    open fun applyExtraData(itemStack: ItemStack): ItemStack = itemStack
    open fun validate(itemStack: ItemStack): Boolean = true
}

@Retention
@Target(AnnotationTarget.CLASS)
annotation class ItemData(val material: Material, val modelData: Int)

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler

data class DamageEntityEvent(val player: Player, val itemStack: ItemStack, val damagedEntity: Entity)
data class LeftClickEvent(val player: Player, val itemStack: ItemStack, val clickedEntity: Entity?, val clickedBlock: Block?)
data class RightClickEvent(val player: Player, val itemStack: ItemStack, val clickedEntity: Entity?, val clickedBlock: Block?)