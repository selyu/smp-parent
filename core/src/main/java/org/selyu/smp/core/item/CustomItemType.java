package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CustomItemType {
    // DIAMOND
    DIAMOND_SHEARS(CustomItemCategory.SHEARS),

    // HELLSTONE
    HELLSTONE_INGOT(CustomItemCategory.INGOT),
    HELLSTONE_PICKAXE(CustomItemCategory.PICKAXE),
    HELLSTONE_SHOVEL(CustomItemCategory.SHOVEL),
    HELLSTONE_SWORD(CustomItemCategory.SWORD),
    HELLSTONE_AXE(CustomItemCategory.AXE);

    private final CustomItemCategory customItemCategory;

    CustomItemType(@NotNull CustomItemCategory customItemCategory) {
        this.customItemCategory = customItemCategory;
    }

    @NotNull
    public CustomItemCategory getCustomItemCategory() {
        return customItemCategory;
    }
}
