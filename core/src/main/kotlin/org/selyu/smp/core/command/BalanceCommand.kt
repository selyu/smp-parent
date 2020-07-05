package org.selyu.smp.core.command

import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player
import org.selyu.smp.core.manager.ProfileManager
import org.selyu.smp.core.util.color

@Command("balance")
@Alias("bal", "shekels")
class BalanceCommand(private val profileManager: ProfileManager) : CommandBase() {
    @Default
    fun onDefault(sender: Player) {
        profileManager.getProfile(sender.uniqueId).ifPresent {
            sender.sendMessage("&aYou have %s shekels!".format(it.balance).color)
        }
    }
}