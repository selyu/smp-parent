package org.selyu.smp.core.item.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;

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
        return new ShapedRecipeBuilder(this)
                .shape(
                        'm', 'm', 'm',
                        'm', 'i', 'm',
                        'm', 'm', 'm'
                )
                .ingredient('m', Material.MAGMA_BLOCK)
                .ingredient('i', Material.IRON_INGOT)
                .build();
    }
}
