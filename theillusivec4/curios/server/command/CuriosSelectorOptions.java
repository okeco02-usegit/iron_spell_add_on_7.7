/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.minecraft.commands.arguments.selector.EntitySelectorParser
 *  net.minecraft.commands.arguments.selector.options.EntitySelectorOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.TagParser
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package top.theillusivec4.curios.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CuriosSelectorOptions {
    public static void register() {
        EntitySelectorOptions.m_121453_((String)"curios", CuriosSelectorOptions::curioArgument, entitySelectorParser -> true, (Component)Component.m_237115_((String)"argument.entity.options.curios.description"));
    }

    private static void curioArgument(EntitySelectorParser parser) throws CommandSyntaxException {
        ItemStack stack;
        StringReader reader = parser.m_121346_();
        boolean invert = parser.m_121330_();
        CompoundTag compoundtag = new TagParser(reader).m_129373_();
        ListTag listTag = compoundtag.m_128437_("slot", 8);
        HashSet<String> slots = new HashSet<String>();
        for (int i = 0; i < listTag.size(); ++i) {
            slots.add(listTag.m_128778_(i));
        }
        listTag = compoundtag.m_128437_("index", 3);
        int min = 0;
        int max = -1;
        if (listTag.size() == 2) {
            min = Math.max(0, listTag.m_128763_(0));
            max = Math.max(min + 1, listTag.m_128763_(1));
        }
        ItemStack itemStack = stack = compoundtag.m_128441_("item") ? ItemStack.m_41712_((CompoundTag)compoundtag.m_128469_("item")) : null;
        if (stack != null) {
            stack.m_41764_(Math.max(1, stack.m_41613_()));
        }
        boolean exclusive = compoundtag.m_128471_("exclusive");
        int finalMin = min;
        int finalMax = max;
        parser.m_121272_(entity -> CuriosSelectorOptions.matches(entity, slots, finalMin, finalMax, stack, invert, exclusive));
    }

    private static boolean matches(Entity entity, Set<String> slots, int min, int max, ItemStack stack, boolean invert, boolean exclusive) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            return CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).map(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                if (stack != null) {
                    if (exclusive) {
                        return CuriosSelectorOptions.hasOnlyItem(curios, slots, min, max, stack, invert);
                    }
                    return CuriosSelectorOptions.hasItem(curios, slots, min, max, stack, invert);
                }
                if (!slots.isEmpty()) {
                    if (exclusive) {
                        return CuriosSelectorOptions.hasOnlySlot(curios, slots, max, invert);
                    }
                    return CuriosSelectorOptions.hasSlot(curios, slots, max, invert);
                }
                return true;
            }).orElse(false);
        }
        return false;
    }

    private static boolean hasOnlySlot(Map<String, ICurioStacksHandler> curios, Set<String> slots, int max, boolean invert) {
        boolean foundSlot = false;
        if (invert) {
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                if (CuriosSelectorOptions.matches(slots, max, entry.getKey(), entry.getValue())) {
                    foundSlot = true;
                    continue;
                }
                if (!foundSlot) continue;
                return true;
            }
            return false;
        }
        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
            if (CuriosSelectorOptions.matches(slots, max, entry.getKey(), entry.getValue())) {
                foundSlot = true;
                continue;
            }
            if (!foundSlot) continue;
            return false;
        }
        return foundSlot;
    }

    private static boolean hasSlot(Map<String, ICurioStacksHandler> curios, Set<String> slots, int max, boolean invert) {
        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
            if (!CuriosSelectorOptions.matches(slots, max, entry.getKey(), entry.getValue())) continue;
            return !invert;
        }
        return invert;
    }

    private static boolean matches(Set<String> slots, int max, String id, ICurioStacksHandler stacks) {
        int size = stacks.getSlots();
        return slots.contains(id) && size > 0 && (max == -1 || size >= max);
    }

    private static boolean hasOnlyItem(Map<String, ICurioStacksHandler> curios, Set<String> slots, int min, int max, ItemStack stack, boolean invert) {
        boolean foundItem = false;
        if (invert) {
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                if (!slots.isEmpty() && !slots.contains(entry.getKey())) continue;
                ICurioStacksHandler stacks = entry.getValue();
                int limit = max == -1 ? stacks.getSlots() : Math.min(stacks.getSlots(), max);
                for (int i = min; i < limit; ++i) {
                    ItemStack current = stacks.getStacks().getStackInSlot(i);
                    if (ItemStack.m_41728_((ItemStack)current, (ItemStack)stack)) {
                        foundItem = true;
                        continue;
                    }
                    if (!foundItem) continue;
                    return true;
                }
            }
            return false;
        }
        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
            if (!slots.isEmpty() && !slots.contains(entry.getKey())) continue;
            ICurioStacksHandler stacks = entry.getValue();
            int limit = max == -1 ? stacks.getSlots() : Math.min(stacks.getSlots(), max);
            for (int i = min; i < limit; ++i) {
                ItemStack current = stacks.getStacks().getStackInSlot(i);
                if (ItemStack.m_41728_((ItemStack)current, (ItemStack)stack)) {
                    foundItem = true;
                    continue;
                }
                if (!foundItem) continue;
                return false;
            }
        }
        return foundItem;
    }

    private static boolean hasItem(Map<String, ICurioStacksHandler> curios, Set<String> slots, int min, int max, ItemStack stack, boolean invert) {
        for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
            if (!slots.isEmpty() && !slots.contains(entry.getKey())) continue;
            ICurioStacksHandler stacks = entry.getValue();
            int limit = max == -1 ? stacks.getSlots() : Math.min(stacks.getSlots(), max);
            for (int i = min; i < limit; ++i) {
                ItemStack current = stacks.getStacks().getStackInSlot(i);
                if (!ItemStack.m_41728_((ItemStack)current, (ItemStack)stack)) continue;
                return !invert;
            }
        }
        return invert;
    }
}

