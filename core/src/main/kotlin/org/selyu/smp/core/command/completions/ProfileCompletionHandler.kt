package org.selyu.smp.core.command.completions

import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.CommandCompletions
import org.bukkit.Server
import org.bukkit.entity.Player

class ProfileCompletionHandler(private val server: Server) : CommandCompletions.AsyncCommandCompletionHandler<BukkitCommandCompletionContext> {
    override fun getCompletions(context: BukkitCommandCompletionContext): MutableCollection<String> {
        return if (context.sender is Player) {
            println("player")
            server.onlinePlayers.filter { (context.sender as Player).canSee(it) }.map { it.name.toLowerCase() }.toMutableList()
        } else {
            println("console")
            server.onlinePlayers.map { it.name.toLowerCase() }.toMutableList()
        }
    }
}