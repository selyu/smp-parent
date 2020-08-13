package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneLeggingsItem extends SimpleDurableCustomItem {
    public HellstoneLeggingsItem() {
        super(CustomItemType.HELLSTONE_LEGGINGS, Material.IRON_LEGGINGS, 1, DurabilityUtil.HELLSTONE_INGOT * 7);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Leggings";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        'i', 'i', 'i',
                        'i', '_', 'i',
                        'i', '_', 'i'
                )
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .build();
    }
}
