package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.DurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey;

import java.util.Collections;
import java.util.List;

public final class DiamondShearsItem extends DurableCustomItem {
    public DiamondShearsItem() {
        super("diamond_shears", CustomItemType.SHEARS, Material.SHEARS, 1, 1478);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fDiamond Shears";
    }

    @NotNull
    @Override
    public List<String> getLore() {
        return Collections.singletonList("&7&oEspecially durable!");
    }

    @Override
    public @NotNull Recipe getRecipe() {
        var recipe = new Recipe(this);
        recipe.addKey(new MaterialRecipeKey(Material.DIAMOND, 2));
        return recipe;
    }
}
