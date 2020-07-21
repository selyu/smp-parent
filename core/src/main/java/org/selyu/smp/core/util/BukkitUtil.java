package org.selyu.smp.core.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItem;

public final class BukkitUtil {
    private BukkitUtil() {
    }

    @NotNull
    public static ItemMeta ensureMeta(@NotNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null)
            itemStack.setItemMeta(Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
        return itemStack.getItemMeta();
    }

    public static boolean isCustomItem(@NotNull ItemStack itemStack) {
        return itemStack.getItemMeta() != null && itemStack.getItemMeta().getPersistentDataContainer().has(CoreItem.INTERNAL_NAME_KEY, PersistentDataType.STRING);
    }
}
