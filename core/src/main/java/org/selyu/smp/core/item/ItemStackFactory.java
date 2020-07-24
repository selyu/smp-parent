package org.selyu.smp.core.item;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;
import static org.selyu.smp.core.util.MessageUtil.color;

class ItemStackFactory {
    private ItemStackFactory() {
    }

    @NotNull
    public static ItemStack create(@NotNull CustomItem customItem) {
        var itemStack = new ItemStack(customItem.getMaterial());
        var itemMeta = ensureMeta(itemStack);
        if (!customItem.getDisplayName().isBlank())
            itemMeta.setDisplayName(color(customItem.getDisplayName()));
        if (!customItem.getLore().isEmpty())
            itemMeta.setLore(color(customItem.getLore()));
        itemMeta.setCustomModelData(customItem.getModelData());
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.getPersistentDataContainer().set(CustomItem.INTERNAL_NAME_KEY, PersistentDataType.STRING, customItem.getInternalName());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public static ItemStack createDurable(@NotNull DurableCustomItem durableCustomItem) {
        var itemStack = create(durableCustomItem);
        var itemMeta = ensureMeta(itemStack);

        itemMeta.setLore(addDurabilityLore(durableCustomItem, durableCustomItem.getMaxDurability()));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.getPersistentDataContainer().set(DurableCustomItem.DAMAGE_KEY, PersistentDataType.INTEGER, 0);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public static List<String> addDurabilityLore(@NotNull DurableCustomItem durableCoreItem, int durability) {
        List<String> lore = durableCoreItem.getLore();
        lore.add("");
        lore.add(String.format("&fDurability: %s / %s", durability, durableCoreItem.getMaxDurability()));
        return color(lore);
    }
}
