/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.LinkedHashMultimap
 *  com.google.common.collect.Multimap
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.Direction
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.ICapabilityProvider
 *  net.minecraftforge.common.capabilities.ICapabilitySerializable
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.items.IItemHandlerModifiable
 *  net.minecraftforge.items.ItemHandlerHelper
 *  net.minecraftforge.items.ItemStackHandler
 *  net.minecraftforge.items.wrapper.CombinedInvWrapper
 */
package top.theillusivec4.curios.common.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.inventory.CurioStacksHandler;

public class CurioInventoryCapability {
    public static ICapabilityProvider createProvider(LivingEntity livingEntity) {
        return new Provider(livingEntity);
    }

    public static class Provider
    implements ICapabilitySerializable<Tag> {
        final LazyOptional<ICuriosItemHandler> optional;
        final ICuriosItemHandler handler;
        final LivingEntity wearer;

        Provider(LivingEntity livingEntity) {
            this.wearer = livingEntity;
            this.handler = new CurioInventoryWrapper(this.wearer);
            this.optional = LazyOptional.of(() -> this.handler);
        }

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nullable Capability<T> capability, Direction facing) {
            if (CuriosApi.getEntitySlots(this.wearer).isEmpty()) {
                return LazyOptional.empty();
            }
            return CuriosCapability.INVENTORY.orEmpty(capability, this.optional);
        }

        public Tag serializeNBT() {
            return this.handler.writeTag();
        }

