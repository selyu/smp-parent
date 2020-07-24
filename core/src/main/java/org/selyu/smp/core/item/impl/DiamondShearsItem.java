package org.selyu.smp.core.item.impl;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.DurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;

import java.util.List;

import static org.selyu.smp.core.util.DurabilityUtil.DIAMOND;

public final class DiamondShearsItem extends DurableCustomItem {
    public DiamondShearsItem() {
        super(CustomItemType.DIAMOND_SHEARS, Material.SHEARS, 1, DIAMOND * 2);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fDiamond Shears";
    }

    @NotNull
    @Override
    public List<String> getLore() {
        return Lists.newArrayList("&7&oEspecially durable!");
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        '_', '_', '_',
                        '_', '_', 'd',
                        '_', 'd', '_'
                )
                .ingredient('d', Material.DIAMOND)
                .build();
    }
}
