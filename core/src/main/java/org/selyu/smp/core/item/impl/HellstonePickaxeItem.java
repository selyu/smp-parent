package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.DurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.key.impl.InternalNameRecipeKey;
import org.selyu.smp.core.item.recipe.key.impl.MaterialRecipeKey;

public final class HellstonePickaxeItem extends DurableCustomItem {
    public HellstonePickaxeItem() {
        super("hellstone_pickaxe", CustomItemType.PICKAXE, Material.IRON_PICKAXE, 1, 1000);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Pickaxe";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        var recipe = new Recipe(this);
        recipe.addKey(new InternalNameRecipeKey("hellstone_ingot", 3));
        recipe.addKey(new MaterialRecipeKey(Material.STICK, 2));
        return recipe;
    }
}
