package org.selyu.smp.core.item.recipe.key.impl;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItem;
import org.selyu.smp.core.item.recipe.key.RecipeKey;

public final class InternalNameRecipeKey extends RecipeKey {
    private final String internalName;

    public InternalNameRecipeKey(@NotNull String internalName, int amount) {
        super(amount, internalName);
        this.internalName = internalName;
    }

    public InternalNameRecipeKey(@NotNull String internalName) {
        super(1, internalName);
        this.internalName = internalName;
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack) {
        return super.test(itemStack) && internalName.equals(itemStack.getItemMeta().getPersistentDataContainer().get(CoreItem.getINTERNAL_NAME_KEY(), PersistentDataType.STRING));
    }
}
