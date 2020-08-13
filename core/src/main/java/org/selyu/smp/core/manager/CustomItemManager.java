package org.selyu.smp.core.manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemCategory;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.SimpleDurableCustomItem;
import org.selyu.smp.core.item.annotation.ItemEventHandler;
import org.selyu.smp.core.item.impl.DiamondShearsItem;
import org.selyu.smp.core.item.impl.hellstone.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;

public final class CustomItemManager {
    private final List<CustomItem> items = Arrays.asList(
            new DiamondShearsItem(),
            new HellstoneIngotItem(),
            new HellstonePickaxeItem(),
            new HellstoneShovelItem(),
            new HellstoneSwordItem(),
            new HellstoneAxeItem()
    );

    private final Map<Class<?>, List<SubscribedMethod>> subscribersMap = wrap(items);

    public void addRecipes() {
        for (CustomItem item : items) {
            if (item.getRecipe() == null) {
                continue;
            }

            Recipe bukkitRecipe = item.getRecipe().toBukkitRecipe();
            Core.getInstance().getServer().addRecipe(bukkitRecipe);
        }
    }

    public void runEvent(@NotNull Event event, @NotNull ItemStack itemStack) {
        List<SubscribedMethod> subscribers = subscribersMap.get(event.getClass());
        if (subscribers == null)
            return;

        subscribers.sort((o1, o2) -> o2.priority - o1.priority);
        for (SubscribedMethod subscriber : subscribers) {
            if (subscriber.parent.getMaterial() == itemStack.getType() &&
                    subscriber.parent.getModelData() == requireNonNull(itemStack.getItemMeta()).getCustomModelData()) {
                try {
                    subscriber.method.invoke(subscriber.parent, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NotNull
    public Material getMaterialByType(@NotNull CustomItemType customItemType) {
        return getItemByType(customItemType).getMaterial();
    }

    @NotNull
    public CustomItem getItemByType(@NotNull CustomItemType customItemType) {
        return items.stream()
                .filter(customItem -> customItem.getCustomItemType() == customItemType)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No core item with type " + customItemType));
    }

    @NotNull
    public ItemStack getMenuItemByType(@NotNull CustomItemCategory customItemCategory) {
        CustomItem customItem = items.stream()
                .filter(customItem1 -> customItem1.getCustomItemType().getCustomItemCategory() == customItemCategory)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No items with type " + customItemCategory));
        ItemStack itemStack = new ItemStack(customItem.getMaterial());
        ItemMeta itemMeta = ensureMeta(itemStack);
        itemMeta.setDisplayName(ChatColor.RESET + customItemCategory.getCorrectName());
        itemMeta.setCustomModelData(customItem.getModelData());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public List<CustomItem> getCraftableItemsByCategory(CustomItemCategory customItemCategory) {
        return items.stream()
                .filter(customItem -> customItem.getRecipe() != null)
                .filter(customItem -> customItem.getCustomItemType().getCustomItemCategory() == customItemCategory)
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<Class<?>, List<SubscribedMethod>> wrap(@NotNull List<CustomItem> items) {
        HashMap<Class<?>, List<SubscribedMethod>> map = new HashMap<>();
        for (CustomItem item : items) {
            addSubscribedMethods(item, map, item.getClass().getDeclaredMethods());
            if (item instanceof SimpleDurableCustomItem) {
                addSubscribedMethods(item, map, item.getClass().getSuperclass().getDeclaredMethods());
            }
        }
        return map;
    }

    private void addSubscribedMethods(@NotNull CustomItem customItem, @NotNull Map<Class<?>, List<SubscribedMethod>> map, @NotNull Method[] methods) {
        for (Method method : methods) {
            if (!method.isAnnotationPresent(ItemEventHandler.class) ||
                    method.getParameterCount() != 1 ||
                    !Event.class.isAssignableFrom(method.getParameterTypes()[0]) ||
                    !method.trySetAccessible()) {
                continue;
            }

            ItemEventHandler annotation = method.getAnnotation(ItemEventHandler.class);
            Class<?> event = method.getParameterTypes()[0];
            List<SubscribedMethod> mapValue = map.computeIfAbsent(event, (aClass -> new CopyOnWriteArrayList<>()));

            mapValue.add(new SubscribedMethod(customItem, method, annotation.priority()));
            map.put(event, mapValue);
        }
    }

    private static final class SubscribedMethod {
        private final CustomItem parent;
        private final Method method;
        private final int priority;

        public SubscribedMethod(@NotNull CustomItem parent, @NotNull Method method, int priority) {
            this.parent = parent;
            this.method = method;
            this.priority = priority;
        }

        @Override
        @Contract(pure = true)
        public @NotNull String toString() {
            return "SubscribedMethod{" +
                    "parent=" + parent +
                    ", method=" + method +
                    ", priority=" + priority +
                    '}';
        }
    }
}
