package org.selyu.smp.core.item.impl.hellstone;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.util.DurabilityUtil;

public final class HellstoneSwordItem extends SimpleDurableCustomItem {
    public HellstoneSwordItem() {
        super(CustomItemType.HELLSTONE_SWORD, Material.IRON_SWORD, 1, DurabilityUtil.HELLSTONE_INGOT * 2);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "&fHellstone Sword";
    }
}
