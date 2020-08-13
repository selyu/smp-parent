package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneAxeItem extends SimpleDurableCustomItem {
    public HellstoneAxeItem() {
        super(CustomItemType.HELLSTONE_AXE, Material.IRON_AXE, 1, DurabilityUtil.HELLSTONE_INGOT * 3);
    }

    @Override
    public @NotNull String getDisplayName() {
        return null;
    }
}
