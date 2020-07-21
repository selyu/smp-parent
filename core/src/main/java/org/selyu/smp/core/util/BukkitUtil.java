package org.selyu.smp.core.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItem;

public final class BukkitUtil {
    private BukkitUtil() {
    }

    public static void ensureMeta(@NotNull ItemStack itemStack) {
        if (!itemStack.hasItemMeta())
            itemStack.setItemMeta(Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    }

    public static boolean isCustomItem(@NotNull ItemStack itemStack) {
        return itemStack.getItemMeta() != null && itemStack.getItemMeta().getPersistentDataContainer().has(CoreItem.INTERNAL_NAME_KEY, PersistentDataType.STRING);
    }
}
