/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.monster.EnderMan
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 */
package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurio {
    public ItemStack getStack();

    default public void curioTick(SlotContext slotContext) {
        this.curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    default public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        this.onEquip(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    default public void onUnequip(SlotContext slotContext, ItemStack newStack) {
        this.onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    default public boolean canEquip(SlotContext slotContext) {
        return true;
    }

    default public boolean canUnequip(SlotContext slotContext) {
        return true;
    }

    default public List<Component> getSlotsTooltip(List<Component> tooltips) {
        return this.getTagsTooltip(tooltips);
    }

    default public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
        return this.getAttributeModifiers(slotContext.identifier());
    }

    default public void onEquipFromUse(SlotContext slotContext) {
        this.playRightClickEquipSound(slotContext.entity());
    }

    @Nonnull
    default public SoundInfo getEquipSound(SlotContext slotContext) {
        return new SoundInfo(SoundEvents.f_11675_, 1.0f, 1.0f);
    }

    default public boolean canEquipFromUse(SlotContext slotContext) {
        return this.canRightClickEquip();
    }

    default public void curioBreak(SlotContext slotContext) {
        this.curioBreak(this.getStack(), slotContext.entity());
    }

    default public boolean canSync(SlotContext slotContext) {
        return this.canSync(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    @Nullable
    default public CompoundTag writeSyncData(SlotContext slotContext) {
        return this.writeSyncData();
    }

    default public void readSyncData(SlotContext slotContext, CompoundTag compound) {
        this.readSyncData(compound);
    }

    @Nonnull
    default public DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
        return this.getDropRule(slotContext.entity());
    }

    default public List<Component> getAttributesTooltip(List<Component> tooltips) {
        return this.showAttributesTooltip("") ? tooltips : new ArrayList();
    }

    default public int getFortuneLevel(SlotContext slotContext, @Nullable LootContext lootContext) {
        return this.getFortuneBonus(slotContext.identifier(), slotContext.entity(), this.getStack(), slotContext.index());
    }

    default public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting) {
        return this.getLootingBonus(slotContext.identifier(), slotContext.entity(), this.getStack(), slotContext.index());
    }

    default public boolean makesPiglinsNeutral(SlotContext slotContext) {
        return this.getStack().makesPiglinsNeutral(slotContext.entity());
    }

    default public boolean canWalkOnPowderedSnow(SlotContext slotContext) {
        return this.getStack().canWalkOnPowderedSnow(slotContext.entity());
    }

    default public boolean isEnderMask(SlotContext slotContext, EnderMan enderMan) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            return this.getStack().isEnderMask(player, enderMan);
        }
        return false;
    }

    public static void playBreakAnimation(ItemStack stack, LivingEntity livingEntity) {
        if (!stack.m_41619_()) {
            if (!livingEntity.m_20067_()) {
                livingEntity.m_9236_().m_7785_(livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_(), SoundEvents.f_12018_, livingEntity.m_5720_(), 0.8f, 0.8f + livingEntity.m_9236_().f_46441_.m_188501_() * 0.4f, false);
            }
            for (int i = 0; i < 5; ++i) {
                Vec3 vec3d = new Vec3(((double)livingEntity.m_217043_().m_188501_() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3d = vec3d.m_82496_(-livingEntity.m_146909_() * ((float)Math.PI / 180));
                vec3d = vec3d.m_82524_(-livingEntity.m_146908_() * ((float)Math.PI / 180));
                double d0 = (double)(-livingEntity.m_217043_().m_188501_()) * 0.6 - 0.3;
                Vec3 vec3d1 = new Vec3(((double)livingEntity.m_217043_().m_188501_() - 0.5) * 0.3, d0, 0.6);
                vec3d1 = vec3d1.m_82496_(-livingEntity.m_146909_() * ((float)Math.PI / 180));
                vec3d1 = vec3d1.m_82524_(-livingEntity.m_146908_() * ((float)Math.PI / 180));
                vec3d1 = vec3d1.m_82520_(livingEntity.m_20185_(), livingEntity.m_20186_() + (double)livingEntity.m_20192_(), livingEntity.m_20189_());
                livingEntity.m_9236_().m_7106_((ParticleOptions)new ItemParticleOption(ParticleTypes.f_123752_, stack), vec3d1.f_82479_, vec3d1.f_82480_, vec3d1.f_82481_, vec3d.f_82479_, vec3d.f_82480_ + 0.05, vec3d.f_82481_);
            }
        }
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void curioTick(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void curioAnimate(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        ICurio.playBreakAnimation(stack, livingEntity);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void onEquip(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void onUnequip(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canEquip(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canUnequip(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public List<Component> getTagsTooltip(List<Component> tagTooltips) {
        return tagTooltips;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return EnchantmentHelper.m_44843_((Enchantment)Enchantments.f_44987_, (ItemStack)curio);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return EnchantmentHelper.m_44843_((Enchantment)Enchantments.f_44982_, (ItemStack)curio);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean showAttributesTooltip(String identifier) {
        return true;
    }

    @Nonnull
    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public DropRule getDropRule(LivingEntity livingEntity) {
        return DropRule.DEFAULT;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
        return false;
    }

    @Deprecated(forRemoval=true)
    @Nullable
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public CompoundTag writeSyncData() {
        return new CompoundTag();
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void readSyncData(CompoundTag compound) {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean canRightClickEquip() {
        return false;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void playRightClickEquipSound(LivingEntity livingEntity) {
        SoundInfo soundInfo = this.getEquipSound(new SlotContext("", livingEntity, 0, false, true));
        livingEntity.m_9236_().m_5594_(null, livingEntity.m_20183_(), soundInfo.getSoundEvent(), livingEntity.m_5720_(), soundInfo.getVolume(), soundInfo.getPitch());
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
        return HashMultimap.create();
    }

    public record SoundInfo(SoundEvent soundEvent, float volume, float pitch) {
        @Deprecated(forRemoval=true, since="1.20.1")
        @ApiStatus.ScheduledForRemoval(inVersion="1.22")
        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }

        @Deprecated(forRemoval=true, since="1.20.1")
        @ApiStatus.ScheduledForRemoval(inVersion="1.22")
        public float getVolume() {
            return this.volume;
        }

        @Deprecated(forRemoval=true, since="1.20.1")
        @ApiStatus.ScheduledForRemoval(inVersion="1.22")
        public float getPitch() {
            return this.pitch;
        }
    }

    public static enum DropRule {
        DEFAULT,
        ALWAYS_DROP,
        ALWAYS_KEEP,
        DESTROY;

    }
}

