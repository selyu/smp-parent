package org.selyu.smp.core.manager

import org.selyu.smp.core.Core
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.item.WrappedCoreItem
import org.selyu.smp.core.item.impl.DiamondShearsItem

class CoreItemManager(core: Core) {
    val items = wrap(
            DiamondShearsItem()
    )

    init {
        items.forEach {
            val recipe = it.parent.getRecipe(core)
            if (recipe != null)
                core.server.addRecipe(recipe)
        }
    }

    private fun wrap(vararg coreItems: CoreItem): List<WrappedCoreItem> = coreItems.map { WrappedCoreItem(it) }
}