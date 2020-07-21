package org.selyu.smp.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public final class MessageUtil {
    private MessageUtil() {
    }

    @NotNull
    public static String rainbow(@NotNull String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                stringBuilder.append(' ');
            } else {
                double hue = Math.ceil((System.currentTimeMillis() + (i * 300)) / 20.0);
                hue %= 360.0d;

                stringBuilder.append(ChatColor.of(Color.getHSBColor(((float) hue / 360.0f), 0.9f, 0.8f)));
                stringBuilder.append(string.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static void success(@NotNull CommandSender sender, @NotNull String string) {
        Component component = asComponent("<color:" + (sender instanceof Player ? "#00C851" : "green") + "><bold>!!</bold> " + string);
        Core.getInstance().getBukkitAudiences().audience(sender).sendMessage(component);
    }

    public static void info(@NotNull CommandSender sender, @NotNull String string) {
        Component component = asComponent("<color:" + (sender instanceof Player ? "#33b5e5" : "aqua") + "><bold>!!</bold> " + string);
        Core.getInstance().getBukkitAudiences().audience(sender).sendMessage(component);
    }

    public static void warning(@NotNull CommandSender sender, @NotNull String string) {
        Component component = asComponent("<color:" + (sender instanceof Player ? "#FFbb33" : "yellow") + "><bold>!!</bold> " + string);
        Core.getInstance().getBukkitAudiences().audience(sender).sendMessage(component);
    }

    public static void error(@NotNull CommandSender sender, @NotNull String string) {
        Component component = asComponent("<color:" + (sender instanceof Player ? "#ff4444" : "red") + "><bold>!!</bold> " + string);
        Core.getInstance().getBukkitAudiences().audience(sender).sendMessage(component);
    }

    @NotNull
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @NotNull
    public static List<String> color(List<String> list) {
        return list.stream().map(MessageUtil::color).collect(Collectors.toList());
    }

    @NotNull
    private static Component asComponent(@NotNull String string) {
        return MiniMessage.get().parse(string);
    }
}
