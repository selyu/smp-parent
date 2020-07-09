package org.selyu.smp.core.util

import net.md_5.bungee.api.ChatColor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

fun JavaPlugin.registerListeners(vararg listeners: Listener) = listeners.forEach {
    server.pluginManager.registerEvents(it, this)
}

operator fun ChatColor.plus(string: String): String = toString() + string
operator fun ChatColor.plus(char: Char): String = toString() + char