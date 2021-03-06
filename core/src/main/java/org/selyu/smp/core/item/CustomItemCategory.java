package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CustomItemCategory {
    INGOT("Ingots"),
    PICKAXE("Pickaxes"),
    SHOVEL("Shovels"),
    SWORD("Swords"),
    AXE("Axes"),
    HELMET("Helmets"),
    CHESTPLATE("Chestplates"),
    LEGGING("Leggings"),
    BOOT("Boots"),
    HOE("Hoes");

    private final String correctName;

    CustomItemCategory(@NotNull String correctName) {
        this.correctName = correctName;
    }

    @NotNull
    public String getCorrectName() {
        return correctName;
    }
}
