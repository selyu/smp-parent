package org.selyu.smp.core.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.persistence.PersistentDataType
import java.util.function.Predicate

class CoreRecipe(private val coreItem: CoreItem) {
    private val keys = mutableSetOf<RecipeKey>()

    fun addKey(recipeKey: RecipeKey) = keys.add(recipeKey)

    fun test(playerInventory: PlayerInventory): Boolean {
        val unmatchedKeys = keys
        playerInventory.contents.forEach { itemStack ->
            if (unmatchedKeys.isEmpty())
                return true

            unmatchedKeys.forEach { key ->
                if (key.test(itemStack)) {
                    unmatchedKeys.remove(key)

                    itemStack.amount -= key.amount
                    if (itemStack.amount <= 0)
                        itemStack.type = Material.AIR
                }
            }
        }

        return unmatchedKeys.isEmpty()
    }

    fun getFinalItem(): ItemStack = coreItem.getItem().clone()
}

open class RecipeKey(val amount: Int = 1) : Predicate<ItemStack> {
    override fun test(t: ItemStack): Boolean = t.amount >= amount
}

class InternalNameRecipeKey(private val internalName: String, amount: Int = 1) : RecipeKey(amount) {
    override fun test(t: ItemStack): Boolean = super.test(t) && t.hasItemMeta() && internalName == t.itemMeta!!.persistentDataContainer.get(CoreItem.INTERNAL_NAME_KEY, PersistentDataType.STRING)
}

class MaterialRecipeKey(private val material: Material, amount: Int = 1) : RecipeKey(amount) {
    override fun test(t: ItemStack): Boolean = super.test(t) && t.type == material
}