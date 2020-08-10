package org.selyu.smp.core.command;

import org.bukkit.entity.Player;
import org.selyu.commands.api.annotation.Command;
import org.selyu.commands.api.annotation.Sender;
import org.selyu.smp.core.menu.recipe.RecipeMenu;

public final class RecipesCommand {
    @Command(name = "", desc = "Displays a recipe book")
    public void onDefault(@Sender Player sender) {
        RecipeMenu.INSTANCE.open(sender);
    }
}
