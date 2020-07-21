package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipe;

public final class HellstoneIngotItem extends CustomItem {
    public HellstoneIngotItem() {
        super(CustomItemType.HELLSTONE_INGOT, Material.PAPER, 1);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Ingot";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        var recipe = new ShapedRecipe(this);
        recipe.setShape(
                'm', 'm', 'm',
                'm', 'i', 'm',
                'm', 'm', 'm'
        );
        recipe.setIngredient('m', Material.MAGMA_BLOCK);
        recipe.setIngredient('i', Material.IRON_INGOT);
        return recipe;
    }
}
