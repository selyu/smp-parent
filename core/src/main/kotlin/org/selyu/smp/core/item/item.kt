package org.selyu.smp.core.item

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.selyu.smp.core.Core
import org.selyu.smp.core.util.color
import org.selyu.smp.core.util.ensureMeta
import java.util.concurrent.ThreadLocalRandom

enum class CoreItemType(val correctName: String) {
    SHEARS("Shears");
}

abstract class CoreItem(private val internalName: String, val coreItemType: CoreItemType, val material: Material, val modelData: Int) {
    companion object {
        @JvmStatic
        val INTERNAL_NAME_KEY = Core.keyOf("internal_name")
    }

    protected open fun itemOf(displayName: String = "", lore: MutableList<String> = mutableListOf()) = ItemStack(material).also {
        it.ensureMeta()

        val meta = it.itemMeta!!
        if (displayName.isNotBlank())
            meta.setDisplayName(displayName)
        if (lore.isNotEmpty())
            meta.lore = lore.toMutableList()
        meta.setCustomModelData(modelData)
        meta.persistentDataContainer.set(INTERNAL_NAME_KEY, PersistentDataType.STRING, internalName)

        it.itemMeta = meta
    }

    fun getMenuItem(): ItemStack = ItemStack(material).also {
        it.ensureMeta()

        val meta = it.itemMeta!!
        meta.setDisplayName(getDisplayName().color)
        meta.setCustomModelData(modelData)

        it.itemMeta = meta
    }

    abstract fun getDisplayName(): String
    open fun getLore(): MutableList<String> = mutableListOf()

    open fun getItem(): ItemStack = itemOf(getDisplayName().color, getLore().color as MutableList<String>)
    open fun validate(itemStack: ItemStack): Boolean = true
    open fun getRecipe(): CoreRecipe? = null
}

abstract class DurableCoreItem(internalName: String, coreItemType: CoreItemType, material: Material, modelData: Int, private val maxDurability: Int) : CoreItem(internalName, coreItemType, material, modelData) {
    companion object {
        @JvmStatic
        val DURABILITY_KEY = Core.keyOf("durability")
    }

    private fun addDurability(list: MutableList<String>, durability: Int): MutableList<String> {
        list.add("")
        list.add("&fDurability: $durability / $maxDurability".color)
        return list
    }

    override fun itemOf(displayName: String, lore: MutableList<String>): ItemStack {
        return super.itemOf(displayName, lore).also {
            val meta = it.itemMeta ?: throw NullPointerException("Meta is null!")
            meta.isUnbreakable = true

            meta.lore = addDurability(lore, maxDurability)
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
            meta.persistentDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, maxDurability)

            it.itemMeta = meta
        }
    }

    private fun handleDurability(player: Player, itemStack: ItemStack, equipmentSlot: EquipmentSlot = EquipmentSlot.HAND, setItemInSlot: Boolean = false) {
        val meta = itemStack.itemMeta ?: throw NullPointerException("Meta is null!")

        val chanceToNegate = if (meta.hasEnchant(Enchantment.DURABILITY)) {
            val level = meta.getEnchantLevel(Enchantment.DURABILITY)
            100 / (level + 1)
        } else {
            0
        }
        val randomNumber = ThreadLocalRandom.current().nextInt(1, 100)
        val newDurability = if (randomNumber <= chanceToNegate) {
            meta.persistentDataContainer.getOrDefault(DURABILITY_KEY, PersistentDataType.INTEGER, maxDurability)
        } else {
            meta.persistentDataContainer.getOrDefault(DURABILITY_KEY, PersistentDataType.INTEGER, maxDurability) - 1
        }

        if (newDurability == -1) {
            player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            player.equipment?.setItem(equipmentSlot, null)
        } else {
            meta.lore = addDurability(getLore(), newDurability)
            meta.persistentDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, newDurability)
            itemStack.itemMeta = meta

            if (setItemInSlot)
                player.equipment?.setItem(equipmentSlot, itemStack)
        }
    }

    @ItemEventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.player.gameMode != GameMode.CREATIVE)
            handleDurability(event.player, event.player.equipment!!.itemInMainHand)
    }

    @ItemEventHandler
    fun onShear(event: PlayerShearEntityEvent) {
        if (event.player.gameMode != GameMode.CREATIVE)
            handleDurability(event.player, event.item, event.hand, true)
    }
}

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler