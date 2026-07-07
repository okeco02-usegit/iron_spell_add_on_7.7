/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.LinkedHashMultimap
 *  com.google.common.collect.Multimap
 *  com.google.common.collect.Multimaps
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.eventbus.api.Event
 *  org.jetbrains.annotations.Nullable
 */
package top.theillusivec4.curios.api.event;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

public class CurioAttributeModifierEvent
extends Event {
    private final ItemStack stack;
    private final SlotContext slotContext;
    private final UUID uuid;
    private final Multimap<Attribute, AttributeModifier> originalModifiers;
    private Multimap<Attribute, AttributeModifier> unmodifiableModifiers;
    @Nullable
    private Multimap<Attribute, AttributeModifier> modifiableModifiers;

    public CurioAttributeModifierEvent(ItemStack stack, SlotContext slotContext, UUID uuid, Multimap<Attribute, AttributeModifier> modifiers) {
        this.stack = stack;
        this.slotContext = slotContext;
        this.originalModifiers = modifiers;
        this.unmodifiableModifiers = this.originalModifiers;
        this.uuid = uuid;
    }

    public Multimap<Attribute, AttributeModifier> getModifiers() {
        return this.unmodifiableModifiers;
    }

    public Multimap<Attribute, AttributeModifier> getOriginalModifiers() {
        return this.originalModifiers;
    }

    private Multimap<Attribute, AttributeModifier> getModifiableMap() {
        if (this.modifiableModifiers == null) {
            this.modifiableModifiers = LinkedHashMultimap.create(this.originalModifiers);
            this.unmodifiableModifiers = Multimaps.unmodifiableMultimap(this.modifiableModifiers);
        }
        return this.modifiableModifiers;
    }

    public boolean addModifier(Attribute attribute, AttributeModifier modifier) {
        return this.getModifiableMap().put((Object)attribute, (Object)modifier);
    }

    public boolean removeModifier(Attribute attribute, AttributeModifier modifier) {
        return this.getModifiableMap().remove((Object)attribute, (Object)modifier);
    }

    public Collection<AttributeModifier> removeAttribute(Attribute attribute) {
        return this.getModifiableMap().removeAll((Object)attribute);
    }

    public void clearModifiers() {
        this.getModifiableMap().clear();
    }

    public SlotContext getSlotContext() {
        return this.slotContext;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}

