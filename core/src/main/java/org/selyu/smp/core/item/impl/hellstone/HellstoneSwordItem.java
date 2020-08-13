package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneSwordItem extends SimpleDurableCustomItem {
    public HellstoneSwordItem() {
        super(CustomItemType.HELLSTONE_SWORD, Material.IRON_SWORD, 1, DurabilityUtil.HELLSTONE_INGOT * 2);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Sword";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        '_', 'i', '_',
                        '_', 'i', '_',
                        '_', 's', '_'
                )
                .ingredient('s', Material.STICK)
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .build();
    }
}
