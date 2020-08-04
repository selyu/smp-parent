package org.selyu.smp.core.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;

import static java.util.Objects.requireNonNull;

public final class BukkitUtil {
    private BukkitUtil() {
    }

    @NotNull
    public static ItemMeta ensureMeta(@NotNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null)
            itemStack.setItemMeta(Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
        return itemStack.getItemMeta();
    }

    public static boolean isCustomItem(@Nullable ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getPersistentDataContainer().has(CustomItem.INTERNAL_NAME_KEY, PersistentDataType.STRING);
    }

    @NotNull
    public static CustomItemType getCustomItemType(@NotNull ItemStack itemStack) {
        if (!isCustomItem(itemStack))
            throw new IllegalArgumentException("itemStack isn't a custom item!");
        return CustomItemType.valueOf(requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(CustomItem.INTERNAL_NAME_KEY, PersistentDataType.STRING));
    }
}
