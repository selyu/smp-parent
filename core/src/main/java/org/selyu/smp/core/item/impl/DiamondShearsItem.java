package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.DurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipe;

import java.util.Arrays;
import java.util.List;

public final class DiamondShearsItem extends DurableCustomItem {
    public DiamondShearsItem() {
        super(CustomItemType.DIAMOND_SHEARS, Material.SHEARS, 1, 1478);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fDiamond Shears";
    }

    @NotNull
    @Override
    public List<String> getLore() {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList("&7&oEspecially durable!");
    }

    @Override
    public @NotNull Recipe getRecipe() {
        var recipe = new ShapedRecipe(this);
        recipe.setShape(
                '_', 'd', '_',
                'd', '_', '_',
                '_', '_', '_'
        );
        recipe.setIngredient('d', Material.DIAMOND);
        return recipe;
    }
}
