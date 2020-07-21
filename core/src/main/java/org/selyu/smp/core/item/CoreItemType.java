package org.selyu.smp.core.item;

import org.jetbrains.annotations.NotNull;

public enum CoreItemType {
    SHEARS("Shears"),
    INGOT("Ingots"),
    PICKAXE("Pickaxes");

    private final String correctName;

    CoreItemType(@NotNull String correctName) {
        this.correctName = correctName;
    }

    @NotNull
    public String getCorrectName() {
        return correctName;
    }
}
