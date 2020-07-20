package org.selyu.smp.core.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import me.idriz.oss.menu.Menu
import org.bukkit.entity.Player
import org.selyu.smp.core.manager.CoreItemManager
import org.selyu.smp.core.menu.recipe.RecipeMenu

@CommandAlias("recipes|r")
class RecipesCommand(private val coreItemManager: CoreItemManager) : BaseCommand() {
    @Default
    fun onDefault(sender: Player) {
        Menu.close(sender)
        Menu.show(sender, RecipeMenu(coreItemManager))
    }
}