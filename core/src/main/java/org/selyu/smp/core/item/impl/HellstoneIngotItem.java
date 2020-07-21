package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItem;
import org.selyu.smp.core.item.CoreItemType;
import org.selyu.smp.core.item.recipe.CoreRecipe;
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey;

public final class HellstoneIngotItem extends CoreItem {
    public HellstoneIngotItem() {
        super("hellstone_ingot", CoreItemType.INGOT, Material.PAPER, 1);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Ingot";
    }

    @Override
    public @NotNull CoreRecipe getRecipe() {
        var recipe = new CoreRecipe(this);
        recipe.addKey(new MaterialRecipeKey(Material.MAGMA_BLOCK, 8));
        recipe.addKey(new MaterialRecipeKey(Material.IRON_INGOT, 1));
        return recipe;
    }
}
