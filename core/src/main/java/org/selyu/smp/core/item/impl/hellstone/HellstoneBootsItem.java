package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneBootsItem extends SimpleDurableCustomItem {
    public HellstoneBootsItem() {
        super(CustomItemType.HELLSTONE_BOOTS, Material.IRON_BOOTS, 1, DurabilityUtil.HELLSTONE_INGOT * 4);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Boots";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        'i', '_', 'i',
                        'i', '_', 'i',
                        '_', '_', '_'
                )
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .build();
    }
}
