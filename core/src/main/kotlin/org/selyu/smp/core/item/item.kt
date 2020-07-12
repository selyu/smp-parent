package org.selyu.smp.core.item

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface CoreItem {
    fun getItem(): ItemStack
}

@Retention
@Target(AnnotationTarget.CLASS)
annotation class ItemData(val material: Material, val modelData: Int)

data class WrappedCoreItem(val coreItem: CoreItem, val itemData: ItemData)

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler

data class DamageEntityEvent(val player: Player, val itemStack: ItemStack, val damagedEntity: Entity)
data class LeftClickEvent(val player: Player, val itemStack: ItemStack, val clickedEntity: Entity?, val clickedBlock: Block?)
data class RightClickEvent(val player: Player, val itemStack: ItemStack, val clickedEntity: Entity?, val clickedBlock: Block?)