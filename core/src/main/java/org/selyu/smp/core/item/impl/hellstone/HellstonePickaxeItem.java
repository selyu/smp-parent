package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipeBuilder;

import static org.selyu.smp.core.util.DurabilityUtil.HELLSTONE_INGOT;

public final class HellstonePickaxeItem extends SimpleDurableCustomItem {
    public HellstonePickaxeItem() {
        super(CustomItemType.HELLSTONE_PICKAXE, Material.IRON_PICKAXE, 1, HELLSTONE_INGOT * 3);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "&fHellstone Pickaxe";
    }

    @Override
    public @NotNull Recipe getRecipe() {
        return new ShapedRecipeBuilder(this)
                .shape(
                        'i', 'i', 'i',
                        '_', 's', '_',
                        '_', 's', '_'
                )
                .ingredient('i', CustomItemType.HELLSTONE_INGOT)
                .ingredient('s', Material.STICK)
                .build();
    }
}
