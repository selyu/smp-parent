package org.selyu.smp.core.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Method

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

class WrappedCoreItem(val parent: CoreItem) {
    private val subscribingMethods = mutableListOf<SubscribedMethod>()

    init {
        subscribingMethods.addAll(
                parent.javaClass.declaredMethods
                        .filter { it.parameterCount == 1 }
                        .filter { Event::class.java.isAssignableFrom(it.parameterTypes[0]) }
                        .map { SubscribedMethod(it, it.parameterTypes[0]) }
        )
    }

    fun runEvent(event: Event) {
        subscribingMethods
                .filter { event.javaClass.isAssignableFrom(it.method.parameterTypes[0]) }
                .forEach { it.method.invoke(parent, event) }
    }

    class SubscribedMethod(val method: Method, val event: Any)
}

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler

interface Event

data class DamageEntityEvent(val player: Player, val itemStack: ItemStack, val damagedEntity: Entity) : Event
data class LeftClickEvent(val player: Player, val itemStack: ItemStack) : Event {
    var clickedEntity: Entity? = null
    var clickedBlock: Block? = null

    constructor(event: PlayerInteractEvent) : this(event.player, event.item!!) {
        clickedBlock = event.clickedBlock
    }

    constructor(itemStack: ItemStack, event: PlayerInteractEntityEvent) : this(event.player, itemStack) {
        clickedEntity = event.rightClicked
    }
}

data class RightClickEvent(val player: Player, val itemStack: ItemStack) : Event {
    var clickedEntity: Entity? = null
    var clickedBlock: Block? = null

    constructor(event: PlayerInteractEvent) : this(event.player, event.item!!) {
        clickedBlock = event.clickedBlock
    }

    constructor(itemStack: ItemStack, event: PlayerInteractEntityEvent) : this(event.player, itemStack) {
        clickedEntity = event.rightClicked
    }
}