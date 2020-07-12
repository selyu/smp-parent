package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.selyu.smp.core.item.CoreItem
import org.selyu.smp.core.util.color

class ExampleCoreItem : CoreItem(Material.DIAMOND_PICKAXE, 1) {
    override fun getDisplayName(): String = "&bExample Pickaxe".color

    override fun getLore(): MutableList<String> = mutableListOf("&cExample Lore".color)
}