package org.selyu.smp.core.item;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.annotation.ItemEventHandler;

import java.util.Random;

import static java.util.Objects.requireNonNull;
import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;

public abstract class DurableCustomItem extends CustomItem {
    public static final NamespacedKey DAMAGE_KEY = Core.keyOf("damage");
    private static final Random random = new Random();

    private final int maxDurability;

    public DurableCustomItem(@NotNull CustomItemType customItemType, @NotNull Material material, int modelData, int maxDurability) {
        super(customItemType, material, modelData);
        this.maxDurability = maxDurability;
    }

    private void handleDurability(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull EquipmentSlot equipmentSlot, boolean setItemInSlot) {
        requireNonNull(player.getEquipment());

        ItemMeta itemMeta = ensureMeta(itemStack);
        int chanceToNegate = 0;
        if (itemMeta.hasEnchant(Enchantment.DURABILITY))
            chanceToNegate = 100 / (itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1);

        int damage = itemMeta.getPersistentDataContainer().getOrDefault(DAMAGE_KEY, PersistentDataType.INTEGER, 0);
        int randomChance = random.nextInt(100);
        if (randomChance > chanceToNegate)
            damage++;

        if (damage != maxDurability) {
            itemMeta.setLore(ItemStackFactory.addDurabilityLore(this, maxDurability - damage));
            itemMeta.getPersistentDataContainer().set(DAMAGE_KEY, PersistentDataType.INTEGER, damage);

            double newDamage = (double) damage / (double) maxDurability * itemStack.getType().getMaxDurability() - 2;
            ((Damageable) itemMeta).setDamage((int) Math.ceil(newDamage));
            itemStack.setItemMeta(itemMeta);

            if (setItemInSlot)
                player.getEquipment().setItem(equipmentSlot, itemStack);
        }
    }

    @ItemEventHandler(priority = 999)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            handleDurability(event.getPlayer(), requireNonNull(event.getPlayer().getEquipment()).getItemInMainHand(), EquipmentSlot.HAND, false);
    }

    @ItemEventHandler(priority = 999)
    public void onShear(PlayerShearEntityEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            handleDurability(event.getPlayer(), event.getItem(), event.getHand(), true);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return ItemStackFactory.createDurable(this);
    }

    public final int getMaxDurability() {
        return maxDurability;
    }
}
