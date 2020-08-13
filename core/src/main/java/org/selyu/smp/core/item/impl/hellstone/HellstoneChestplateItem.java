package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneChestplateItem extends SimpleDurableCustomItem {
    public HellstoneChestplateItem() {
        super(CustomItemType.HELLSTONE_CHESTPLATE, Material.IRON_CHESTPLATE, 1, DurabilityUtil.HELLSTONE_INGOT * 8);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Chestplate";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        'i', '_', 'i',
                        'i', 'i', 'i',
                        'i', 'i', 'i'
                )
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .build();
    }
}
