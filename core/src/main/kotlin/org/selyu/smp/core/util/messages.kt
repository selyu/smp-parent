package org.selyu.smp.core.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.selyu.smp.core.Core
import java.awt.Color
import kotlin.math.ceil

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

fun CommandSender.success(text: String) {
    val component = "<color:${if (this is Player) "#00C851" else "green"}><bold>!!</bold> $text".asComponent
    Core.getInstance().bukkitAudiences.audience(this).sendMessage(component)
}

fun CommandSender.info(text: String) {
    val component = "<color:${if (this is Player) "#33b5e5" else "aqua"}><bold>!!</bold> $text".asComponent
    Core.getInstance().bukkitAudiences.audience(this).sendMessage(component)
}

fun CommandSender.warning(text: String) {
    val component = "<color:${if (this is Player) "#FFbb33" else "yellow"}><bold>!!</bold> $text".asComponent
    Core.getInstance().bukkitAudiences.audience(this).sendMessage(component)
}

fun CommandSender.error(text: String) {
    val component = "<color:${if (this is Player) "#ff4444" else "red"}><bold>!!</bold> $text".asComponent
    Core.getInstance().bukkitAudiences.audience(this).sendMessage(component)
}

val String.asComponent: Component
    get() = MiniMessage.get().parse(this)

val String.color: String
    get() = ChatColor.translateAlternateColorCodes('&', this)

val Collection<String>.color: Collection<String>
    get() = this.map { it.color }