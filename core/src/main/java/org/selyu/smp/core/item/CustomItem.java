package org.selyu.smp.core.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.util.BukkitUtil;

import java.util.ArrayList;
import java.util.List;

import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;
import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;
import static org.selyu.smp.core.util.MessageUtil.color;

public abstract class CustomItem {
    public static final NamespacedKey INTERNAL_NAME_KEY = new NamespacedKey(Core.getInstance(), "internal_name");

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
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = ensureMeta(itemStack);

        itemMeta.setDisplayName(color(getDisplayName()));
        itemMeta.setLore(color(getLore()));
        itemMeta.setCustomModelData(modelData);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.getPersistentDataContainer().set(INTERNAL_NAME_KEY, PersistentDataType.STRING, internalName);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean matches(@NotNull ItemStack itemStack) {
        if (!isCustomItem(itemStack))
            return false;

        ItemMeta itemMeta = itemStack.getItemMeta();
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
