package org.selyu.smp.core.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.manager.CustomItemManager;

import static org.selyu.smp.core.util.BukkitUtil.getCustomItemType;
import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;

public final class CustomItemListener implements Listener {
    private final CustomItemManager customItemManager = Core.getInstance().getCustomItemManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null)
            return;

        var result = event.getRecipe().getResult();
        if (event.getRecipe() instanceof ShapedRecipe && isCustomItem(result)) {
            var customItem = customItemManager.getItemByType(getCustomItemType(result));
            var shapedRecipe = (org.selyu.smp.core.item.recipe.ShapedRecipe) customItem.getRecipe();
            if (shapedRecipe != null && !shapedRecipe.validCraftingMatrix(event.getInventory().getMatrix()))
                event.getInventory().setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY || impossibleItem(event.getItem()))
            return;

        customItemManager.runEvent(event, event.getItem());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled() || event.getPlayer().getEquipment() == null || impossibleItem(event.getPlayer().getEquipment().getItemInMainHand()))
            return;

        customItemManager.runEvent(event, event.getPlayer().getEquipment().getItemInMainHand());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShear(PlayerShearEntityEvent event) {
        if (event.isCancelled() || impossibleItem(event.getItem()))
            return;

        customItemManager.runEvent(event, event.getItem());
    }

    private boolean impossibleItem(@Nullable ItemStack itemStack) {
        return !isCustomItem(itemStack);
    }
}
