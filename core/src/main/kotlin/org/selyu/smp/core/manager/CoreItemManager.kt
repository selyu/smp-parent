package org.selyu.smp.core.manager

import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.DurableCoreItem
import org.selyu.smp.core.item.ItemEventHandler
import org.selyu.smp.core.item.impl.DiamondShearsItem
import java.lang.reflect.Method

class CoreItemManager {
    private val items = listOf(
            DiamondShearsItem()
    )

    private val subscribersMap = wrap(items)

    fun runEvent(event: Event, itemStack: ItemStack) {
        val subscribers = subscribersMap[event.javaClass] ?: return
        subscribers.forEach {
            if (it.parent.material == itemStack.type && it.parent.modelData == itemStack.itemMeta!!.customModelData && it.parent.validate(itemStack))
                it.method.invoke(it.parent, event)
        }
    }

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