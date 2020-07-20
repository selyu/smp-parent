package org.selyu.smp.core.manager

import net.md_5.bungee.api.ChatColor
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.CoreItemType
import org.selyu.smp.core.item.DurableCoreItem
import org.selyu.smp.core.item.ItemEventHandler
import org.selyu.smp.core.item.impl.DiamondShearsItem
import org.selyu.smp.core.item.impl.HellstoneIngotItem
import org.selyu.smp.core.util.ensureMeta
import org.selyu.smp.core.util.plus
import java.lang.reflect.Method

class CoreItemManager {
    private val items = listOf(
            DiamondShearsItem(),
            HellstoneIngotItem()
    )

    private val subscribersMap = wrap(items)

    fun runEvent(event: Event, itemStack: ItemStack) {
        val subscribers = subscribersMap[event.javaClass] ?: return
        subscribers.forEach {
            if (it.parent.material == itemStack.type && it.parent.modelData == itemStack.itemMeta!!.customModelData && it.parent.validate(itemStack))
                it.method.invoke(it.parent, event)
        }
    }

    fun getMenuItemForType(coreItemType: CoreItemType): ItemStack {
        val item = items.firstOrNull { it.coreItemType == coreItemType }
                ?: throw NullPointerException("No items with type $coreItemType")
        val itemStack = ItemStack(item.material)
        itemStack.ensureMeta()

        val meta = itemStack.itemMeta!!
        meta.setDisplayName(ChatColor.RESET + coreItemType.correctName)
        meta.setCustomModelData(item.modelData)

        itemStack.itemMeta = meta
        return itemStack
    }

    fun getCraftableItemsForType(coreItemType: CoreItemType): List<CoreItem> = items
            .filter { it.getRecipe() != null }
            .filter { it.coreItemType == coreItemType }

    private fun wrap(coreItems: List<CoreItem>): Map<Class<*>, MutableSet<SubscribedMethod>> {
        val map = hashMapOf<Class<*>, MutableSet<SubscribedMethod>>()

        fun addMethods(coreItem: CoreItem, methods: Array<Method>) {
            methods.forEach {
                if (!it.isAnnotationPresent(ItemEventHandler::class.java) || it.parameterCount != 1 || !Event::class.java.isAssignableFrom(it.parameterTypes[0]) || !it.trySetAccessible())
                    return@forEach

                val event = it.parameterTypes[0]
                val mapValue = map.getOrDefault(event, hashSetOf())
                mapValue.add(SubscribedMethod(coreItem, it))

                map[event] = mapValue
            }
        }

        coreItems.forEach {
            addMethods(it, it.javaClass.declaredMethods)
            if (it is DurableCoreItem)
                addMethods(it, it.javaClass.superclass.declaredMethods)
        }

        return map
    }

    private data class SubscribedMethod(val parent: CoreItem, val method: Method)
}