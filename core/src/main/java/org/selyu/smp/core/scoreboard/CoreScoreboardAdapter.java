package org.selyu.smp.core.scoreboard;

import org.jetbrains.annotations.NotNull;
import org.selyu.ui.scoreboard.adapter.ScoreboardAdapter;
import org.selyu.ui.scoreboard.objective.ScoreboardObjective;
import org.selyu.ui.scoreboard.title.ScoreboardTitle;

import java.util.List;

import static org.selyu.smp.core.util.MessageUtil.rainbow;

public final class CoreScoreboardAdapter implements ScoreboardAdapter {
    @Override
    public @NotNull ScoreboardTitle getTitle() {
        return ScoreboardTitle.of(false, 1, rainbow("SMP"));
    }

    @Override
    public @NotNull List<ScoreboardObjective> getObjectives() {
        return ScoreboardObjective
                .builder()
                .addObjective("&cHi!", "&bHi!", "&aHi!", "&eHi!")
                .build();
    }
}
