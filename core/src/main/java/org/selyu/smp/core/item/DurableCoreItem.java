package org.selyu.smp.core.item;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.annotation.ItemEventHandler;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ConstantConditions")
public abstract class DurableCoreItem extends CoreItem {
    public static final NamespacedKey DURABILITY_KEY = Core.keyOf("durability");

    private final int maxDurability;

    public DurableCoreItem(@NotNull String internalName, @NotNull CoreItemType coreItemType, @NotNull Material material, int modelData, int maxDurability) {
        super(internalName, coreItemType, material, modelData);
        this.maxDurability = maxDurability;
    }

    private void handleDurability(Player player, ItemStack itemStack, EquipmentSlot equipmentSlot, boolean setItemInSlot) {
        var itemMeta = itemStack.getItemMeta();
        var chanceToNegate = 0;
        if (itemMeta.hasEnchant(Enchantment.DURABILITY))
            chanceToNegate = 100 / (itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1);

        var random = ThreadLocalRandom.current().nextInt(1, 100);
        var newDurability = itemMeta.getPersistentDataContainer().getOrDefault(DURABILITY_KEY, PersistentDataType.INTEGER, maxDurability);
        if(random > chanceToNegate)
            newDurability--;

        if(newDurability == -1) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            player.getEquipment().setItem(equipmentSlot, null);
        } else {
            itemMeta.setLore(CoreItemStackFactory.addDurabilityLore(this, newDurability));
            itemMeta.getPersistentDataContainer().set(DURABILITY_KEY, PersistentDataType.INTEGER, newDurability);

            itemStack.setItemMeta(itemMeta);
            if(setItemInSlot)
                player.getEquipment().setItem(equipmentSlot, itemStack);
        }
    }

    @ItemEventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            handleDurability(event.getPlayer(), event.getPlayer().getEquipment().getItemInMainHand(), EquipmentSlot.HAND, false);
    }

    @ItemEventHandler
    public void onShear(PlayerShearEntityEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            handleDurability(event.getPlayer(), event.getItem(), event.getHand(), true);
    }

    public final int getMaxDurability() {
        return maxDurability;
    }
}
