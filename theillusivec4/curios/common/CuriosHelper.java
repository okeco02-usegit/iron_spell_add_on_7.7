/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  javax.annotation.Nonnull
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.fml.loading.FMLLoader
 *  net.minecraftforge.items.IItemHandlerModifiable
 *  org.apache.commons.lang3.tuple.ImmutableTriple
 *  org.apache.logging.log4j.util.TriConsumer
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 */
package top.theillusivec4.curios.common;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

public class CuriosHelper
implements ICuriosHelper {
    @Override
    public LazyOptional<ICurio> getCurio(ItemStack stack) {
        return CuriosApi.getCurio(stack);
    }

    @Override
    public LazyOptional<ICuriosItemHandler> getCuriosHandler(@Nonnull LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity);
    }

    @Override
    public Set<String> getCurioTags(Item item) {
        return CuriosApi.getItemStackSlots(item.m_7968_(), FMLLoader.getDist() == Dist.CLIENT).keySet();
    }

    @Override
    public LazyOptional<IItemHandlerModifiable> getEquippedCurios(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity).lazyMap(ICuriosItemHandler::getEquippedCurios);
    }

    @Override
    public void setEquippedCurio(@NotNull LivingEntity livingEntity, String identifier, int index, ItemStack stack) {
        CuriosApi.getCuriosInventory(livingEntity).ifPresent(inv -> inv.setEquippedCurio(identifier, index, stack));
    }

    @Override
    public Optional<SlotResult> findFirstCurio(@Nonnull LivingEntity livingEntity, Item item) {
        return this.findFirstCurio(livingEntity, (ItemStack stack) -> stack.m_41720_() == item);
    }

    @Override
    public Optional<SlotResult> findFirstCurio(@Nonnull LivingEntity livingEntity, Predicate<ItemStack> filter) {
        return CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findFirstCurio(filter)).orElse(Optional.empty());
    }

    @Override
    public List<SlotResult> findCurios(@Nonnull LivingEntity livingEntity, Item item) {
        return this.findCurios(livingEntity, (ItemStack stack) -> stack.m_41720_() == item);
    }

    @Override
    public List<SlotResult> findCurios(@Nonnull LivingEntity livingEntity, Predicate<ItemStack> filter) {
        return CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findCurios(filter)).orElse(Collections.emptyList());
    }

    @Override
    public List<SlotResult> findCurios(@NotNull LivingEntity livingEntity, String ... identifiers) {
        return CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findCurios(identifiers)).orElse(Collections.emptyList());
    }

    @Override
    public Optional<SlotResult> findCurio(@Nonnull LivingEntity livingEntity, String identifier, int index) {
        return CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findCurio(identifier, index)).orElse(Optional.empty());
    }

    @Override
    @Nonnull
    public Optional<ImmutableTriple<String, Integer, ItemStack>> findEquippedCurio(Item item, @Nonnull LivingEntity livingEntity) {
        return this.findEquippedCurio((ItemStack stack) -> stack.m_41720_() == item, livingEntity);
    }

    @Override
    @Nonnull
    public Optional<ImmutableTriple<String, Integer, ItemStack>> findEquippedCurio(Predicate<ItemStack> filter, @Nonnull LivingEntity livingEntity) {
        ImmutableTriple result = this.getCuriosHandler(livingEntity).map(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (String id : curios.keySet()) {
                ICurioStacksHandler stacksHandler = curios.get(id);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); ++i) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if (stack.m_41619_() || !filter.test(stack)) continue;
                    return new ImmutableTriple((Object)id, (Object)i, (Object)stack);
                }
            }
            return new ImmutableTriple((Object)"", (Object)0, (Object)ItemStack.f_41583_);
        }).orElse(new ImmutableTriple((Object)"", (Object)0, (Object)ItemStack.f_41583_));
        return ((String)result.getLeft()).isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier, ItemStack stack) {
        return this.getAttributeModifiers(new SlotContext(identifier, null, 0, false, true), UUID.randomUUID(), stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void addSlotModifier(Multimap<Attribute, AttributeModifier> map, String identifier, UUID uuid, double amount, AttributeModifier.Operation operation) {
        CuriosApi.addSlotModifier(map, identifier, uuid, amount, operation);
    }

    @Override
    public void addSlotModifier(ItemStack stack, String identifier, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        this.addModifier(stack, SlotAttribute.getOrCreate(identifier), name, uuid, amount, operation, slot);
    }

    @Override
    public void addModifier(ItemStack stack, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        CuriosApi.addModifier(stack, attribute, name, uuid, amount, operation, slot);
    }

    @Override
    public boolean isStackValid(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.isStackValid(slotContext, stack);
    }

    @Override
    public void onBrokenCurio(SlotContext slotContext) {
        CuriosApi.broadcastCurioBreakEvent(slotContext);
    }

    @Override
    public void setBrokenCurioConsumer(Consumer<SlotContext> consumer) {
    }

    @Override
    public void onBrokenCurio(String id, int index, LivingEntity damager) {
        CuriosApi.broadcastCurioBreakEvent(new SlotContext(id, damager, index, false, true));
    }

    @Override
    public void setBrokenCurioConsumer(TriConsumer<String, Integer, LivingEntity> consumer) {
    }

    @Deprecated(since="1.20.1", forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.20.2")
    public static SlotAttributeWrapper getOrCreateSlotAttribute(String identifier) {
        return (SlotAttributeWrapper)SlotAttribute.getOrCreate(identifier);
    }

    @Deprecated(since="1.20.1", forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.20.2")
    public static class SlotAttributeWrapper
    extends SlotAttribute {
        public final String identifier;

        public SlotAttributeWrapper(String identifier) {
            super(identifier);
            this.identifier = identifier;
        }
    }
}

