package org.selyu.smp.core.item.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;

import java.util.HashMap;
import java.util.Map;

import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;

public final class ShapedRecipe implements Recipe {
    private final Map<Character, Object> charLookupMap = new HashMap<>();
    private final CustomItem customItem;
    private char[] shape;
    private org.bukkit.inventory.Recipe bukkitRecipe;
    private Object[] matrix;

    public ShapedRecipe(@NotNull CustomItem customItem) {
        this.customItem = customItem;
    }

    public void setShape(char... shape) {
        if (shape.length != 9)
            throw new IllegalArgumentException("shape size must be 9");
        this.shape = shape;
    }

    public void setIngredient(char c, @NotNull Material material) {
        charLookupMap.put(c, material);
    }

    public void setIngredient(char c, @NotNull CustomItemType customItemType) {
        charLookupMap.put(c, customItemType);
    }

    public boolean validCraftingMatrix(@Nullable ItemStack[] craftingMatrix) {
        Object[] matrix = getMatrix();
        for (int i = 0; i < 9; i++) {
            Object object = matrix[i];
            if (object instanceof Material) {
                var material = (Material) object;
                if (isCustomItem(craftingMatrix[i]))
                    return false;
                if (craftingMatrix[i] == null || !craftingMatrix[i].getType().equals(material))
                    return false;
            } else if (object instanceof CustomItemType) {
                var customItemType = (CustomItemType) object;
                var customItem = Core.getInstance().getCustomItemManager().getItemByType(customItemType);
                if (craftingMatrix[i] == null || !customItem.matches(craftingMatrix[i]))
                    return false;
            } else if (craftingMatrix[i] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getFinalItem() {
        return customItem.getItem().clone();
    }

    @NotNull
    @Override
    public org.bukkit.inventory.Recipe toBukkitRecipe() {
        if (bukkitRecipe == null) {
            var newBukkitRecipe = new org.bukkit.inventory.ShapedRecipe(Core.keyOf(customItem.getCustomItemType().name()), getFinalItem());
            var bukkitShape = new String[3];
            StringBuilder row = new StringBuilder();
            var rowIndex = 0;

            for (int i = 0; i < shape.length; i++) {
                char c = shape[i];
                row.append(c);

                if (i % 3 == 2) {
                    bukkitShape[rowIndex] = row.toString();
                    rowIndex++;
                    row = new StringBuilder();
                }
            }

            newBukkitRecipe.shape(bukkitShape);

            for (char c : shape) {
                Object lookup = charLookupMap.get(c);
                Material material;
                if (lookup instanceof Material) {
                    material = (Material) lookup;
                } else if (lookup instanceof CustomItemType) {
                    material = Core.getInstance().getCustomItemManager().getMaterialByType((CustomItemType) lookup);
                } else {
                    continue;
                }

                newBukkitRecipe.setIngredient(c, material);
            }

            bukkitRecipe = newBukkitRecipe;
        }

        return bukkitRecipe;
    }

    @Nullable
    public Object[] getMatrix() {
        if (matrix == null) {
            var newMatrix = new Object[9];
            for (int i = 0; i < shape.length; i++) {
                newMatrix[i] = charLookupMap.get(shape[i]);
            }
            matrix = newMatrix;
        }
        return matrix;
    }
}
