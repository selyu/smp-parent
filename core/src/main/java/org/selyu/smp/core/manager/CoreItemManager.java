package org.selyu.smp.core.manager;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CustomItem;
import org.selyu.smp.core.item.CustomItemType;
import org.selyu.smp.core.item.DurableCustomItem;
import org.selyu.smp.core.item.annotation.ItemEventHandler;
import org.selyu.smp.core.item.impl.DiamondShearsItem;
import org.selyu.smp.core.item.impl.HellstoneIngotItem;
import org.selyu.smp.core.item.impl.HellstonePickaxeItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.selyu.smp.core.util.BukkitUtil.ensureMeta;

public final class CoreItemManager {
    private final List<CustomItem> items = Arrays.asList(
            new DiamondShearsItem(),
            new HellstoneIngotItem(),
            new HellstonePickaxeItem()
    );

    private final Map<Class<?>, Set<SubscribedMethod>> subscribersMap = wrap(items);

    @SuppressWarnings("ConstantConditions")
    public void runEvent(@NotNull Event event, @NotNull ItemStack itemStack) {
        var subscribers = subscribersMap.get(event.getClass());
        if (subscribers == null)
            return;

        for (SubscribedMethod subscriber : subscribers) {
            if (subscriber.parent.getMaterial() == itemStack.getType() &&
                    subscriber.parent.getModelData() == itemStack.getItemMeta().getCustomModelData() &&
                    subscriber.parent.validate(itemStack)) {
                try {
                    subscriber.method.invoke(subscriber.parent, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NotNull
    public ItemStack getMenuItemForType(@NotNull CustomItemType customItemType) {
        var coreItem = items
                .stream()
                .filter(item -> item.getCustomItemType() == customItemType)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No items with type " + customItemType));
        var itemStack = new ItemStack(coreItem.getMaterial());
        var itemMeta = ensureMeta(itemStack);
        itemMeta.setDisplayName(ChatColor.RESET + customItemType.getCorrectName());
        itemMeta.setCustomModelData(coreItem.getModelData());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public List<CustomItem> getCraftableItemsForType(CustomItemType customItemType) {
        return items
                .stream()
                .filter(coreItem -> coreItem.getRecipe() != null)
                .filter(coreItem -> coreItem.getCustomItemType() == customItemType)
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<Class<?>, Set<SubscribedMethod>> wrap(@NotNull List<CustomItem> items) {
        var map = new HashMap<Class<?>, Set<SubscribedMethod>>();
        for (CustomItem item : items) {
            map.putAll(getSubscribedMethods(item, item.getClass().getDeclaredMethods()));
            if (item instanceof DurableCustomItem)
                map.putAll(getSubscribedMethods(item, item.getClass().getSuperclass().getDeclaredMethods()));
        }
        return map;
    }

    @NotNull
    private Map<Class<?>, Set<SubscribedMethod>> getSubscribedMethods(@NotNull CustomItem customItem, @NotNull Method[] methods) {
        var map = new HashMap<Class<?>, Set<SubscribedMethod>>();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(ItemEventHandler.class) ||
                    method.getParameterCount() != 1 ||
                    !Event.class.isAssignableFrom(method.getParameterTypes()[0]) ||
                    !method.trySetAccessible()) {
                continue;
            }

            var event = method.getParameterTypes()[0];
            var mapValue = map.getOrDefault(event, new HashSet<>());
            mapValue.add(new SubscribedMethod(customItem, method));

            map.put(event, mapValue);
        }

        return map;
    }

    private static final class SubscribedMethod {
        private final CustomItem parent;
        private final Method method;

        public SubscribedMethod(CustomItem parent, Method method) {
            this.parent = parent;
            this.method = method;
        }
    }
}
