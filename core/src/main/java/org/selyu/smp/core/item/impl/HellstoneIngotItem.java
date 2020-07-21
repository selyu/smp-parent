package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey;

public final class HellstoneIngotItem extends CustomItem {
    public HellstoneIngotItem() {
        super("hellstone_ingot", CustomItemType.INGOT, Material.PAPER, 1);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Ingot";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        var recipe = new Recipe(this);
        recipe.addKey(new MaterialRecipeKey(Material.MAGMA_BLOCK, 8));
        recipe.addKey(new MaterialRecipeKey(Material.IRON_INGOT, 1));
        return recipe;
    }
}
