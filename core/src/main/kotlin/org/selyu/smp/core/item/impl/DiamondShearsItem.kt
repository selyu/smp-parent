package org.selyu.smp.core.item.impl

import org.bukkit.Material
import org.selyu.smp.core.item.DurableCoreItem
import org.selyu.smp.core.util.color

class DiamondShearsItem : DurableCoreItem("diamond_shears", Material.SHEARS, 1, 1487) {
    override fun getDisplayName(): String = "&bDiamond Shears".color
    override fun getLore(): MutableList<String> = mutableListOf("&7&oEspecially durable!".color)
}