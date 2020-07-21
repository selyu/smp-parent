package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CustomItemType {
    SHEARS("Shears"),
    INGOT("Ingots"),
    PICKAXE("Pickaxes");

    private final String correctName;

    CustomItemType(@NotNull String correctName) {
        this.correctName = correctName;
    }

    @NotNull
    public String getCorrectName() {
        return correctName;
    }
}
