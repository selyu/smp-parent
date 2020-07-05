package org.selyu.smp.core.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.selyu.smp.core.Core
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.profile.Profile
import org.selyu.smp.core.util.asComponent
import org.selyu.smp.core.util.color

@CommandAlias("balance|bal|shekels")
class BalanceCommand(private val profileManager: ProfileManager, private val core: Core, private val repository: Repository) : BaseCommand() {
    @Default
    fun onDefault(sender: Player) {
        profileManager.getProfile(sender.uniqueId).ifPresent {
            sender.sendMessage("&aYou have ${it.balance} shekels!".color)
        }
    }

    @Subcommand("set")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@profiles")
    fun onSet(sender: CommandSender, profile: Profile, newBalance: Double) {
        val player = profile.toPlayer()
        profile.balance = newBalance

        if (player != null) {
            val component = "<gradient:green:dark_green>Your balance has been set to $newBalance shekels!</gradient>".asComponent
            core.sendComponentMessage(player, component)
        } else {
            repository.profileStore.save(profile)
        }

        val component = "<gradient:green:dark_green>You have set ${profile.getProperUsername()} balance to $newBalance shekels!</gradient>".asComponent
        core.sendComponentMessage(sender, component)
    }
}