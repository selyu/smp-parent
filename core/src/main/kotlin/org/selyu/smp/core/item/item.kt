package org.selyu.smp.core.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Event
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
    private val subscribingMethods = mutableListOf<Method>()

    init {
        subscribingMethods.addAll(
                parent.javaClass.declaredMethods
                        .filter { it.parameterCount == 1 }
                        .filter { Event::class.java.isAssignableFrom(it.parameterTypes[0]) }
        )
    }

    fun runEvent(event: Event) {
        subscribingMethods
                .filter { event.javaClass.isAssignableFrom(it.parameterTypes[0]) }
                .forEach { it.invoke(parent, event) }
    }
}

fun Iterable<WrappedCoreItem>.getValidItems(against: ItemStack): Iterable<WrappedCoreItem> {
    return this
            .filter { wrapped -> wrapped.parent.material == against.type }
            .filter { wrapped -> wrapped.parent.modelData == against.itemMeta!!.customModelData }
            .filter { wrapped -> wrapped.parent.validate(against) }
}

@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class ItemEventHandler