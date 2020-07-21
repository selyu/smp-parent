package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItemType;
import org.selyu.smp.core.item.DurableCoreItem;
import org.selyu.smp.core.item.recipe.CoreRecipe;
import org.selyu.smp.core.item.recipe.key.impl.InternalNameRecipeKey;
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey;

public final class HellstonePickaxeItem extends DurableCoreItem {
    public HellstonePickaxeItem() {
        super("hellstone_pickaxe", CoreItemType.PICKAXE, Material.IRON_PICKAXE, 1, 1000);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Pickaxe";
    }

    @Override
    public @NotNull CoreRecipe getRecipe() {
        var recipe = new CoreRecipe(this);
        recipe.addKey(new InternalNameRecipeKey("hellstone_ingot", 3));
        recipe.addKey(new MaterialRecipeKey(Material.STICK, 2));
        return recipe;
    }
}
