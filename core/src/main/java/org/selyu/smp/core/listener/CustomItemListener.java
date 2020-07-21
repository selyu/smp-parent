package org.selyu.smp.core.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.manager.CoreItemManager;

import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;

public final class CustomItemListener implements Listener {
    private final CoreItemManager coreItemManager = Core.getInstance().getCoreItemManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY || impossibleItem(event.getItem()))
            return;

        coreItemManager.runEvent(event, event.getItem());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled() || event.getPlayer().getEquipment() == null || impossibleItem(event.getPlayer().getEquipment().getItemInMainHand()))
            return;

        coreItemManager.runEvent(event, event.getPlayer().getEquipment().getItemInMainHand());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShear(PlayerShearEntityEvent event) {
        if (event.isCancelled() || impossibleItem(event.getItem()))
            return;

        coreItemManager.runEvent(event, event.getItem());
    }

    private boolean impossibleItem(@Nullable ItemStack itemStack) {
        return itemStack == null || !isCustomItem(itemStack);
    }
}
