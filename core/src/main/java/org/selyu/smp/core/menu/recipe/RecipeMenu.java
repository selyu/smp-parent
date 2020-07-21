package org.selyu.smp.core.menu.recipe;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CoreItemType;
import org.selyu.smp.core.manager.CoreItemManager;
import org.selyu.smp.core.menu.Menus;

public final class RecipeMenu implements InventoryProvider {
    public static final SmartInventory INSTANCE = Core.getInstance().buildInventory()
            .provider(new RecipeMenu())
            .id("recipeMenu")
            .size(3, 9)
            .title("Recipe Book")
            .build();

    private final CoreItemManager coreItemManager = Core.getInstance().getCoreItemManager();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.fill(Menus.PLACEHOLDER_ITEM);
        for (int i = 0; i < CoreItemType.values().length; i++) {
            inventoryContents.set(1, i + 1, clickableItemForType(CoreItemType.values()[i]));
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

    @NotNull
    private ClickableItem clickableItemForType(@NotNull CoreItemType coreItemType) {
        return ClickableItem.of(coreItemManager.getMenuItemForType(coreItemType), (event) -> {
            ChooseRecipeMenu.open(coreItemType, (Player) event.getWhoClicked(), 0);
        });
    }
}
