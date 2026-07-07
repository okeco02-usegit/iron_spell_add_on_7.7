/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  com.mojang.logging.LogUtils
 *  javax.annotation.Nonnull
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.common.capabilities.ICapabilityProvider
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.fml.loading.FMLLoader
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.slf4j.Logger
 */
package top.theillusivec4.curios.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;
import top.theillusivec4.curios.api.type.util.IIconHelper;
import top.theillusivec4.curios.api.type.util.ISlotHelper;

public final class CuriosApi {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "curios";
    private static IIconHelper iconHelper;
    private static ICuriosHelper curiosHelper;
    private static ISlotHelper slotHelper;

    public static void registerCurio(Item item, ICurioItem curio) {
        CuriosApi.apiError();
    }

    public static Optional<ISlotType> getSlot(String id, Level level) {
        return CuriosApi.getSlot(id, level.m_5776_());
    }

    public static Optional<ISlotType> getSlot(String id, boolean isClient) {
        return Optional.ofNullable(CuriosApi.getSlots(isClient).get(id));
    }

    public static Map<String, ISlotType> getSlots(Level level) {
        return CuriosApi.getSlots(level.m_5776_());
    }

    public static Map<String, ISlotType> getSlots(boolean isClient) {
        CuriosApi.apiError();
        return Map.of();
    }

    public static Map<String, ISlotType> getPlayerSlots(Level level) {
        return CuriosApi.getPlayerSlots(level.m_5776_());
    }

    public static Map<String, ISlotType> getPlayerSlots(boolean isClient) {
        return CuriosApi.getEntitySlots(EntityType.f_20532_, isClient);
    }

    public static Map<String, ISlotType> getPlayerSlots(Player player) {
        return CuriosApi.getEntitySlots((LivingEntity)player);
    }

    public static Map<String, ISlotType> getEntitySlots(LivingEntity livingEntity) {
        return livingEntity != null ? CuriosApi.getEntitySlots(livingEntity.m_6095_(), livingEntity.m_9236_()) : Map.of();
    }

    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type, Level level) {
        return CuriosApi.getEntitySlots(type, level.m_5776_());
    }

    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type, boolean isClient) {
        CuriosApi.apiError();
        return Map.of();
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, Level level) {
        return CuriosApi.getItemStackSlots(stack, level.m_5776_());
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, boolean isClient) {
        CuriosApi.apiError();
        return Map.of();
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, LivingEntity livingEntity) {
        CuriosApi.apiError();
        return Map.of();
    }

    public static LazyOptional<ICurio> getCurio(ItemStack stack) {
        CuriosApi.apiError();
        return LazyOptional.empty();
    }

    @Nonnull
    public static ICapabilityProvider createCurioProvider(ICurio curio) {
        CuriosApi.apiError();
        return Items.f_41852_.m_7968_();
    }

    public static LazyOptional<ICuriosItemHandler> getCuriosInventory(LivingEntity livingEntity) {
        CuriosApi.apiError();
        return LazyOptional.empty();
    }

    public static boolean isStackValid(SlotContext slotContext, ItemStack stack) {
        CuriosApi.apiError();
        return false;
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        CuriosApi.apiError();
        return HashMultimap.create();
    }

    public static void addSlotModifier(Multimap<Attribute, AttributeModifier> map, String identifier, UUID uuid, double amount, AttributeModifier.Operation operation) {
        CuriosApi.apiError();
    }

    public static void addSlotModifier(ItemStack stack, String identifier, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        CuriosApi.apiError();
    }

    public static void addModifier(ItemStack stack, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        CuriosApi.apiError();
    }

    public static void registerCurioPredicate(ResourceLocation resourceLocation, Predicate<SlotResult> predicate) {
        CuriosApi.apiError();
    }

    public static Optional<Predicate<SlotResult>> getCurioPredicate(ResourceLocation resourceLocation) {
        CuriosApi.apiError();
        return Optional.empty();
    }

    public static Map<ResourceLocation, Predicate<SlotResult>> getCurioPredicates() {
        CuriosApi.apiError();
        return Map.of();
    }

    public static boolean testCurioPredicates(Set<ResourceLocation> predicates, SlotResult slotResult) {
        CuriosApi.apiError();
        return true;
    }

    public static UUID getSlotUuid(SlotContext slotContext) {
        CuriosApi.apiError();
        return UUID.randomUUID();
    }

    public static void broadcastCurioBreakEvent(SlotContext slotContext) {
        CuriosApi.apiError();
    }

    static void apiError() {
        LOGGER.error("Missing Curios API implementation!");
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static Optional<ISlotType> getSlot(String id) {
        return CuriosApi.getSlot(id, false);
    }

    @Nonnull
    public static ResourceLocation getSlotIcon(String id) {
        return CuriosApi.getSlot(id, true).map(ISlotType::getIcon).orElse(new ResourceLocation(MODID, "slot/empty_curio_slot"));
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static Map<String, ISlotType> getSlots() {
        return CuriosApi.getSlots(false);
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type) {
        return CuriosApi.getEntitySlots(type, false);
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static Map<String, ISlotType> getPlayerSlots() {
        return CuriosApi.getPlayerSlots(false);
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack) {
        return CuriosApi.getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT);
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static void setIconHelper(IIconHelper helper) {
        if (iconHelper == null) {
            iconHelper = helper;
        }
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static IIconHelper getIconHelper() {
        return iconHelper;
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static void setCuriosHelper(ICuriosHelper helper) {
        if (curiosHelper == null) {
            curiosHelper = helper;
        }
    }

    @Deprecated(since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static ICuriosHelper getCuriosHelper() {
        return curiosHelper;
    }

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static ISlotHelper getSlotHelper() {
        return slotHelper;
    }

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public static void setSlotHelper(ISlotHelper helper) {
        slotHelper = helper;
    }
}

