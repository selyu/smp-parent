package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CustomItemType {
    // INGOTS
    HELLSTONE_INGOT(CustomItemCategory.INGOT),
    // PICKAXES
    HELLSTONE_PICKAXE(CustomItemCategory.PICKAXE),
    // SHEARS
    DIAMOND_SHEARS(CustomItemCategory.SHEARS);

    private final CustomItemCategory customItemCategory;

    CustomItemType(@NotNull CustomItemCategory customItemCategory) {
        this.customItemCategory = customItemCategory;
    }

    @NotNull
    public CustomItemCategory getCustomItemCategory() {
        return customItemCategory;
    }
}
