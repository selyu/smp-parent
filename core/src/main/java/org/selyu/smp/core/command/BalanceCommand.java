package org.selyu.smp.core.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.manager.ProfileManager;
import org.selyu.smp.core.profile.Profile;

import static org.selyu.smp.core.util.MessagesKt.*;

@CommandAlias("balance|bal|shekels")
public final class BalanceCommand extends BaseCommand {
    private final ProfileManager profileManager = Core.getInstance().getProfileManager();
    private final Repository repository = Core.getInstance().getRepository();

    @Default
    @Syntax("[player]")
    public void onDefault(CommandSender sender, @Optional Profile target) {
        if (target != null) {
            info(sender, String.format("%s has %s shekels!", target.getUsername(), target.getBalance()));
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            throw new InvalidCommandArgument(true);
        } else {
            profileManager
                    .getByUUID(((Player) sender).getUniqueId())
                    .ifPresent((profile) -> info(sender, String.format("You have %s shekels!", profile.getBalance())));
        }
    }

    @Subcommand("set")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    @Syntax("<player> <new balance>")
    public void onSet(CommandSender sender, Profile target, double balance) {
        var targetPlayer = target.toPlayer();
        target.setBalance(balance);

        if (targetPlayer != null) {
            info(targetPlayer, String.format("Someone set your balance to %s!", balance));
        } else {
            repository.getProfileStore().save(target);
        }

        success(sender, String.format("You set %s balance to %s!", target.getProperUsername(), balance));
    }

    @Subcommand("add")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    @Syntax("<player> <amount>")
    public void onAdd(CommandSender sender, Profile target, double amount) {
        var targetPlayer = target.toPlayer();
        target.addBalance(amount);

        if (targetPlayer != null) {
            info(targetPlayer, String.format("Someone added %s to your balance!", amount));
        } else {
            repository.getProfileStore().save(target);
        }

        success(sender, String.format("You added %s to %s balance!", amount, target.getProperUsername()));
    }

    @Subcommand("remove")
    @CommandPermission("core.balance.edit")
    @CommandCompletion("@players")
    @Syntax("<player> <amount>")
    public void onRemove(CommandSender sender, Profile target, double amount) {
        var targetPlayer = target.toPlayer();
        var result = target.removeBalance(amount);
        if (result) {
            if (targetPlayer != null) {
                info(targetPlayer, String.format("Someone took %s from your balance!", amount));
            } else {
                repository.getProfileStore().save(target);
            }

            success(sender, String.format("You took %s from %s balance!", amount, target.getProperUsername()));
        } else {
            error(sender, String.format("%s only has %s shekels!", target.getUsername(), target.getBalance()));
        }
    }
}
