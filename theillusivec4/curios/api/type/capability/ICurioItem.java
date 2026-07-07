/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  javax.annotation.Nonnull
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.monster.EnderMan
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 */
package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.ApiStatus;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public interface ICurioItem {
    public static final ICurio defaultInstance = () -> ItemStack.f_41583_;

    default public boolean hasCurioCapability(ItemStack stack) {
        return true;
    }

    default public void curioTick(SlotContext slotContext, ItemStack stack) {
        this.curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    default public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        this.onEquip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    default public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        this.onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    default public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return this.canEquip(slotContext.identifier(), slotContext.entity(), stack);
    }

    default public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return this.canUnequip(slotContext.identifier(), slotContext.entity(), stack);
    }

    default public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        return this.getTagsTooltip(tooltips, stack);
    }

    default public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return this.getAttributeModifiers(slotContext.identifier(), stack);
    }

    default public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        this.playRightClickEquipSound(slotContext.entity(), stack);
    }

    @Nonnull
    default public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.f_11675_, 1.0f, 1.0f);
    }

    default public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return this.canRightClickEquip(stack);
    }

    default public void curioBreak(SlotContext slotContext, ItemStack stack) {
        this.curioBreak(stack, slotContext.entity());
    }

    default public boolean canSync(SlotContext slotContext, ItemStack stack) {
        return this.canSync(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Nonnull
    default public CompoundTag writeSyncData(SlotContext slotContext, ItemStack stack) {
        return this.writeSyncData(stack);
    }

    default public void readSyncData(SlotContext slotContext, CompoundTag compound, ItemStack stack) {
        this.readSyncData(compound, stack);
    }

    @Nonnull
    default public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return this.getDropRule(slotContext.entity(), stack);
    }

    default public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return this.showAttributesTooltip("", stack) ? tooltips : new ArrayList();
    }

    default public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        return this.getFortuneBonus(slotContext.identifier(), slotContext.entity(), stack, slotContext.index());
    }

    default public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return this.getLootingBonus(slotContext.identifier(), slotContext.entity(), stack, slotContext.index());
    }

    default public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return stack.makesPiglinsNeutral(slotContext.entity());
    }

    default public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return stack.canWalkOnPowderedSnow(slotContext.entity());
    }

    default public boolean isEnderMask(SlotContext slotContext, EnderMan enderMan, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            return stack.isEnderMask(player, enderMan);
        }
        return false;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.curioTick(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void curioAnimate(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.curioAnimate(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void onEquip(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.onEquip(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void onUnequip(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.onUnequip(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.canEquip(identifier, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canUnequip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.canUnequip(identifier, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public List<Component> getTagsTooltip(List<Component> tagTooltips, ItemStack stack) {
        return defaultInstance.getTagsTooltip(tagTooltips);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void playRightClickEquipSound(LivingEntity livingEntity, ItemStack stack) {
        ICurio.SoundInfo soundInfo = this.getEquipSound(new SlotContext("", livingEntity, 0, false, true), stack);
        livingEntity.m_9236_().m_5594_(null, livingEntity.m_20183_(), soundInfo.getSoundEvent(), livingEntity.m_5720_(), soundInfo.getVolume(), soundInfo.getPitch());
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canRightClickEquip(ItemStack stack) {
        return defaultInstance.canRightClickEquip();
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier, ItemStack stack) {
        return defaultInstance.getAttributeModifiers(identifier);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        defaultInstance.curioBreak(stack, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canSync(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.canSync(identifier, index, livingEntity);
    }

    @Nonnull
    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public CompoundTag writeSyncData(ItemStack stack) {
        return defaultInstance.writeSyncData();
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void readSyncData(CompoundTag compound, ItemStack stack) {
        defaultInstance.readSyncData(compound);
    }

    @Nonnull
    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public ICurio.DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.getDropRule(livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean showAttributesTooltip(String identifier, ItemStack stack) {
        return defaultInstance.showAttributesTooltip(identifier);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return defaultInstance.getFortuneBonus(identifier, livingEntity, curio, index);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return defaultInstance.getLootingBonus(identifier, livingEntity, curio, index);
    }
}

