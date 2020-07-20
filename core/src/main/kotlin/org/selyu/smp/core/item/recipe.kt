package org.selyu.smp.core.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.persistence.PersistentDataType
import java.util.function.Predicate

class CoreRecipe(private val coreItem: CoreItem) {
    val keys = hashSetOf<RecipeKey>()

    fun addKey(recipeKey: RecipeKey) = keys.add(recipeKey)

    fun test(playerInventory: PlayerInventory): Boolean {
        val matchedKeys = hashSetOf<RecipeKey>()
        val updatedItems = hashMapOf<Int, ItemStack>()

        for (i in playerInventory.contents.indices) {
            val itemStack: ItemStack? = playerInventory.getItem(i)
            if (itemStack == null || itemStack.type == Material.AIR)
                continue

            keys.forEach { key ->
                if (matchedKeys.contains(key))
                    return@forEach

                if (key.test(itemStack)) {
                    val newItemStack = itemStack.clone()
                    newItemStack.amount -= key.amount

                    updatedItems[i] = newItemStack
                    matchedKeys.add(key)
                }
            }
        }

        if (matchedKeys.size == keys.size)
            updatedItems.forEach { (idx, item) -> playerInventory.setItem(idx, item) }

        return matchedKeys.size == keys.size
    }

    fun getFinalItem(): ItemStack = coreItem.getItem().clone()
}

open class RecipeKey(val amount: Int = 1, val needed: String) : Predicate<ItemStack> {
    override fun test(t: ItemStack): Boolean = t.amount >= amount
}

class InternalNameRecipeKey(private val internalName: String, amount: Int = 1) : RecipeKey(amount, internalName) {
    override fun test(t: ItemStack): Boolean = super.test(t) && t.hasItemMeta() && internalName == t.itemMeta!!.persistentDataContainer.get(CoreItem.INTERNAL_NAME_KEY, PersistentDataType.STRING)
}

class MaterialRecipeKey(private val material: Material, amount: Int = 1) : RecipeKey(amount, material.name) {
    override fun test(t: ItemStack): Boolean = super.test(t) && t.type == material
}