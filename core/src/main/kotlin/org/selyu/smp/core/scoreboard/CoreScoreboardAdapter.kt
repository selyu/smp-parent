package org.selyu.smp.core.scoreboard

import org.selyu.smp.core.util.rainbow
import org.selyu.ui.scoreboard.adapter.ScoreboardAdapter
import org.selyu.ui.scoreboard.objective.ScoreboardObjective
import org.selyu.ui.scoreboard.title.ScoreboardTitle

class CoreScoreboardAdapter : ScoreboardAdapter {
    override fun getObjectives(): MutableList<ScoreboardObjective> =
            ScoreboardObjective
                    .builder()
                    .addObjective("&dWelcome to the SMP!")
                    .build()

    override fun getTitle(): ScoreboardTitle = ScoreboardTitle.of(false, "Selyu SMP".rainbow()).also {
        it.updateSpeed = 1
    }
}