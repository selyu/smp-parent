package org.selyu.smp.core.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;
import org.selyu.smp.core.menu.recipe.RecipeMenu;

@CommandAlias("recipes")
public final class RecipesCommand extends BaseCommand {
    @Default
    public void onDefault(Player sender) {
        RecipeMenu.INSTANCE.open(sender);
    }
}