        public void deserializeNBT(Tag nbt) {
            this.handler.readTag(nbt);
        }
    }

    public static class CurioInventoryWrapper
    implements ICuriosItemHandler {
        Map<String, ICurioStacksHandler> curios = new LinkedHashMap<String, ICurioStacksHandler>();
        NonNullList<ItemStack> invalidStacks = NonNullList.m_122779_();
        LivingEntity wearer;
        Set<ICurioStacksHandler> updates = new HashSet<ICurioStacksHandler>();

        public CurioInventoryWrapper(LivingEntity livingEntity) {
            this.wearer = livingEntity;
            this.reset();
        }

        @Override
        public void reset() {
            if (this.wearer != null) {
                this.curios.clear();
                this.invalidStacks.clear();
                TreeSet<ISlotType> sorted = new TreeSet<ISlotType>(CuriosApi.getEntitySlots(this.wearer).values());
                for (ISlotType slotType : sorted) {
                    this.curios.put(slotType.getIdentifier(), new CurioStacksHandler(this, slotType.getIdentifier(), slotType.getSize(), slotType.useNativeGui(), slotType.hasCosmetic(), slotType.canToggleRendering(), slotType.getDropRule()));
                }
            }
        }

        @Override
        public int getSlots() {
            int totalSlots = 0;
            for (ICurioStacksHandler stacks : this.curios.values()) {
                totalSlots += stacks.getSlots();
            }
            return totalSlots;
        }

        @Override
        public int getVisibleSlots() {
            int totalSlots = 0;
            for (ICurioStacksHandler stacks : this.curios.values()) {
                if (!stacks.isVisible()) continue;
                totalSlots += stacks.getSlots();
            }
            return totalSlots;
        }

        @Override
        public Optional<ICurioStacksHandler> getStacksHandler(String identifier) {
            return Optional.ofNullable(this.curios.get(identifier));
        }

        @Override
        public IItemHandlerModifiable getEquippedCurios() {
            Map<String, ICurioStacksHandler> curios = this.getCurios();
            IItemHandlerModifiable[] itemHandlers = new IItemHandlerModifiable[curios.size()];
            int index = 0;
            for (ICurioStacksHandler stacksHandler : curios.values()) {
                if (index >= itemHandlers.length) continue;
                itemHandlers[index] = stacksHandler.getStacks();
                ++index;
            }
            return new CombinedInvWrapper(itemHandlers);
        }

        @Override
        public void setEquippedCurio(String identifier, int index, ItemStack stack) {
            IDynamicStackHandler stackHandler;
            Map<String, ICurioStacksHandler> curios = this.getCurios();
            ICurioStacksHandler stacksHandler = curios.get(identifier);
            if (stacksHandler != null && index < (stackHandler = stacksHandler.getStacks()).getSlots()) {
                stackHandler.setStackInSlot(index, stack);
            }
        }

        @Override
        public Optional<SlotResult> findFirstCurio(Item item) {
            return this.findFirstCurio((ItemStack stack) -> stack.m_41720_() == item);
        }

        @Override
        public Optional<SlotResult> findFirstCurio(Predicate<ItemStack> filter) {
            Map<String, ICurioStacksHandler> curios = this.getCurios();
            for (String id : curios.keySet()) {
                ICurioStacksHandler stacksHandler = curios.get(id);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); ++i) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if (stack.m_41619_() || !filter.test(stack)) continue;
                    NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                    return Optional.of(new SlotResult(new SlotContext(id, this.wearer, i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false), stack));
                }
            }
            return Optional.empty();
        }

        @Override
        public List<SlotResult> findCurios(Item item) {
            return this.findCurios((ItemStack stack) -> stack.m_41720_() == item);
        }

        @Override
        public List<SlotResult> findCurios(Predicate<ItemStack> filter) {
            ArrayList<SlotResult> result = new ArrayList<SlotResult>();
            Map<String, ICurioStacksHandler> curios = this.getCurios();
            for (String id : curios.keySet()) {
                ICurioStacksHandler stacksHandler = curios.get(id);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); ++i) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if (stack.m_41619_() || !filter.test(stack)) continue;
                    NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                    result.add(new SlotResult(new SlotContext(id, this.wearer, i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false), stack));
                }
            }
            return result;
        }

        @Override
        public List<SlotResult> findCurios(String ... identifiers) {
            ArrayList<SlotResult> result = new ArrayList<SlotResult>();
            HashSet<String> ids = new HashSet<String>(List.of(identifiers));
            Map<String, ICurioStacksHandler> curios = this.getCurios();
            for (String id : curios.keySet()) {
                if (!ids.contains(id)) continue;
                ICurioStacksHandler stacksHandler = curios.get(id);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); ++i) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if (stack.m_41619_()) continue;
                    NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                    result.add(new SlotResult(new SlotContext(id, this.wearer, i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false), stack));
                }
            }
            return result;
        }

        @Override
        public Optional<SlotResult> findCurio(String identifier, int index) {
            ItemStack stack;
            IDynamicStackHandler stackHandler;
            Map<String, ICurioStacksHandler> curios = this.getCurios();
            ICurioStacksHandler stacksHandler = curios.get(identifier);
            if (stacksHandler != null && index < (stackHandler = stacksHandler.getStacks()).getSlots() && !(stack = stackHandler.getStackInSlot(index)).m_41619_()) {
                NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                return Optional.of(new SlotResult(new SlotContext(identifier, this.wearer, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false), stack));
            }
            return Optional.empty();
        }

        @Override
        public Map<String, ICurioStacksHandler> getCurios() {
            return Collections.unmodifiableMap(this.curios);
        }

        @Override
        public void setCurios(Map<String, ICurioStacksHandler> curios) {
            this.curios = curios;
        }

        @Override
        public void growSlotType(String identifier, int amount) {
            if (amount > 0) {
                this.getStacksHandler(identifier).ifPresent(stackHandler -> stackHandler.grow(amount));
            }
        }

        @Override
        public void shrinkSlotType(String identifier, int amount) {
            if (amount > 0) {
                this.getStacksHandler(identifier).ifPresent(stackHandler -> stackHandler.shrink(amount));
            }
        }

        @Override
        @Nullable
        public LivingEntity getWearer() {
            return this.wearer;
        }

        @Override
        public void loseInvalidStack(ItemStack stack) {
            this.invalidStacks.add((Object)stack);
        }

        @Override
        public void handleInvalidStacks() {
            if (this.wearer != null && !this.invalidStacks.isEmpty()) {
                LivingEntity livingEntity = this.wearer;
                if (livingEntity instanceof Player) {
                    Player player = (Player)livingEntity;
                    this.invalidStacks.forEach(drop -> ItemHandlerHelper.giveItemToPlayer((Player)player, (ItemStack)drop));
                } else {
                    this.invalidStacks.forEach(drop -> {
                        ItemEntity ent = this.wearer.m_5552_(drop, 1.0f);
                        RandomSource rand = this.wearer.m_217043_();
                        if (ent != null) {
                            ent.m_20256_(ent.m_20184_().m_82520_((double)((rand.m_188501_() - rand.m_188501_()) * 0.1f), (double)(rand.m_188501_() * 0.05f), (double)((rand.m_188501_() - rand.m_188501_()) * 0.1f)));
                        }
                    });
                }
                this.invalidStacks = NonNullList.m_122779_();
            }
        }

        @Override
        public int getFortuneLevel(@Nullable LootContext lootContext) {
            int fortuneLevel = 0;
            for (Map.Entry<String, ICurioStacksHandler> entry : this.getCurios().entrySet()) {
                IDynamicStackHandler stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    int index = i;
                    fortuneLevel += CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> {
                        NonNullList<Boolean> renderStates = ((ICurioStacksHandler)entry.getValue()).getRenders();
                        return curio.getFortuneLevel(new SlotContext((String)entry.getKey(), this.wearer, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false), lootContext);
                    }).orElse(0).intValue();
                }
            }
            return fortuneLevel;
        }

        @Override
        public int getLootingLevel(DamageSource source, LivingEntity target, int baseLooting) {
            int lootingLevel = 0;
            for (Map.Entry<String, ICurioStacksHandler> entry : this.getCurios().entrySet()) {
                IDynamicStackHandler stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    int index = i;
                    lootingLevel += CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> {
                        NonNullList<Boolean> renderStates = ((ICurioStacksHandler)entry.getValue()).getRenders();
                        return curio.getLootingLevel(new SlotContext((String)entry.getKey(), this.wearer, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false), source, target, baseLooting);
                    }).orElse(0).intValue();
                }
            }
            return lootingLevel;
        }

        @Override
        public ListTag saveInventory(boolean clear) {
            return this.saveInventory(clear, (ItemStack stack) -> true);
        }

        @Override
        public ListTag saveInventory(boolean clear, Predicate<ItemStack> filter) {
            return this.saveInventory(clear, (ItemStack stack, SlotContext slotContext) -> filter.test((ItemStack)stack));
        }

        @Override
        public ListTag saveInventory(boolean clear, BiPredicate<ItemStack, SlotContext> filter) {
            ListTag taglist = new ListTag();
            for (Map.Entry<String, ICurioStacksHandler> entry : this.curios.entrySet()) {
                CompoundTag tag = new CompoundTag();
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                IDynamicStackHandler cosmetics = stacksHandler.getCosmeticStacks();
                String id = entry.getKey();
                NonNullList<Boolean> renders = stacksHandler.getRenders();
                tag.m_128365_("Stacks", (Tag)this.getFilteredStacksTag(id, false, renders, stacks, filter, clear));
                tag.m_128365_("Cosmetics", (Tag)this.getFilteredStacksTag(id, true, renders, cosmetics, filter, clear));
                tag.m_128359_("Identifier", entry.getKey());
                Map<UUID, AttributeModifier> modifiers = stacksHandler.getModifiers();
                if (!modifiers.isEmpty()) {
                    ListTag list = new ListTag();
                    modifiers.forEach((uuid, modifier) -> {
                        if (!stacksHandler.getPermanentModifiers().contains(modifier)) {
                            list.add((Object)modifier.m_22219_());
                        }
                    });
                    tag.m_128365_("Modifiers", (Tag)list);
                }
                taglist.add((Object)tag);
            }
            return taglist;
        }

        protected CompoundTag getFilteredStacksTag(String id, boolean cosmetic, NonNullList<Boolean> renders, IDynamicStackHandler stacks, BiPredicate<ItemStack, SlotContext> filter, boolean clear) {
            ListTag nbtTagList = new ListTag();
            for (int i = 0; i < stacks.getSlots(); ++i) {
                ItemStack stack = stacks.getStackInSlot(i);
                SlotContext slotContext = new SlotContext(id, this.wearer, i, cosmetic, renders.size() < i && (Boolean)renders.get(i) != false);
                if (stack.m_41619_() || !filter.test(stack, slotContext)) continue;
                CompoundTag itemTag = new CompoundTag();
                itemTag.m_128405_("Slot", i);
                stack.m_41739_(itemTag);
                nbtTagList.add((Object)itemTag);
                if (!clear) continue;
                stacks.setStackInSlot(i, ItemStack.f_41583_);
            }
            CompoundTag nbt = new CompoundTag();
            nbt.m_128365_("Items", (Tag)nbtTagList);
            nbt.m_128405_("Size", stacks.getSlots());
            return nbt;
        }

        @Override
        public void loadInventory(ListTag data) {
            if (data != null) {
                for (int i = 0; i < data.size(); ++i) {
                    int i1;
                    CompoundTag stacksData;
                    CompoundTag tag = data.m_128728_(i);
                    String identifier = tag.m_128461_("Identifier");
                    ICurioStacksHandler stacksHandler = this.curios.get(identifier);
                    ItemStackHandler loaded = new ItemStackHandler();
                    if (stacksHandler != null && tag.m_128425_("Modifiers", 9)) {
                        ListTag list = tag.m_128437_("Modifiers", 10);
                        for (int j = 0; j < list.size(); ++j) {
                            AttributeModifier attributeModifier = AttributeModifier.m_22212_((CompoundTag)list.m_128728_(j));
                            if (attributeModifier == null) continue;
                            stacksHandler.getCachedModifiers().add(attributeModifier);
                            stacksHandler.addTransientModifier(attributeModifier);
                        }
                    }
                    if (!(stacksData = tag.m_128469_("Stacks")).m_128456_()) {
                        loaded.deserializeNBT(stacksData);
                        if (stacksHandler != null) {
                            IDynamicStackHandler stacks = stacksHandler.getStacks();
                            this.loadStacks(stacksHandler, loaded, stacks);
                        } else {
                            for (i1 = 0; i1 < loaded.getSlots(); ++i1) {
                                this.loseInvalidStack(loaded.getStackInSlot(i1));
                            }
                        }
                    }
                    if ((stacksData = tag.m_128469_("Cosmetics")).m_128456_()) continue;
                    loaded.deserializeNBT(stacksData);
                    if (stacksHandler != null) {
                        IDynamicStackHandler stacks = stacksHandler.getCosmeticStacks();
                        this.loadStacks(stacksHandler, loaded, stacks);
                        continue;
                    }
                    for (i1 = 0; i1 < loaded.getSlots(); ++i1) {
                        this.loseInvalidStack(loaded.getStackInSlot(i1));
                    }
                }
            }
        }

        @Override
        public Set<ICurioStacksHandler> getUpdatingInventories() {
            return this.updates;
        }

        @Override
        public void addTransientSlotModifiers(Multimap<String, AttributeModifier> modifiers) {
            for (Map.Entry entry : modifiers.asMap().entrySet()) {
                String id = (String)entry.getKey();
                for (AttributeModifier attributeModifier : (Collection)entry.getValue()) {
                    ICurioStacksHandler stacksHandler = this.curios.get(id);
                    if (stacksHandler == null) continue;
                    stacksHandler.addTransientModifier(attributeModifier);
                }
            }
        }

        @Override
        public void addPermanentSlotModifiers(Multimap<String, AttributeModifier> modifiers) {
            for (Map.Entry entry : modifiers.asMap().entrySet()) {
                String id = (String)entry.getKey();
                for (AttributeModifier attributeModifier : (Collection)entry.getValue()) {
                    ICurioStacksHandler stacksHandler = this.curios.get(id);
                    if (stacksHandler == null) continue;
                    stacksHandler.addPermanentModifier(attributeModifier);
                }
            }
        }

        @Override
        public void removeSlotModifiers(Multimap<String, AttributeModifier> modifiers) {
            for (Map.Entry entry : modifiers.asMap().entrySet()) {
                String id = (String)entry.getKey();
                for (AttributeModifier attributeModifier : (Collection)entry.getValue()) {
                    ICurioStacksHandler stacksHandler = this.curios.get(id);
                    if (stacksHandler == null) continue;
                    stacksHandler.removeModifier(attributeModifier.m_22209_());
                }
            }
        }

        @Override
        public void addTransientSlotModifier(String slot, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
            LinkedHashMultimap map = LinkedHashMultimap.create();
            map.put((Object)slot, (Object)new AttributeModifier(uuid, name, amount, operation));
            this.addTransientSlotModifiers((Multimap<String, AttributeModifier>)map);
        }

        @Override
        public void addPermanentSlotModifier(String slot, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
            LinkedHashMultimap map = LinkedHashMultimap.create();
            map.put((Object)slot, (Object)new AttributeModifier(uuid, name, amount, operation));
            this.addPermanentSlotModifiers((Multimap<String, AttributeModifier>)map);
        }

        @Override
        public void removeSlotModifier(String slot, UUID uuid) {
            LinkedHashMultimap map = LinkedHashMultimap.create();
            map.put((Object)slot, (Object)new AttributeModifier(uuid, "", 0.0, AttributeModifier.Operation.ADDITION));
            this.removeSlotModifiers((Multimap<String, AttributeModifier>)map);
        }

        @Override
        public void clearSlotModifiers() {
            for (Map.Entry<String, ICurioStacksHandler> entry : this.curios.entrySet()) {
                entry.getValue().clearModifiers();
            }
        }

        @Override
        public void clearCachedSlotModifiers() {
            HashMultimap slots = HashMultimap.create();
            for (Map.Entry<String, ICurioStacksHandler> entry : this.curios.entrySet()) {
                ICurioStacksHandler stacksHandler = entry.getValue();
                Set<AttributeModifier> modifiers = stacksHandler.getCachedModifiers();
                if (modifiers.isEmpty()) continue;
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                String id = entry.getKey();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.m_41619_()) continue;
                    SlotContext slotContext = new SlotContext(id, this.getWearer(), i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false);
                    UUID uuid = CuriosApi.getSlotUuid(slotContext);
                    Multimap<Attribute, AttributeModifier> map = CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
                    for (Attribute attribute : map.keySet()) {
                        if (!(attribute instanceof SlotAttribute)) continue;
                        SlotAttribute wrapper = (SlotAttribute)attribute;
                        slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                    }
                }
            }
            for (Map.Entry<String, ICurioStacksHandler> entry : slots.asMap().entrySet()) {
                String id = entry.getKey();
                ICurioStacksHandler stacksHandler = this.curios.get(id);
                if (stacksHandler == null) continue;
                for (AttributeModifier attributeModifier : (Collection)((Object)entry.getValue())) {
                    stacksHandler.getCachedModifiers().remove(attributeModifier);
                }
                stacksHandler.clearCachedModifiers();
            }
        }

        @Override
        public Multimap<String, AttributeModifier> getModifiers() {
            HashMultimap result = HashMultimap.create();
            for (Map.Entry<String, ICurioStacksHandler> entry : this.curios.entrySet()) {
                result.putAll((Object)entry.getKey(), entry.getValue().getModifiers().values());
            }
            return result;
        }

        private void loadStacks(ICurioStacksHandler stacksHandler, ItemStackHandler loaded, IDynamicStackHandler stacks) {
            int index;
            for (index = 0; index < stacksHandler.getSlots() && index < loaded.getSlots(); ++index) {
                ItemStack stack = stacks.getStackInSlot(index);
                ItemStack loadedStack = loaded.getStackInSlot(index);
                if (stack.m_41619_()) {
                    stacks.setStackInSlot(index, loadedStack);
                    continue;
                }
                this.loseInvalidStack(loadedStack);
            }
            while (index < loaded.getSlots()) {
                this.loseInvalidStack(loaded.getStackInSlot(index));
                ++index;
            }
        }

        @Override
        public Tag writeTag() {
            CompoundTag compound = new CompoundTag();
            ListTag taglist = new ListTag();
            this.getCurios().forEach((key, stacksHandler) -> {
                CompoundTag tag = new CompoundTag();
                tag.m_128365_("StacksHandler", (Tag)stacksHandler.serializeNBT());
                tag.m_128359_("Identifier", key);
                taglist.add((Object)tag);
            });
            compound.m_128365_("Curios", (Tag)taglist);
            return compound;
        }

        @Override
        public void readTag(Tag nbt) {
            ListTag tagList = ((CompoundTag)nbt).m_128437_("Curios", 10);
            LivingEntity livingEntity = this.getWearer();
            if (!tagList.isEmpty()) {
                LinkedHashMap<String, ICurioStacksHandler> curios = new LinkedHashMap<String, ICurioStacksHandler>();
                TreeMap<ISlotType, ICurioStacksHandler> sortedCurios = new TreeMap<ISlotType, ICurioStacksHandler>();
                TreeSet<ISlotType> sorted = new TreeSet<ISlotType>(CuriosApi.getEntitySlots(this.wearer).values());
                for (ISlotType slotType2 : sorted) {
                    sortedCurios.put(slotType2, new CurioStacksHandler(this, slotType2.getIdentifier(), slotType2.getSize(), slotType2.useNativeGui(), slotType2.hasCosmetic(), slotType2.canToggleRendering(), slotType2.getDropRule()));
                }
                for (int i = 0; i < tagList.size(); ++i) {
                    CompoundTag tag = tagList.m_128728_(i);
                    String identifier = tag.m_128461_("Identifier");
                    CurioStacksHandler prevStacksHandler = new CurioStacksHandler(this, identifier);
                    prevStacksHandler.deserializeNBT(tag.m_128469_("StacksHandler"));
                    Optional<ISlotType> optionalType = Optional.ofNullable(CuriosApi.getEntitySlots(this.wearer).get(identifier));
                    optionalType.ifPresent(type -> {
                        int index;
                        CurioStacksHandler newStacksHandler = new CurioStacksHandler(this, type.getIdentifier(), type.getSize(), type.useNativeGui(), type.hasCosmetic(), type.canToggleRendering(), type.getDropRule());
                        newStacksHandler.copyModifiers(prevStacksHandler);
                        for (index = 0; index < newStacksHandler.getSlots() && index < prevStacksHandler.getSlots(); ++index) {
                            ItemStack prevCosmetic;
                            ItemStack prevStack = prevStacksHandler.getStacks().getStackInSlot(index);
                            if (!prevStack.m_41619_()) {
                                if (newStacksHandler.getStacks().isItemValid(index, prevStack)) {
                                    newStacksHandler.getStacks().setStackInSlot(index, prevStack);
                                } else {
                                    this.loseInvalidStack(prevStack);
                                }
                            }
                            if ((prevCosmetic = prevStacksHandler.getCosmeticStacks().getStackInSlot(index)).m_41619_()) continue;
                            if (newStacksHandler.getStacks().isItemValid(index, prevCosmetic)) {
                                newStacksHandler.getCosmeticStacks().setStackInSlot(index, prevStacksHandler.getCosmeticStacks().getStackInSlot(index));
                                continue;
                            }
                            this.loseInvalidStack(prevCosmetic);
                        }
                        while (index < prevStacksHandler.getSlots()) {
                            this.loseInvalidStack(prevStacksHandler.getStacks().getStackInSlot(index));
                            this.loseInvalidStack(prevStacksHandler.getCosmeticStacks().getStackInSlot(index));
                            ++index;
                        }
                        sortedCurios.put((ISlotType)type, newStacksHandler);
                        for (int j = 0; j < newStacksHandler.getRenders().size() && j < prevStacksHandler.getRenders().size(); ++j) {
                            newStacksHandler.getRenders().set(j, (Object)((Boolean)prevStacksHandler.getRenders().get(j)));
                        }
                    });
                    if (!optionalType.isEmpty()) continue;
                    IDynamicStackHandler stackHandler = prevStacksHandler.getStacks();
                    IDynamicStackHandler cosmeticStackHandler = prevStacksHandler.getCosmeticStacks();
                    for (int j = 0; j < stackHandler.getSlots(); ++j) {
                        ItemStack cosmeticStack;
                        ItemStack stack = stackHandler.getStackInSlot(j);
                        if (!stack.m_41619_()) {
                            this.loseInvalidStack(stack);
                        }
                        if ((cosmeticStack = cosmeticStackHandler.getStackInSlot(j)).m_41619_()) continue;
                        this.loseInvalidStack(cosmeticStack);
                    }
                }
                sortedCurios.forEach((slotType, stacksHandler) -> curios.put(slotType.getIdentifier(), (ICurioStacksHandler)stacksHandler));
                this.setCurios(curios);
            }
        }
    }
}

