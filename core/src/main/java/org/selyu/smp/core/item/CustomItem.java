package org.selyu.smp.core.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.util.BukkitUtil;

import java.util.ArrayList;
import java.util.List;

import static co.aikar.commands.ACFBukkitUtil.color;
import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;
import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;

public abstract class CustomItem {
    public static final NamespacedKey INTERNAL_NAME_KEY = Core.keyOf("internal_name");

    private final String internalName;
    private final CustomItemType customItemType;
    private final Material material;
    private final int modelData;

    public CustomItem(@NotNull CustomItemType customItemType, @NotNull Material material, int modelData) {
        this.internalName = customItemType.name();
        this.customItemType = customItemType;
        this.material = material;
        this.modelData = modelData;
    }

    @NotNull
    public abstract String getDisplayName();

    @NotNull
    public List<String> getLore() {
        return new ArrayList<>();
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

    public boolean matches(@NotNull ItemStack itemStack) {
        if (!isCustomItem(itemStack))
            return false;

        var itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return false;

        return material.equals(itemStack.getType()) && itemMeta.hasCustomModelData() && modelData == itemMeta.getCustomModelData() && customItemType.equals(BukkitUtil.getCustomItemType(itemStack));
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
