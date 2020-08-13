package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneHelmetItem extends SimpleDurableCustomItem {
    public HellstoneHelmetItem() {
        super(CustomItemType.HELLSTONE_HELMET, Material.IRON_HELMET, 1, DurabilityUtil.HELLSTONE_INGOT * 5);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Helmet";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        'i', 'i', 'i',
                        'i', '_', 'i',
                        '_', '_', '_'
                )
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .build();
    }
}
