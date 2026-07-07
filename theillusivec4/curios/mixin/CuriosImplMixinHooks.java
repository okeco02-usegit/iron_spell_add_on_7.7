/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.LinkedHashMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.fml.loading.FMLLoader
 *  net.minecraftforge.network.PacketDistributor
 *  net.minecraftforge.registries.ForgeRegistries
 */
package top.theillusivec4.curios.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketBreak;

public class CuriosImplMixinHooks {
    private static final Map<Item, ICurioItem> REGISTRY = new ConcurrentHashMap<Item, ICurioItem>();
    private static final Map<String, UUID> UUIDS = new HashMap<String, UUID>();
    private static final Map<ResourceLocation, Predicate<SlotResult>> SLOT_RESULT_PREDICATES = new HashMap<ResourceLocation, Predicate<SlotResult>>();

    public static void registerCurio(Item item, ICurioItem icurio) {
        REGISTRY.put(item, icurio);
    }

    public static Optional<ICurioItem> getCurioFromRegistry(Item item) {
        return Optional.ofNullable(REGISTRY.get(item));
    }

    public static Map<String, ISlotType> getSlots(boolean isClient) {
        CuriosSlotManager slotManager = isClient ? CuriosSlotManager.CLIENT : CuriosSlotManager.SERVER;
        return slotManager.getSlots();
    }

    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type, boolean isClient) {
        CuriosEntityManager entityManager = isClient ? CuriosEntityManager.CLIENT : CuriosEntityManager.SERVER;
        return entityManager.getEntitySlots(type);
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, boolean isClient) {
        return CuriosImplMixinHooks.filteredSlots(slotType -> {
            SlotContext slotContext = new SlotContext(slotType.getIdentifier(), null, 0, false, true);
            SlotResult slotResult = new SlotResult(slotContext, stack);
            return CuriosApi.testCurioPredicates(slotType.getValidators(), slotResult);
        }, CuriosApi.getSlots(isClient));
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, LivingEntity livingEntity) {
        return CuriosImplMixinHooks.filteredSlots(slotType -> {
            SlotContext slotContext = new SlotContext(slotType.getIdentifier(), livingEntity, 0, false, true);
            SlotResult slotResult = new SlotResult(slotContext, stack);
            return CuriosApi.testCurioPredicates(slotType.getValidators(), slotResult);
        }, CuriosApi.getEntitySlots(livingEntity));
    }

    private static Map<String, ISlotType> filteredSlots(Predicate<ISlotType> filter, Map<String, ISlotType> map) {
        HashMap<String, ISlotType> result = new HashMap<String, ISlotType>();
        for (Map.Entry<String, ISlotType> entry : map.entrySet()) {
            ISlotType slotType = entry.getValue();
            if (!filter.test(slotType)) continue;
            result.put(entry.getKey(), slotType);
        }
        return result;
    }

    public static LazyOptional<ICurio> getCurio(ItemStack stack) {
        return stack.getCapability(CuriosCapability.ITEM);
    }

    public static LazyOptional<ICuriosItemHandler> getCuriosInventory(LivingEntity livingEntity) {
        if (livingEntity != null) {
            return livingEntity.getCapability(CuriosCapability.INVENTORY);
        }
        return LazyOptional.empty();
    }

    public static boolean isStackValid(SlotContext slotContext, ItemStack stack) {
        String id = slotContext.identifier();
        LivingEntity entity = slotContext.entity();
        Map<String, ISlotType> map = entity != null ? CuriosImplMixinHooks.getItemStackSlots(stack, entity) : CuriosImplMixinHooks.getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT);
        Set<String> slots = map.keySet();
        if (!slots.isEmpty()) {
            return id.equals("curio") || slots.contains(id) || slots.contains("curio");
        }
        if (id.equals("curio")) {
            if (stack.m_204131_().anyMatch(tagKey -> tagKey.f_203868_().m_135827_().equals("curios"))) {
                return true;
            }
            Map<String, ISlotType> allSlots = CuriosApi.getSlots(false);
            SlotResult slotResult = new SlotResult(slotContext, stack);
            for (Map.Entry<String, ISlotType> entry : allSlots.entrySet()) {
                ISlotType slotType = entry.getValue();
                for (ResourceLocation validator : slotType.getValidators()) {
                    if (!CuriosApi.getCurioPredicate(validator).map(val -> val.test(slotResult)).orElse(false).booleanValue()) continue;
                    return true;
                }
            }
            return CuriosApi.getCurio(stack).isPresent();
        }
        return false;
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        HashMultimap multimap = HashMultimap.create();
        if (stack.m_41783_() != null && stack.m_41783_().m_128425_("CurioAttributeModifiers", 9)) {
            ListTag listnbt = stack.m_41783_().m_128437_("CurioAttributeModifiers", 10);
            String identifier = slotContext.identifier();
            for (int i = 0; i < listnbt.size(); ++i) {
                CompoundTag compoundnbt = listnbt.m_128728_(i);
                if (!compoundnbt.m_128461_("Slot").equals(identifier)) continue;
                ResourceLocation rl = ResourceLocation.m_135820_((String)compoundnbt.m_128461_("AttributeName"));
                UUID id = uuid;
                if (rl == null) continue;
                if (compoundnbt.m_128441_("UUID")) {
                    id = compoundnbt.m_128342_("UUID");
                }
                if (id.getLeastSignificantBits() == 0L || id.getMostSignificantBits() == 0L) continue;
                AttributeModifier.Operation operation = AttributeModifier.Operation.m_22236_((int)compoundnbt.m_128451_("Operation"));
                double amount = compoundnbt.m_128459_("Amount");
                String name = compoundnbt.m_128461_("Name");
                if (rl.m_135827_().equals("curios")) {
                    boolean clientSide;
                    String identifier1 = rl.m_135815_();
                    LivingEntity livingEntity = slotContext.entity();
                    boolean bl = clientSide = livingEntity == null || livingEntity.m_9236_().m_5776_();
                    if (!CuriosApi.getSlot(identifier1, clientSide).isPresent()) continue;
                    CuriosApi.addSlotModifier((Multimap<Attribute, AttributeModifier>)multimap, identifier1, id, amount, operation);
                    continue;
                }
                Attribute attribute = (Attribute)ForgeRegistries.ATTRIBUTES.getValue(rl);
                if (attribute == null) continue;
                multimap.put((Object)attribute, (Object)new AttributeModifier(id, name, amount, operation));
            }
        } else {
            multimap = (Multimap)CuriosImplMixinHooks.getCurio(stack).map(curio -> curio.getAttributeModifiers(slotContext, uuid)).orElse(multimap);
        }
        CurioAttributeModifierEvent evt = new CurioAttributeModifierEvent(stack, slotContext, uuid, (Multimap<Attribute, AttributeModifier>)multimap);
        MinecraftForge.EVENT_BUS.post((Event)evt);
        return LinkedHashMultimap.create(evt.getModifiers());
    }

    public static void addSlotModifier(Multimap<Attribute, AttributeModifier> map, String identifier, UUID uuid, double amount, AttributeModifier.Operation operation) {
        map.put((Object)SlotAttribute.getOrCreate(identifier), (Object)new AttributeModifier(uuid, identifier, amount, operation));
    }

    public static void addSlotModifier(ItemStack stack, String identifier, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        CuriosImplMixinHooks.addModifier(stack, SlotAttribute.getOrCreate(identifier), name, uuid, amount, operation, slot);
    }

    public static void addModifier(ItemStack stack, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        CompoundTag tag = stack.m_41784_();
        if (!tag.m_128425_("CurioAttributeModifiers", 9)) {
            tag.m_128365_("CurioAttributeModifiers", (Tag)new ListTag());
        }
        ListTag listtag = tag.m_128437_("CurioAttributeModifiers", 10);
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.m_128359_("Name", name);
        compoundtag.m_128347_("Amount", amount);
        compoundtag.m_128405_("Operation", operation.m_22235_());
        if (uuid != null) {
            compoundtag.m_128362_("UUID", uuid);
        }
        Object id = "";
        if (attribute instanceof SlotAttribute) {
            SlotAttribute wrapper = (SlotAttribute)attribute;
            id = "curios:" + wrapper.getIdentifier();
        } else {
            ResourceLocation rl = ForgeRegistries.ATTRIBUTES.getKey((Object)attribute);
            if (rl != null) {
                id = rl.toString();
            }
        }
        if (!((String)id).isEmpty()) {
            compoundtag.m_128359_("AttributeName", (String)id);
        }
        compoundtag.m_128359_("Slot", slot);
        listtag.add((Object)compoundtag);
    }

    public static void broadcastCurioBreakEvent(SlotContext slotContext) {
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(slotContext::entity), (Object)new SPacketBreak(slotContext.entity().m_19879_(), slotContext.identifier(), slotContext.index()));
    }

    public static UUID getUuid(SlotContext slotContext) {
        String key = slotContext.identifier() + slotContext.index();
        return UUIDS.computeIfAbsent(key, k -> UUID.nameUUIDFromBytes(k.getBytes()));
    }

    public static void registerCurioPredicate(ResourceLocation resourceLocation, Predicate<SlotResult> validator) {
        SLOT_RESULT_PREDICATES.putIfAbsent(resourceLocation, validator);
    }

    public static Optional<Predicate<SlotResult>> getCurioPredicate(ResourceLocation resourceLocation) {
        return Optional.ofNullable(SLOT_RESULT_PREDICATES.get(resourceLocation));
    }

    public static Map<ResourceLocation, Predicate<SlotResult>> getCurioPredicates() {
        return ImmutableMap.copyOf(SLOT_RESULT_PREDICATES);
    }

    public static boolean testCurioPredicates(Set<ResourceLocation> predicates, SlotResult slotResult) {
        for (ResourceLocation id : predicates) {
            if (!CuriosApi.getCurioPredicate(id).map(slotResultPredicate -> slotResultPredicate.test(slotResult)).orElse(false).booleanValue()) continue;
            return true;
        }
        return false;
    }

    static {
        CuriosImplMixinHooks.registerCurioPredicate(new ResourceLocation("curios", "all"), slotResult -> true);
        CuriosImplMixinHooks.registerCurioPredicate(new ResourceLocation("curios", "none"), slotResult -> false);
        CuriosImplMixinHooks.registerCurioPredicate(new ResourceLocation("curios", "tag"), slotResult -> {
            String id = slotResult.slotContext().identifier();
            TagKey tag1 = ItemTags.create((ResourceLocation)new ResourceLocation("curios", id));
            TagKey tag2 = ItemTags.create((ResourceLocation)new ResourceLocation("curios", "curio"));
            ItemStack stack = slotResult.stack();
            return stack.m_204117_(tag1) || stack.m_204117_(tag2);
        });
    }
}

