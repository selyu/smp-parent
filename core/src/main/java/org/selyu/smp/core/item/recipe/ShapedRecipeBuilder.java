package org.selyu.smp.core.item.recipe;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;

import java.util.HashMap;
import java.util.Map;

public final class ShapedRecipeBuilder {
    private final Map<Character, Object> ingredients = new HashMap<>();
    private final CustomItem customItem;
    private char[] shape;

    public ShapedRecipeBuilder(@NotNull CustomItem customItem) {
        this.customItem = customItem;
    }

    @NotNull
    public ShapedRecipeBuilder shape(char... shape) {
        this.shape = shape;
        return this;
    }

    @NotNull
    public ShapedRecipeBuilder ingredient(char c, @NotNull Material ingredient) {
        ingredients.put(c, ingredient);
        return this;
    }

    @NotNull
    public ShapedRecipeBuilder ingredient(char c, @NotNull CustomItemType ingredient) {
        ingredients.put(c, ingredient);
        return this;
    }

    public @NotNull Recipe build() {
        ShapedRecipe recipe = new ShapedRecipe(customItem);
        ingredients.forEach((character, o) -> {
            if (o instanceof Material) {
                recipe.setIngredient(character, (Material) o);
            } else if (o instanceof CustomItemType) {
                recipe.setIngredient(character, (CustomItemType) o);
            }
        });
        recipe.setShape(shape);
        return recipe;
    }
}
