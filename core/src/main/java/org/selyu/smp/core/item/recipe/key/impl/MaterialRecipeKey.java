package org.selyu.smp.core.item.recipe.key.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.recipe.key.RecipeKey;

public final class MaterialRecipeKey extends RecipeKey {
    private final Material material;

    public MaterialRecipeKey(@NotNull Material material, int amount) {
        super(amount, material.name());
        this.material = material;
    }

    public MaterialRecipeKey(@NotNull Material material) {
        this(material, 1);
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack) {
        return super.test(itemStack) && itemStack.getType() == material;
    }
}
