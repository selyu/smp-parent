package org.selyu.smp.core.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public final class MessageUtil {
    public static final ChatColor SUCCESS = ChatColor.of("#00C851");
    public static final ChatColor INFO = ChatColor.of("#33b5e5");
    public static final ChatColor WARNING = ChatColor.of("#FFbb33");
    public static final ChatColor ERROR = ChatColor.of("#ff4444");
    private static final String FORMAT = "%1$s" + ChatColor.BOLD + "!!%1$s %2$s";

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

    @Nonnull
    public static String[] color(@Nonnull String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = color(strings[i]);
        }
        return strings;
    }

    @Nonnull
    public static String success(@Nonnull String string, @Nonnull Object... placeholders) {
        return String.format(FORMAT, SUCCESS, String.format(string, placeholders));
    }

    @Nonnull
    public static String info(@Nonnull String string, @Nonnull Object... placeholders) {
        return String.format(FORMAT, INFO, String.format(string, placeholders));
    }

    @Nonnull
    public static String warning(@Nonnull String string, @Nonnull Object... placeholders) {
        return String.format(FORMAT, WARNING, String.format(string, placeholders));
    }

    @Nonnull
    public static String error(@Nonnull String string, @Nonnull Object... placeholders) {
        return String.format(FORMAT, ERROR, String.format(string, placeholders));
    }

    public static void success(@Nonnull CommandSender commandSender, @Nonnull String string, @Nonnull Object... placeholders) {
        commandSender.sendMessage(success(string, placeholders));
    }

    public static void info(@Nonnull CommandSender commandSender, @Nonnull String string, @Nonnull Object... placeholders) {
        commandSender.sendMessage(info(string, placeholders));

    }

    public static void warning(@Nonnull CommandSender commandSender, @Nonnull String string, @Nonnull Object... placeholders) {
        commandSender.sendMessage(warning(string, placeholders));
    }

    public static void error(@Nonnull CommandSender commandSender, @Nonnull String string, @Nonnull Object... placeholders) {
        commandSender.sendMessage(error(string, placeholders));
    }

    @NotNull
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @NotNull
    public static List<String> color(List<String> list) {
        return list.stream().map(MessageUtil::color).collect(Collectors.toList());
    }
}
