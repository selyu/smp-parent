package org.selyu.smp.core.manager;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.item.CoreItem;
import org.selyu.smp.core.item.CoreItemType;
import org.selyu.smp.core.item.DurableCoreItem;
import org.selyu.smp.core.item.ItemEventHandler;
import org.selyu.smp.core.item.impl.DiamondShearsItem;
import org.selyu.smp.core.item.impl.HellstoneIngotItem;
import org.selyu.smp.core.item.impl.HellstonePickaxeItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.selyu.smp.core.util.BukkitKt.ensureMeta;

public final class CoreItemManager {
    private final List<CoreItem> items = Arrays.asList(
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

    @SuppressWarnings("ConstantConditions")
    @NotNull
    public ItemStack getMenuItemForType(@NotNull CoreItemType coreItemType) {
        var coreItem = items
                .stream()
                .filter(item -> item.getCoreItemType() == coreItemType)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No items with type " + coreItemType));
        var itemStack = new ItemStack(coreItem.getMaterial());
        ensureMeta(itemStack);

        var itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + coreItemType.getCorrectName());
        itemMeta.setCustomModelData(coreItem.getModelData());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @NotNull
    public List<CoreItem> getCraftableItemsForType(CoreItemType coreItemType) {
        return items
                .stream()
                .filter(coreItem -> coreItem.getRecipe() != null)
                .filter(coreItem -> coreItem.getCoreItemType() == coreItemType)
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<Class<?>, Set<SubscribedMethod>> wrap(@NotNull List<CoreItem> items) {
        var map = new HashMap<Class<?>, Set<SubscribedMethod>>();
        for (CoreItem item : items) {
            map.putAll(getSubscribedMethods(item, item.getClass().getDeclaredMethods()));
            if (item instanceof DurableCoreItem)
                map.putAll(getSubscribedMethods(item, item.getClass().getSuperclass().getDeclaredMethods()));
        }
        return map;
    }

    @NotNull
    private Map<Class<?>, Set<SubscribedMethod>> getSubscribedMethods(@NotNull CoreItem coreItem, @NotNull Method[] methods) {
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
            mapValue.add(new SubscribedMethod(coreItem, method));

            map.put(event, mapValue);
        }

        return map;
    }

    private static final class SubscribedMethod {
        private final CoreItem parent;
        private final Method method;

        public SubscribedMethod(CoreItem parent, Method method) {
            this.parent = parent;
            this.method = method;
        }
    }
}
