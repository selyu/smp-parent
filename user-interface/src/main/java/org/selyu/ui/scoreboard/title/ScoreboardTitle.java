package org.selyu.ui.scoreboard.title;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.selyu.ui.scoreboard.animation.AnimatedText;

@SuppressWarnings("unused")
public final class ScoreboardTitle extends AnimatedText {
    private ScoreboardTitle(@NotNull String... frames) {
        super(frames);
    }

    public static @NotNull ScoreboardTitle of(@NotNull String frame) {
        return of(true, frame);
    }

    public static @NotNull ScoreboardTitle of(boolean color, @NotNull String frame) {
        if (color)
            frame = ChatColor.translateAlternateColorCodes('&', frame);
        return new ScoreboardTitle(frame);
    }

    public static @NotNull ScoreboardTitle ofFrames(@NotNull String... frames) {
        return ofFrames(true, frames);
    }

    @SuppressWarnings("SameParameterValue")
    public static @NotNull ScoreboardTitle ofFrames(boolean color, @NotNull String... frames) {
        if (color) {
            for (int idx = 0; idx < frames.length; idx++) {
                frames[idx] = ChatColor.translateAlternateColorCodes('&', frames[idx]);
            }
        }

        return new ScoreboardTitle(frames);
    }
}
