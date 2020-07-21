package org.selyu.smp.core.menu.recipe;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CustomItemCategory;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.recipe.Recipe;
import org.selyu.smp.core.item.recipe.ShapedRecipe;
import org.selyu.smp.core.manager.CustomItemManager;
import org.selyu.smp.core.menu.Menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.selyu.smp.core.util.BukkitUtil.getCustomItemType;
import static org.selyu.smp.core.util.BukkitUtil.isCustomItem;
import static org.selyu.smp.core.util.MessageUtil.color;

public final class ChooseRecipeMenu implements InventoryProvider {
    private final CustomItemCategory customItemCategory;
    private final CustomItemManager customItemManager = Core.getInstance().getCustomItemManager();

    public ChooseRecipeMenu(@NotNull CustomItemCategory customItemCategory) {
        this.customItemCategory = customItemCategory;
    }

    public static void open(@NotNull CustomItemCategory customItemCategory, @NotNull Player player, int page) {
        Core.getInstance().buildInventory()
                .provider(new ChooseRecipeMenu(customItemCategory))
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
        var itemsArray = customItemManager.getCraftableItemsByCategory(customItemCategory)
                .stream()
                .map(coreItem -> {
                    var recipe = coreItem.getRecipe();
                    var itemStack = coreItem.getMenuItem();
                    var itemMeta = itemStack.getItemMeta();
                    var lore = new ArrayList<String>();
                    Map<Object, Integer> ingredientsWithAmount = getIngredientsWithAmount(recipe);
                    ingredientsWithAmount.forEach((o, amount) -> lore.add(color(String.format("&f%sx %s", amount, o.toString()))));

                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);

                    return ClickableItem.of(itemStack, (inventoryClickEvent -> {
                        if (inventoryClickEvent.isLeftClick() && canCraft(inventoryClickEvent.getWhoClicked().getInventory(), ingredientsWithAmount))
                            inventoryClickEvent.getWhoClicked().getInventory().addItem(recipe.getFinalItem());
                    }));
                }).toArray(ClickableItem[]::new);

        pagination.setItems(itemsArray);
        pagination.setItemsPerPage(7);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        inventoryContents.set(2, 3, ClickableItem.of(Menus.PREV_PAGE_ITEM_STACK, (event -> open(customItemCategory, player, pagination.previous().getPage()))));
        inventoryContents.set(2, 5, ClickableItem.of(Menus.NEXT_PAGE_ITEM_STACK, (event -> open(customItemCategory, player, pagination.next().getPage()))));
        inventoryContents.set(2, 4, ClickableItem.of(Menus.BACK_ITEM_STACK, (event -> RecipeMenu.INSTANCE.open(player))));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

    @NotNull
    private Map<Object, Integer> getIngredientsWithAmount(Recipe recipe) {
        var map = new HashMap<Object, Integer>();

        if (recipe instanceof ShapedRecipe) {
            var shapedRecipe = (ShapedRecipe) recipe;
            for (Object object : shapedRecipe.getMatrix()) {
                if (object == null)
                    continue;
                map.put(object, map.getOrDefault(object, 0) + 1);
            }
        }

        return map;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean canCraft(@NotNull PlayerInventory playerInventory, @NotNull Map<Object, Integer> map) {
        // There are probably better ways to do this, but it works.
        var newMap = new HashMap<>(map);
        var usedIngredients = new HashSet<>();
        var updatedItems = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < playerInventory.getContents().length; i++) {
            var itemStack = playerInventory.getContents()[i];
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            int finalI = i;
            newMap.forEach((ingredient, amount) -> {
                if(usedIngredients.contains(ingredient))
                    return;

                if (ingredient instanceof Material) {
                    if (itemStack.getType().equals(ingredient) && itemStack.getAmount() >= amount) {
                        var newItemStack = itemStack.clone();
                        newItemStack.setAmount(newItemStack.getAmount() - amount);
                        updatedItems.put(finalI, newItemStack);
                        usedIngredients.add(ingredient);
                    }
                } else if (ingredient instanceof CustomItemType) {
                    if (isCustomItem(itemStack) && getCustomItemType(itemStack).equals(ingredient) && itemStack.getAmount() >= amount) {
                        var newItemStack = itemStack.clone();
                        newItemStack.setAmount(newItemStack.getAmount() - amount);
                        updatedItems.put(finalI, newItemStack);
                        usedIngredients.add(ingredient);
                    }
                }
            });
        }

        if (newMap.size() == usedIngredients.size())
            updatedItems.forEach(playerInventory::setItem);
        return newMap.size() == usedIngredients.size();
    }
}
