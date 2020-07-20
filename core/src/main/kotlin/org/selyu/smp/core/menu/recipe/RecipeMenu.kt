package org.selyu.smp.core.menu.recipe

import me.idriz.oss.menu.MenuItem
import me.idriz.oss.menu.impl.SimpleMenu
import me.idriz.oss.menu.template.MenuTemplate
import org.selyu.smp.core.item.CoreItemType
import org.selyu.smp.core.manager.CoreItemManager
import org.selyu.smp.core.menu.PlaceholderButton

class RecipeMenu(coreItemManager: CoreItemManager) : SimpleMenu("Choose a recipe type!", 3) {
    init {
        MenuTemplate.create(
                "#########",
                "####s####",
                "#########"
        )
                .where('s', RecipeTypeButton(CoreItemType.SHEARS, coreItemManager))
                .where('#', PlaceholderButton())
                .apply(this)
    }

    private class RecipeTypeButton(coreItemType: CoreItemType, coreItemManager: CoreItemManager) : MenuItem(coreItemManager.getMenuItemForType(coreItemType), {})
}