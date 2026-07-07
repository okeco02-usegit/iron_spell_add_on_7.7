/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package top.theillusivec4.curios.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

public class CuriosTooltip {
    private final List<Component> content = new ArrayList<Component>();
    private final Set<String> identifiers = new HashSet<String>();
    private ItemStack stack = ItemStack.f_41583_;
    private LivingEntity livingEntity;

    public CuriosTooltip append(Component component) {
        this.content.add(component);
        return this;
    }

    public CuriosTooltip appendHeader(MutableComponent component) {
        return this.append((Component)component.m_130940_(ChatFormatting.GOLD));
    }

    public CuriosTooltip appendSlotHeader(String identifier) {
        return this.append((Component)Component.m_237115_((String)("curios.modifiers." + identifier)).m_130940_(ChatFormatting.GOLD));
    }

    public CuriosTooltip appendAdditive(MutableComponent component) {
        return this.append((Component)component.m_130940_(ChatFormatting.BLUE));
    }

    public CuriosTooltip appendSubtractive(MutableComponent component) {
        return this.append((Component)component.m_130940_(ChatFormatting.RED));
    }

    public CuriosTooltip appendEqual(MutableComponent component) {
        return this.append((Component)component.m_130940_(ChatFormatting.DARK_GREEN));
    }

    public CuriosTooltip forSlots(String ... identifiers) {
        this.identifiers.addAll(Arrays.asList(identifiers));
        return this;
    }

    public CuriosTooltip forSlots(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public CuriosTooltip forSlots(ItemStack stack, LivingEntity livingEntity) {
        this.stack = stack;
        this.livingEntity = livingEntity;
        return this;
    }

    public List<Component> build() {
        ArrayList<Component> result = new ArrayList<Component>();
        TreeSet<String> ids = new TreeSet<String>();
        if (!this.identifiers.isEmpty()) {
            ids.addAll(this.identifiers);
        } else if (!this.stack.m_41619_()) {
            Map<String, ISlotType> map = this.livingEntity != null ? CuriosApi.getItemStackSlots(this.stack, this.livingEntity) : CuriosApi.getItemStackSlots(this.stack, true);
            ids.addAll(map.keySet());
        }
        for (String identifier : ids) {
            result.add((Component)Component.m_237119_());
            result.add((Component)Component.m_237115_((String)("curios.modifiers." + identifier)).m_130940_(ChatFormatting.GOLD));
            result.addAll(this.content);
        }
        return result;
    }
}

