package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CustomItemCategory {
    SHEARS("Shears"),
    INGOT("Ingots"),
    PICKAXE("Pickaxes"),
    SHOVEL("Shovels"),
    SWORD("Swords"),
    AXE("Axes");

    private final String correctName;

    CustomItemCategory(@NotNull String correctName) {
        this.correctName = correctName;
    }

    @NotNull
    public String getCorrectName() {
        return correctName;
    }
}
