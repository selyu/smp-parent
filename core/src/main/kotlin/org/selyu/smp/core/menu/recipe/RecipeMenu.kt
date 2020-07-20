package org.selyu.smp.core.menu.recipe

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import fr.minuskube.inv.content.SlotIterator
import org.bukkit.entity.Player
import org.selyu.smp.core.Core
import org.selyu.smp.core.item.CoreItemType
import org.selyu.smp.core.menu.backItemStack
import org.selyu.smp.core.menu.nextPageItemStack
import org.selyu.smp.core.menu.placeholderItem
import org.selyu.smp.core.menu.prevPageItemStack
import org.selyu.smp.core.util.color

class RecipeMenu : InventoryProvider {
    private val coreItemManager = Core.instance.coreItemManager

    companion object {
        private val inv: SmartInventory = Core.instance.buildInventory()
                .provider(RecipeMenu())
                .id("recipeMenu")
                .size(3, 9)
                .title("Recipe Book")
                .build()

        @JvmStatic
        fun open(player: Player) {
            inv.open(player)
        }
    }

    override fun update(p0: Player?, p1: InventoryContents?) {}

    override fun init(player: Player, contents: InventoryContents) {
        contents.fill(placeholderItem)
        for (i in CoreItemType.values().indices) {
            contents.set(1, i + 1, clickableItemForType(CoreItemType.values()[i]))
        }
    }

    private fun clickableItemForType(coreItemType: CoreItemType): ClickableItem = ClickableItem.of(coreItemManager.getMenuItemForType(coreItemType)) {
        RecipeChooseMenu.open(coreItemType, it.whoClicked as Player)
    }

    private class RecipeChooseMenu(private val coreItemType: CoreItemType) : InventoryProvider {
        private val coreItemManager = Core.instance.coreItemManager

        companion object {
            @JvmStatic
            fun open(coreItemType: CoreItemType, player: Player, page: Int = 0) {
                Core.instance.buildInventory()
                        .provider(RecipeChooseMenu(coreItemType))
                        .id("recipeChooseMenu")
                        .size(3, 9)
                        .title("Choose a recipe to craft!")
                        .build()
                        .open(player, page)
            }
        }

        override fun update(p0: Player?, p1: InventoryContents?) {}

        override fun init(player: Player, contents: InventoryContents) {
            contents.fill(placeholderItem)

            val pagination = contents.pagination()
            val itemsArray = coreItemManager.getCraftableItemsForType(coreItemType).map { coreItem ->
                val itemStack = coreItem.getMenuItem()
                val itemMeta = itemStack.itemMeta!!
                val recipe = coreItem.getRecipe()!!
                val lore = mutableListOf<String>()
                recipe.keys.forEach {
                    lore.add("&f${it.amount}x ${it.needed}".color)
                }

                itemMeta.lore = lore
                itemStack.itemMeta = itemMeta
                ClickableItem.of(itemStack) {
                    if (recipe.test(it.whoClicked.inventory))
                        it.whoClicked.inventory.addItem(recipe.getFinalItem())
                }
            }.toTypedArray()
            pagination.setItems(*itemsArray)
            pagination.setItemsPerPage(7)

            pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1))

            contents[2, 3] = ClickableItem.of(prevPageItemStack) {
                open(coreItemType, player, pagination.previous().page)
            }

            contents[2, 5] = ClickableItem.of(nextPageItemStack) {
                open(coreItemType, player, pagination.next().page)
            }

            contents[2, 4] = ClickableItem.of(backItemStack) {
                open(player)
            }
        }
    }
}