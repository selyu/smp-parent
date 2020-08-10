package org.selyu.smp.core.menu;

import fr.minuskube.inv.ClickableItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;
import static org.selyu.smp.core.util.MessageUtil.color;

public final class Menus {
    public static final ClickableItem PLACEHOLDER_ITEM = ClickableItem.empty(buildMenuItem(Material.BLACK_STAINED_GLASS_PANE, "&0"));
    public static final ItemStack NEXT_PAGE_ITEM_STACK = buildMenuItem(Material.ARROW, "&a>>");
    public static final ItemStack PREV_PAGE_ITEM_STACK = buildMenuItem(Material.ARROW, "&b<<");
    public static final ItemStack BACK_ITEM_STACK = buildMenuItem(Material.BARRIER, "&cGo back!");

    private Menus() {
    }

    @NotNull
    private static ItemStack buildMenuItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = ensureMeta(itemStack);
        itemMeta.setDisplayName(color(displayName));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
