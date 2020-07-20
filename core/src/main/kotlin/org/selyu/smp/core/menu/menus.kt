package org.selyu.smp.core.menu

import me.idriz.oss.menu.MenuItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.util.color

class PlaceholderButton : MenuItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE).also {
    if (it.hasItemMeta()) {
        val meta = it.itemMeta!!
        meta.setDisplayName("&0".color)
        it.itemMeta = meta
    }
}, {})