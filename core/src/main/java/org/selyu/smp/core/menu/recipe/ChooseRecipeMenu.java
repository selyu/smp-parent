package org.selyu.smp.core.menu.recipe;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CoreItemType;
import org.selyu.smp.core.item.recipe.key.RecipeKey;
import org.selyu.smp.core.manager.CoreItemManager;
import org.selyu.smp.core.menu.Menus;

import java.util.ArrayList;

import static co.aikar.commands.ACFBukkitUtil.color;

public final class ChooseRecipeMenu implements InventoryProvider {
    private final CoreItemType coreItemType;
    private final CoreItemManager coreItemManager = Core.getInstance().getCoreItemManager();

    public ChooseRecipeMenu(@NotNull CoreItemType coreItemType) {
        this.coreItemType = coreItemType;
    }

    public static void open(@NotNull CoreItemType coreItemType, @NotNull Player player, int page) {
        Core.getInstance().buildInventory()
                .provider(new ChooseRecipeMenu(coreItemType))
                .id("chooseRecipeMenu")
                .size(3, 9)
                .title("Choose a recipe to craft!")
                .build()
                .open(player, page);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.fill(Menus.PLACEHOLDER_ITEM);

        var pagination = inventoryContents.pagination();
        var itemsArray = coreItemManager.getCraftableItemsForType(coreItemType)
                .stream()
                .map(coreItem -> {
                    var recipe = coreItem.getRecipe();
                    var itemStack = coreItem.getMenuItem();
                    var itemMeta = itemStack.getItemMeta();
                    var lore = new ArrayList<String>();
                    for (RecipeKey key : recipe.getKeys()) {
                        lore.add(color(String.format("&f%sx %s", key.getAmount(), key.getNeeded())));
                    }

                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);

                    return ClickableItem.of(itemStack, (inventoryClickEvent -> {
                        if (inventoryClickEvent.isLeftClick() && recipe.canCraft(inventoryClickEvent.getWhoClicked().getInventory()))
                            inventoryClickEvent.getWhoClicked().getInventory().addItem(recipe.getFinalItem());
                    }));
                }).toArray(ClickableItem[]::new);

        pagination.setItems(itemsArray);
        pagination.setItemsPerPage(7);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        inventoryContents.set(2, 3, ClickableItem.of(Menus.PREV_PAGE_ITEM_STACK, (event -> open(coreItemType, player, pagination.previous().getPage()))));
        inventoryContents.set(2, 5, ClickableItem.of(Menus.NEXT_PAGE_ITEM_STACK, (event -> open(coreItemType, player, pagination.next().getPage()))));
        inventoryContents.set(2, 3, ClickableItem.of(Menus.BACK_ITEM_STACK, (event -> RecipeMenu.INSTANCE.open(player))));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }
}
