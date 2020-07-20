package org.selyu.smp.core.util

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.selyu.smp.core.item.CoreItem

fun JavaPlugin.registerListeners(vararg listeners: Listener) = listeners.forEach {
    server.pluginManager.registerEvents(it, this)
}

operator fun ChatColor.plus(string: String): String = toString() + string
operator fun ChatColor.plus(char: Char): String = toString() + char

fun ItemStack.ensureMeta() {
    if (!this.hasItemMeta())
        this.itemMeta = Bukkit.getItemFactory().getItemMeta(this.type)
}

fun ItemStack.isCustomItem() = this.hasItemMeta() && this.itemMeta!!.persistentDataContainer.get(CoreItem.INTERNAL_NAME_KEY, PersistentDataType.STRING) != null