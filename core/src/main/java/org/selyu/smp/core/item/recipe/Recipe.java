package org.selyu.smp.core.item.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.recipe.key.RecipeKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Recipe {
    private final CustomItem customItem;
    private final Set<RecipeKey> keys = new HashSet<>();

    public Recipe(@NotNull CustomItem customItem) {
        this.customItem = customItem;
    }

    public void addKey(@NotNull RecipeKey recipeKey) {
        keys.add(recipeKey);
    }

    public boolean canCraft(@NotNull PlayerInventory playerInventory) {
        var matchedKeys = new HashSet<RecipeKey>();
        var updatedItems = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < playerInventory.getContents().length; i++) {
            var itemStack = playerInventory.getItem(i);
            if (itemStack == null || itemStack.getType() == Material.AIR)
                continue;

            for (RecipeKey key : keys) {
                if (matchedKeys.contains(key))
                    continue;

                if (key.test(itemStack)) {
                    var newItemStack = itemStack.clone();
                    newItemStack.setAmount(newItemStack.getAmount() - key.getAmount());

                    updatedItems.put(i, newItemStack);
                    matchedKeys.add(key);
                }
            }
        }

        if (matchedKeys.size() == keys.size())
            updatedItems.forEach(playerInventory::setItem);

        return matchedKeys.size() == keys.size();
    }

    @NotNull
    public ItemStack getFinalItem() {
        return customItem.getItem().clone();
    }

    @NotNull
    public Set<RecipeKey> getKeys() {
        return keys;
    }
}
