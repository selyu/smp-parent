package org.selyu.smp.core.menu.recipe;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
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

import static java.util.Objects.requireNonNull;
import static org.selyu.smp.core.util.BukkitUtil.*;
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

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.fill(Menus.PLACEHOLDER_ITEM);

        Pagination pagination = inventoryContents.pagination();
        ClickableItem[] itemsArray = customItemManager.getCraftableItemsByCategory(customItemCategory)
                .stream()
                .map(coreItem -> {
                    Recipe recipe = requireNonNull(coreItem.getRecipe());
                    ItemStack itemStack = coreItem.getMenuItem();
                    ItemMeta itemMeta = ensureMeta(itemStack);
                    ArrayList<String> lore = new ArrayList<>();
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
        HashMap<Object, Integer> map = new HashMap<>();
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
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
        HashMap<Object, Integer> newMap = new HashMap<>(map);
        HashSet<Object> usedIngredients = new HashSet<>();
        HashMap<Integer, ItemStack> updatedItems = new HashMap<>();

        for (int i = 0; i < playerInventory.getContents().length; i++) {
            ItemStack itemStack = playerInventory.getContents()[i];
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            int finalI = i;
            newMap.forEach((ingredient, amount) -> {
                if (usedIngredients.contains(ingredient))
                    return;

                if (ingredient instanceof Material) {
                    if (itemStack.getType().equals(ingredient) && itemStack.getAmount() >= amount) {
                        ItemStack newItemStack = itemStack.clone();
                        newItemStack.setAmount(newItemStack.getAmount() - amount);
                        updatedItems.put(finalI, newItemStack);
                        usedIngredients.add(ingredient);
                    }
                } else if (ingredient instanceof CustomItemType) {
                    if (isCustomItem(itemStack) && getCustomItemType(itemStack).equals(ingredient) && itemStack.getAmount() >= amount) {
                        ItemStack newItemStack = itemStack.clone();
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
