package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.DurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipe;

public final class HellstonePickaxeItem extends DurableCustomItem {
    public HellstonePickaxeItem() {
        super(CustomItemType.HELLSTONE_PICKAXE, Material.IRON_PICKAXE, 1, 1000);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Pickaxe";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        var recipe = new ShapedRecipe(this);
        recipe.setShape(
                'i', 'i', 'i',
                '_', 's', '_',
                '_', 's', '_'
        );
        recipe.setIngredient('i', CustomItemType.HELLSTONE_INGOT);
        recipe.setIngredient('s', Material.STICK);
        return recipe;
    }
}
