package org.selyu.smp.core.item;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.annotation.ItemEventHandler;

import static java.util.Objects.requireNonNull;

public abstract class SimpleDurableCustomItem extends DurableCustomItem {
    public SimpleDurableCustomItem(@NotNull CustomItemType customItemType, @NotNull Material material, int modelData, int maxDurability) {
        super(customItemType, material, modelData, maxDurability);
    }

    @ItemEventHandler(priority = 999)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            handleDamage(event.getPlayer(), requireNonNull(event.getPlayer().getEquipment()).getItemInMainHand(), 1, EquipmentSlot.HAND, false);
        }
    }

    @ItemEventHandler(priority = 999)
    public void onShear(PlayerShearEntityEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            handleDamage(event.getPlayer(), event.getItem(), 1, event.getHand(), true);
        }
    }
}
