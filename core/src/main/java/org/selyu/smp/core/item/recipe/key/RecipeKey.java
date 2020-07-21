package org.selyu.smp.core.item.recipe.key;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class RecipeKey implements Predicate<ItemStack> {
    private final int amount;
    private final String needed;

    public RecipeKey(int amount, @NotNull String needed) {
        this.amount = amount;
        this.needed = needed;
    }

    public RecipeKey(@NotNull String needed) {
        this(1, needed);
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return itemStack.getAmount() >= amount;
    }

    public int getAmount() {
        return amount;
    }

    @NotNull
    public String getNeeded() {
        return needed;
    }
}
