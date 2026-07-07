/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  com.mojang.logging.LogUtils
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.util.Tuple
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraftforge.items.IItemHandlerModifiable
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.slf4j.Logger
 */
package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public interface ICuriosItemHandler {
    public static final Logger LOGGER = LogUtils.getLogger();

    public Map<String, ICurioStacksHandler> getCurios();

    public void setCurios(Map<String, ICurioStacksHandler> var1);

    public int getSlots();

    default public int getVisibleSlots() {
        return this.getSlots();
    }

    public void reset();

    public Optional<ICurioStacksHandler> getStacksHandler(String var1);

    public IItemHandlerModifiable getEquippedCurios();

    public void setEquippedCurio(String var1, int var2, ItemStack var3);

    default public boolean isEquipped(Item item) {
        return this.findFirstCurio(item).isPresent();
    }

    default public boolean isEquipped(Predicate<ItemStack> filter) {
        return this.findFirstCurio(filter).isPresent();
    }

    public Optional<SlotResult> findFirstCurio(Item var1);

    public Optional<SlotResult> findFirstCurio(Predicate<ItemStack> var1);

    public List<SlotResult> findCurios(Item var1);

    public List<SlotResult> findCurios(Predicate<ItemStack> var1);

    public List<SlotResult> findCurios(String ... var1);

    public Optional<SlotResult> findCurio(String var1, int var2);

    public LivingEntity getWearer();

    public void loseInvalidStack(ItemStack var1);

    public void handleInvalidStacks();

    public int getFortuneLevel(@Nullable LootContext var1);

    public int getLootingLevel(DamageSource var1, LivingEntity var2, int var3);

    public ListTag saveInventory(boolean var1);

    default public ListTag saveInventory(boolean clear, Predicate<ItemStack> filter) {
        return this.saveInventory(clear);
    }

    default public ListTag saveInventory(boolean clear, BiPredicate<ItemStack, SlotContext> filter) {
        return this.saveInventory(clear);
    }

    public void loadInventory(ListTag var1);

    public Set<ICurioStacksHandler> getUpdatingInventories();

    default public void addTransientSlotModifier(String slot, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
        LOGGER.error("Missing method implementation!");
    }

    public void addTransientSlotModifiers(Multimap<String, AttributeModifier> var1);

    default public void addPermanentSlotModifier(String slot, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
        LOGGER.error("Missing method implementation!");
    }

    public void addPermanentSlotModifiers(Multimap<String, AttributeModifier> var1);

    default public void removeSlotModifier(String slot, UUID uuid) {
        LOGGER.error("Missing method implementation!");
    }

    public void removeSlotModifiers(Multimap<String, AttributeModifier> var1);

    public void clearSlotModifiers();

    public Multimap<String, AttributeModifier> getModifiers();

    public Tag writeTag();

    public void readTag(Tag var1);

    public void clearCachedSlotModifiers();

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public Set<String> getLockedSlots() {
        return new HashSet<String>();
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void unlockSlotType(String identifier, int amount, boolean visible, boolean cosmetic) {
        this.growSlotType(identifier, amount);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void lockSlotType(String identifier) {
        this.shrinkSlotType(identifier, 1);
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void processSlots() {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public int getFortuneBonus() {
        return 0;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public int getLootingBonus() {
        return 0;
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public void setEnchantmentBonuses(Tuple<Integer, Integer> fortuneAndLooting) {
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void growSlotType(String var1, int var2);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void shrinkSlotType(String var1, int var2);
}

