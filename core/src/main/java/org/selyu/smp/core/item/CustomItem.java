package org.selyu.smp.core.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.recipe.Recipe;

import java.util.Collections;
import java.util.List;

import static co.aikar.commands.ACFBukkitUtil.color;
import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;

public abstract class CustomItem {
    public static final NamespacedKey INTERNAL_NAME_KEY = Core.keyOf("internal_name");

    private final String internalName;
    private final CustomItemType customItemType;
    private final Material material;
    private final int modelData;

    public CustomItem(@NotNull String internalName, @NotNull CustomItemType customItemType, @NotNull Material material, int modelData) {
        this.internalName = internalName;
        this.customItemType = customItemType;
        this.material = material;
        this.modelData = modelData;
    }

    @NotNull
    public abstract String getDisplayName();

    @NotNull
    public List<String> getLore() {
        return Collections.emptyList();
    }

    @NotNull
    public ItemStack getItem() {
        return ItemStackFactory.create(this);
    }

    @NotNull
    public final ItemStack getMenuItem() {
        var itemStack = new ItemStack(material);
        var itemMeta = ensureMeta(itemStack);
        if (!getDisplayName().isBlank())
            itemMeta.setDisplayName(color(getDisplayName()));
        itemMeta.setCustomModelData(modelData);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean validate(@NotNull ItemStack itemStack) {
        return true;
    }

    @Nullable
    public Recipe getRecipe() {
        return null;
    }

    @NotNull
    public final String getInternalName() {
        return internalName;
    }

    @NotNull
    public final CustomItemType getCustomItemType() {
        return customItemType;
    }

    @NotNull
    public final Material getMaterial() {
        return material;
    }

    public final int getModelData() {
        return modelData;
    }
}
