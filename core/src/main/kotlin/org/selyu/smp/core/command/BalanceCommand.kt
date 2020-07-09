package org.selyu.smp.core.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.selyu.smp.core.Core
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.profile.Profile
import org.selyu.smp.core.util.*

@CommandAlias("balance|bal|shekels")
class BalanceCommand(private val profileManager: ProfileManager, private val core: Core, private val repository: Repository) : BaseCommand() {
    @Default
    fun onDefault(sender: Player) {
        profileManager.getProfile(sender.uniqueId).ifPresent {
            core.sendComponentMessage(sender, "You have ${it.balance} shekels!".info())
        }
    }

    @Subcommand("set")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    fun onSet(sender: CommandSender, profile: Profile, newBalance: Double) {
        val player = profile.toPlayer()
        profile.balance = newBalance

        if (player != null) {
            val component = "Someone set your balance to $newBalance shekels!".info()
            core.sendComponentMessage(player, component)
        } else {
            repository.profileStore.save(profile)
        }

        val component = "You have set ${profile.getProperUsername()} balance to $newBalance shekels!".success()
        core.sendComponentMessage(sender, component)
    }

    @Subcommand("add|give")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    fun onAdd(sender: CommandSender, profile: Profile, addedBalance: Double) {
        val player = profile.toPlayer()
        profile.addBalance(addedBalance)

        if (player != null) {
            val component = "Someone gave you $addedBalance shekels!".info()
            core.sendComponentMessage(player, component)
        } else {
            repository.profileStore.save(profile)
        }

        val component = "You gave $addedBalance shekels to ${profile.username}!".success()
        core.sendComponentMessage(sender, component)
    }

    @Subcommand("remove|take")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    fun onRemove(sender: CommandSender, profile: Profile, removedBalance: Double) {
        val player = profile.toPlayer()
        val result = profile.removeBalance(removedBalance)
        val component = if (result) {
            if (player != null) {
                val component1 = "Someone took $removedBalance shekels from you!".info()
                core.sendComponentMessage(player, component1)
            } else {
                repository.profileStore.save(profile)
            }

            "You took $removedBalance shekels from ${profile.username}!".success()
        } else {
            "${profile.username} only has ${profile.balance} shekels!".error()
        }

        core.sendComponentMessage(sender, component)
    }
}