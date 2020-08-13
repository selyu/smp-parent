package org.selyu.smp.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.manager.CustomItemManager;

import static org.selyu.smp.core.util.BukkitUtil.getCustomItemType;
import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;

public final class CustomItemListener implements Listener {
    private final CustomItemManager customItemManager = Core.getInstance().getCustomItemManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null)
            return;

        ItemStack result = event.getRecipe().getResult();
        if (event.getRecipe() instanceof ShapedRecipe && isCustomItem(result)) {
            CustomItem customItem = customItemManager.getItemByType(getCustomItemType(result));
            org.selyu.smp.core.item.recipe.ShapedRecipe shapedRecipe = (org.selyu.smp.core.item.recipe.ShapedRecipe) customItem.getRecipe();

            if (shapedRecipe != null && !shapedRecipe.validCraftingMatrix(event.getInventory().getMatrix()))
                event.getInventory().setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(CraftItemEvent event) {
        ItemStack itemStack = event.getRecipe().getResult();
        if (isCustomItem(itemStack)) {
            ItemStack realItem = customItemManager.getItemByType(getCustomItemType(itemStack)).getItem();
            event.setCurrentItem(realItem);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDamage(PlayerItemDamageEvent event) {
        if (event.isCancelled() || impossibleItem(event.getItem()))
            return;

        customItemManager.runEvent(event, event.getItem());
    }

    private boolean impossibleItem(@Nullable ItemStack itemStack) {
        return !isCustomItem(itemStack);
    }
}
