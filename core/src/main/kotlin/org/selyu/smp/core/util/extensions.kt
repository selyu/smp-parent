package org.selyu.smp.core.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color
import kotlin.math.ceil

private val MINI_MESSAGE = MiniMessage.builder().build()

fun JavaPlugin.registerListeners(vararg listeners: Listener) = listeners.forEach {
    server.pluginManager.registerEvents(it, this)
}

operator fun ChatColor.plus(string: String): String = toString() + string
operator fun ChatColor.plus(char: Char): String = toString() + char

fun String.rainbow(): String {
    val stringBuilder = StringBuilder()
    for (i in indices) {
        if (get(i) == ' ') {
            stringBuilder.append(' ')
            continue
        }

        var hue = ceil((System.currentTimeMillis() + (i * 300)) / 20.0)
        hue %= 360.0

        stringBuilder.append(ChatColor.of(Color.getHSBColor((hue / 360F).toFloat(), 0.9F, 0.8F)))
        stringBuilder.append(get(i))
    }

    return stringBuilder.toString()
}

val String.miniMessage: Component
    get() = MINI_MESSAGE.parse(this)

val String.color: String
    get() = ChatColor.translateAlternateColorCodes('&', this)