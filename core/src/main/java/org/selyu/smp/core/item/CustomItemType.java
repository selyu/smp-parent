package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CustomItemType {
    // HELLSTONE
    HELLSTONE_INGOT(CustomItemCategory.INGOT),
    HELLSTONE_PICKAXE(CustomItemCategory.PICKAXE),
    HELLSTONE_SHOVEL(CustomItemCategory.SHOVEL),
    HELLSTONE_SWORD(CustomItemCategory.SWORD),
    HELLSTONE_AXE(CustomItemCategory.AXE),
    HELLSTONE_HOE(CustomItemCategory.HOE),
    HELLSTONE_HELMET(CustomItemCategory.HELMET),
    HELLSTONE_CHESTPLATE(CustomItemCategory.CHESTPLATE),
    HELLSTONE_LEGGINGS(CustomItemCategory.LEGGING),
    HELLSTONE_BOOTS(CustomItemCategory.BOOT),

    ;

    private final CustomItemCategory customItemCategory;

    CustomItemType(@NotNull CustomItemCategory customItemCategory) {
        this.customItemCategory = customItemCategory;
    }

    @NotNull
    public CustomItemCategory getCustomItemCategory() {
        return customItemCategory;
    }
}
