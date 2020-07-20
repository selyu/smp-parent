package org.selyu.smp.core.menu

import fr.minuskube.inv.ClickableItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.selyu.smp.core.util.color
import org.selyu.smp.core.util.ensureMeta

val placeholderItem: ClickableItem = ClickableItem.empty(ItemStack(Material.GRAY_STAINED_GLASS_PANE).also {
    it.ensureMeta()

    val meta = it.itemMeta!!
    meta.setDisplayName("&0".color)
    it.itemMeta = meta
})

val nextPageItemStack: ItemStack = ItemStack(Material.ARROW).also {
    it.ensureMeta()

    val meta = it.itemMeta!!
    meta.setDisplayName("&a>>".color)
    it.itemMeta = meta
}

val prevPageItemStack: ItemStack = ItemStack(Material.ARROW).also {
    it.ensureMeta()

    val meta = it.itemMeta!!
    meta.setDisplayName("&b<<".color)
    it.itemMeta = meta
}

val backItemStack: ItemStack = ItemStack(Material.BARRIER).also {
    it.ensureMeta()

    val meta = it.itemMeta!!
    meta.setDisplayName("&cGo back!".color)
    it.itemMeta = meta
}