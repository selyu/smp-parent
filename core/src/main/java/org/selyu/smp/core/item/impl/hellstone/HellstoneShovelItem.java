package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;

import static org.selyu.smp.core.util.DurabilityUtil.HELLSTONE_INGOT;

public final class HellstoneShovelItem extends SimpleDurableCustomItem {
    public HellstoneShovelItem() {
        super(CustomItemType.HELLSTONE_SHOVEL, Material.IRON_SHOVEL, 1, HELLSTONE_INGOT);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Shovel";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        '_', 'i', '_',
                        '_', 's', '_',
                        '_', 's', '_'
                )
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .ingredient('s', Material.STICK)
                .build();
    }
}
