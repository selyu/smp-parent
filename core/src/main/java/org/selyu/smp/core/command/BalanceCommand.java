package org.selyu.smp.core.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.selyu.commands.api.annotation.Command;
import org.selyu.commands.api.annotation.OptArg;
import org.selyu.commands.api.annotation.Require;
import org.selyu.commands.api.annotation.Sender;
import org.selyu.commands.api.exception.CommandExitMessage;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.manager.ProfileManager;
import org.selyu.smp.core.profile.Profile;

import static org.selyu.smp.core.util.MessageUtil.*;

public final class BalanceCommand {
    private final ProfileManager profileManager = Core.getInstance().getProfileManager();
    private final Repository repository = Core.getInstance().getRepository();

    @Command(name = "", desc = "Get a players balance")
    public void onDefault(@Sender CommandSender sender, @OptArg Profile target) throws CommandExitMessage {
        if (target != null) {
            info(sender, "%s has %s shekels!", target.getUsername(), target.getBalance());
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            throw new CommandExitMessage(error("You must specify a player!"));
        } else {
            profileManager
                    .getByUUID(((Player) sender).getUniqueId())
                    .ifPresent((profile) -> info(sender, "You have %s shekels!", profile.getBalance()));
        }
    }

    @Command(name = "set", desc = "Set a players balance to specified amount")
    @Require("core.balance.edit")
    public void onSet(@Sender CommandSender sender, Profile target, double balance) {
        Player targetPlayer = target.toPlayer();
        target.setBalance(balance);

        if (targetPlayer != null) {
            info(targetPlayer, "Someone set your balance to %s!", balance);
        } else {
            repository.getProfileStore().save(target).join();
        }

        success(sender, "You set %s balance to %s!", target.getProperUsername(), balance);
    }

    @Command(name = "add", desc = "Give some shekels to a player")
    @Require("core.balance.edit")
    public void onAdd(@Sender CommandSender sender, Profile target, double amount) {
        Player targetPlayer = target.toPlayer();
        target.addBalance(amount);

        if (targetPlayer != null) {
            info(targetPlayer, "Someone added %s to your balance!", amount);
        } else {
            repository.getProfileStore().save(target).join();
        }

        success(sender, "You added %s to %s balance!", amount, target.getProperUsername());
    }

    @Command(name = "remove", desc = "Remove some shekels from a player")
    @Require("core.balance.edit")
    public void onRemove(@Sender CommandSender sender, Profile target, double amount) {
        Player targetPlayer = target.toPlayer();
        boolean result = target.removeBalance(amount);
        if (result) {
            if (targetPlayer != null) {
                info(targetPlayer, "Someone took %s from your balance!", amount);
            } else {
                repository.getProfileStore().save(target).join();
            }

            success(sender, "You took %s from %s balance!", amount, target.getProperUsername());
        } else {
            error(sender, "%s only has %s shekels!", target.getUsername(), target.getBalance());
        }
    }
}
