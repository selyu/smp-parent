package org.selyu.smp.core.item;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;
import static org.selyu.smp.core.util.MessageUtil.color;

class CoreItemStackFactory {
    private CoreItemStackFactory() {
    }

    @NotNull
    public static ItemStack create(@NotNull CoreItem coreItem) {
        var itemStack = new ItemStack(coreItem.getMaterial());
        var itemMeta = ensureMeta(itemStack);
        if (!coreItem.getDisplayName().isBlank())
            itemMeta.setDisplayName(color(coreItem.getDisplayName()));
        if (!coreItem.getLore().isEmpty())
            itemMeta.setLore(color(coreItem.getLore()));
        itemMeta.setCustomModelData(coreItem.getModelData());
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.getPersistentDataContainer().set(CoreItem.INTERNAL_NAME_KEY, PersistentDataType.STRING, coreItem.getInternalName());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public static ItemStack createDurable(@NotNull DurableCoreItem durableCoreItem) {
        var itemStack = create(durableCoreItem);
        var itemMeta = ensureMeta(itemStack);
        itemMeta.setUnbreakable(true);

        itemMeta.setLore(addDurabilityLore(durableCoreItem, durableCoreItem.getMaxDurability()));
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.getPersistentDataContainer().set(DurableCoreItem.DURABILITY_KEY, PersistentDataType.INTEGER, durableCoreItem.getMaxDurability());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public static List<String> addDurabilityLore(@NotNull DurableCoreItem durableCoreItem, int durability) {
        List<String> lore = durableCoreItem.getLore();
        lore.add("");
        lore.add(String.format("&fDurability: %s / %s", durability, durableCoreItem.getMaxDurability()));
        return color(lore);
    }
}
