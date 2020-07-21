package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItemType;
import org.selyu.smp.core.item.DurableCoreItem;
import org.selyu.smp.core.item.recipe.CoreRecipe;
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey;

import java.util.Collections;
import java.util.List;

public final class DiamondShearsItem extends DurableCoreItem {
    public DiamondShearsItem() {
        super("diamond_shears", CoreItemType.SHEARS, Material.SHEARS, 1, 1478);
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
    public @NotNull CoreRecipe getRecipe() {
        var recipe = new CoreRecipe(this);
        recipe.addKey(new MaterialRecipeKey(Material.DIAMOND, 2));
        return recipe;
    }
}
