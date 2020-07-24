package org.selyu.smp.core.menu.recipe;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CustomItemCategory;
import org.selyu.smp.core.manager.CustomItemManager;
import org.selyu.smp.core.menu.Menus;

public final class RecipeMenu implements InventoryProvider {
    public static final SmartInventory INSTANCE = Core.getInstance().buildInventory()
            .provider(new RecipeMenu())
            .id("recipeMenu")
            .size(3, 9)
            .title("Recipe Book")
            .build();

    private final CustomItemManager customItemManager = Core.getInstance().getCustomItemManager();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.fill(Menus.PLACEHOLDER_ITEM);
        for (int i = 0; i < CustomItemCategory.values().length; i++) {
            inventoryContents.set(1, i + 1, clickableItemForType(CustomItemCategory.values()[i]));
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

    @NotNull
    private ClickableItem clickableItemForType(@NotNull CustomItemCategory customItemCategory) {
        return ClickableItem.of(customItemManager.getMenuItemByType(customItemCategory), (event) -> ChooseRecipeMenu.open(customItemCategory, (Player) event.getWhoClicked(), 0));
    }
}
