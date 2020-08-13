package org.selyu.smp.core.item;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.annotation.ItemEventHandler;

public abstract class SimpleDurableCustomItem extends DurableCustomItem {
    public SimpleDurableCustomItem(@NotNull CustomItemType customItemType, @NotNull Material material, int modelData, int maxDurability) {
        super(customItemType, material, modelData, maxDurability);
    }

    @ItemEventHandler(priority = 999)
    public void onItemDamage(PlayerItemDamageEvent event) {
        handleDamage(event.getPlayer(), event.getItem(), event.getDamage(), EquipmentSlot.HAND, false);
    }
}
