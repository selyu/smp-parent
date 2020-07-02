package org.selyu.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.selyu.ui.scoreboard.Scoreboard;
import org.selyu.ui.scoreboard.adapter.ScoreboardAdapter;
import org.selyu.ui.scoreboard.task.ScoreboardTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class ScoreboardManager implements Listener {
    private final JavaPlugin plugin;
    private final UserInterfaceProvider userInterfaceProvider;
    private final Map<UUID, Scoreboard> scoreboardMap = new ConcurrentHashMap<>();

    ScoreboardManager(@NotNull JavaPlugin plugin, @NotNull UserInterfaceProvider userInterfaceProvider, long updateSpeedTicks) {
        this.plugin = plugin;
        this.userInterfaceProvider = userInterfaceProvider;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new ScoreboardTask(scoreboardMap), 0, updateSpeedTicks);
    }

    /**
     * Add the player to the scoreboard map for updating with the task
     *
     * @param player  The player
     * @param adapter The adapter
     */
    public void setBoard(@NotNull Player player, @NotNull ScoreboardAdapter adapter) {
        scoreboardMap.put(player.getUniqueId(), new Scoreboard(
                player.getUniqueId(),
                plugin,
                adapter,
                userInterfaceProvider.getBukkitScoreboard(player)
        ));
    }

    /**
     * Removes the players current scoreboard
     *
     * @param player The player
     */
    public void removeBoard(@NotNull Player player) {
        scoreboardMap.remove(player.getUniqueId());
    }

    public @NotNull Map<UUID, Scoreboard> getScoreboardMap() {
        return scoreboardMap;
    }
}
