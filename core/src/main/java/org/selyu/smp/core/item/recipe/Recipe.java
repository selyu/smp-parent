package org.selyu.smp.core.item.recipe;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Recipe {
    @NotNull
    ItemStack getDisplayItem();

    @NotNull
    ItemStack getFinalItem();

    @NotNull
    org.bukkit.inventory.Recipe toBukkitRecipe();
}
