package org.selyu.smp.core.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.selyu.smp.core.Core
import org.selyu.smp.core.profile.Profile
import org.selyu.smp.core.util.error
import org.selyu.smp.core.util.info
import org.selyu.smp.core.util.success

@CommandAlias("balance|bal|shekels")
class BalanceCommand : BaseCommand() {
    private val profileManager = Core.getInstance().profileManager
    private val repository = Core.getInstance().repository

    @Default
    @Syntax("[player]")
    fun onDefault(sender: CommandSender, @Optional target: Profile?) {
        when {
            target != null -> {
                sender.info("${target.username} has ${target.balance} shekels!")
            }
            sender is ConsoleCommandSender -> {
                throw InvalidCommandArgument(true)
            }
            else -> {
                profileManager.getProfile((sender as Player).uniqueId).ifPresent {
                    sender.info("You have ${it.balance} shekels!")
                }
            }
        }
    }

    @Subcommand("set")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    @Syntax("<player> <balance>")
    fun onSet(sender: CommandSender, profile: Profile, newBalance: Double) {
        val player = profile.toPlayer()
        profile.balance = newBalance

        if (player != null) {
            player.info("Someone set your balance to $newBalance shekels!")
        } else {
            repository.profileStore.save(profile)
        }

        sender.success("You have set ${profile.getProperUsername()} balance to $newBalance shekels!")
    }

    @Subcommand("add")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    @Syntax("<player> <amount>")
    fun onAdd(sender: CommandSender, profile: Profile, addedBalance: Double) {
        val player = profile.toPlayer()
        profile.addBalance(addedBalance)

        if (player != null) {
            player.info("Someone gave you $addedBalance shekels!")
        } else {
            repository.profileStore.save(profile)
        }

        sender.success("You gave $addedBalance shekels to ${profile.username}!")
    }

    @Subcommand("remove")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    @Syntax("<player> <amount>")
    fun onRemove(sender: CommandSender, profile: Profile, removedBalance: Double) {
        val player = profile.toPlayer()
        val result = profile.removeBalance(removedBalance)
        if (result) {
            if (player != null) {
                player.info("Someone took $removedBalance shekels from you!")
            } else {
                repository.profileStore.save(profile)
            }

            sender.success("You took $removedBalance shekels from ${profile.username}!")
        } else {
            sender.error("${profile.username} only has ${profile.balance} shekels!")
        }
    }
}