package org.selyu.smp.core.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
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

fun String.success(): Component = "<color:#00C851><bold>!!</bold> $this".asComponent
fun String.info(): Component = "<color:#33b5e5><bold>!!</bold> $this".asComponent
fun String.warning(): Component = "<color:#FFbb33><bold>!!</bold> $this".asComponent
fun String.error(): Component = "<color:#ff4444><bold>!!</bold> $this".asComponent

val String.asComponent: Component
    get() = MiniMessage.get().parse(this)

val String.color: String
    get() = ChatColor.translateAlternateColorCodes('&', this)