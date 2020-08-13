package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneAxeItem extends SimpleDurableCustomItem {
    public HellstoneAxeItem() {
        super(CustomItemType.HELLSTONE_AXE, Material.IRON_AXE, 1, DurabilityUtil.HELLSTONE_INGOT * 3);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Axe";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        'i', 'i', '_',
                        'i', 's', '_',
                        '_', 's', '_'
                )
                .ingredient('s', Material.STICK)
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .build();
    }
}
