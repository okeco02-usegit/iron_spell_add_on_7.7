/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  javax.annotation.Nonnull
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.PackType
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
 *  net.minecraft.util.GsonHelper
 *  net.minecraft.util.profiling.ProfilerFiller
 *  net.minecraft.world.entity.EntityType
 *  net.minecraftforge.common.crafting.CraftingHelper
 *  net.minecraftforge.common.crafting.conditions.ICondition$IContext
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.tags.ITagManager
 */
package top.theillusivec4.curios.common.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;

public class CuriosEntityManager
extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static CuriosEntityManager SERVER = new CuriosEntityManager();
    public static CuriosEntityManager CLIENT = new CuriosEntityManager();
    private Map<EntityType<?>, Map<String, ISlotType>> entitySlots = ImmutableMap.of();
    private Map<String, Set<String>> idToMods = ImmutableMap.of();
    private ICondition.IContext ctx = ICondition.IContext.EMPTY;

    public CuriosEntityManager() {
        super(GSON, "curios/entities");
    }

    public CuriosEntityManager(ICondition.IContext ctx) {
        super(GSON, "curios/entities");
        this.ctx = ctx;
    }

    protected void apply(Map<ResourceLocation, JsonElement> pObject, @Nonnull ResourceManager pResourceManager, @Nonnull ProfilerFiller pProfiler) {
        HashMap<Object, ImmutableMap.Builder> map = new HashMap<Object, ImmutableMap.Builder>();
        HashMap<String, ImmutableSet.Builder> modMap = new HashMap<String, ImmutableSet.Builder>();
        LinkedHashMap sorted = new LinkedHashMap();
        pResourceManager.m_7536_().forEach(packResources -> {
            Set namespaces = packResources.m_5698_(PackType.SERVER_DATA);
            namespaces.forEach(namespace -> packResources.m_8031_(PackType.SERVER_DATA, namespace, "curios/entities", (resourceLocation, inputStreamIoSupplier) -> {
                String path = resourceLocation.m_135815_();
                ResourceLocation rl = new ResourceLocation(namespace, path.substring("curios/entities/".length(), path.length() - ".json".length()));
                JsonElement el = (JsonElement)pObject.get(rl);
                if (el != null) {
                    sorted.put(rl, el);
                }
            }));
        });
        for (String string : LegacySlotManager.getImcBuilders().keySet()) {
            ImmutableMap.Builder builder = map.computeIfAbsent(EntityType.f_20532_, k -> ImmutableMap.builder());
            CuriosSlotManager.SERVER.getSlot(string).ifPresentOrElse(slot -> builder.put((Object)s, slot), () -> Curios.LOGGER.error("{} is not a registered slot type!", (Object)s));
        }
        for (Map.Entry entry2 : sorted.entrySet()) {
            ResourceLocation resourcelocation = (ResourceLocation)entry2.getKey();
            if (resourcelocation.m_135815_().startsWith("_")) continue;
            try {
                JsonObject jsonObject = GsonHelper.m_13918_((JsonElement)((JsonElement)entry2.getValue()), (String)"top element");
                for (Map.Entry<EntityType<?>, Map<String, ISlotType>> entry1 : CuriosEntityManager.getSlotsForEntities(jsonObject, resourcelocation, this.ctx).entrySet()) {
                    if (GsonHelper.m_13855_((JsonObject)jsonObject, (String)"replace", (boolean)false)) {
                        ImmutableMap.Builder builder = ImmutableMap.builder();
                        builder.putAll(entry1.getValue());
                        map.put(entry1.getKey(), builder);
                    } else {
                        map.computeIfAbsent(entry1.getKey(), k -> ImmutableMap.builder()).putAll(entry1.getValue());
                    }
                    modMap.computeIfAbsent(resourcelocation.m_135815_(), k -> ImmutableSet.builder()).add((Object)resourcelocation.m_135827_());
                }
            }
            catch (JsonParseException | IllegalArgumentException e) {
                Curios.LOGGER.error("Parsing error loading curio entity {}", (Object)resourcelocation, (Object)e);
            }
        }
        HashMap configSlots = new HashMap();
        for (String configSlot : CuriosSlotManager.SERVER.getConfigSlots()) {
            CuriosSlotManager.SERVER.getSlot(configSlot).ifPresentOrElse(slot -> configSlots.put(configSlot, slot), () -> Curios.LOGGER.error("{} is not a registered slot type!", (Object)configSlot));
        }
        map.computeIfAbsent(EntityType.f_20532_, k -> ImmutableMap.builder()).putAll(configSlots);
        this.entitySlots = (Map)map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((ImmutableMap.Builder)entry.getValue()).buildKeepingLast()));
        this.idToMods = (Map)modMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((ImmutableSet.Builder)entry.getValue()).build()));
        Curios.LOGGER.info("Loaded {} curio entities", (Object)map.size());
    }

    public static ListTag getSyncPacket() {
        ListTag tag = new ListTag();
        for (Map.Entry<EntityType<?>, Map<String, ISlotType>> entry : CuriosEntityManager.SERVER.entitySlots.entrySet()) {
            ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entry.getKey());
            if (rl == null) continue;
            CompoundTag entity = new CompoundTag();
            entity.m_128359_("Entity", rl.toString());
            ListTag tag1 = new ListTag();
            for (Map.Entry<String, ISlotType> val : entry.getValue().entrySet()) {
                tag1.add((Object)StringTag.m_129297_((String)val.getKey()));
            }
            entity.m_128365_("Slots", (Tag)tag1);
            tag.add((Object)entity);
        }
        return tag;
    }

    public static void applySyncPacket(ListTag tag) {
        HashMap map = new HashMap();
        for (Tag tag1 : tag) {
            CompoundTag entity;
            EntityType type;
            if (!(tag1 instanceof CompoundTag) || (type = (EntityType)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation((entity = (CompoundTag)tag1).m_128461_("Entity")))) == null) continue;
            ListTag slots = entity.m_128437_("Slots", 8);
            for (Tag slot : slots) {
                if (!(slot instanceof StringTag)) continue;
                StringTag stringTag = (StringTag)slot;
                String id = stringTag.m_7916_();
                CuriosSlotManager.CLIENT.getSlot(id).ifPresent(slotType -> map.computeIfAbsent(type, k -> ImmutableMap.builder()).put((Object)id, slotType));
            }
        }
        CuriosEntityManager.CLIENT.entitySlots = (Map)map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((ImmutableMap.Builder)entry.getValue()).build()));
    }

    private static Map<EntityType<?>, Map<String, ISlotType>> getSlotsForEntities(JsonObject jsonObject, ResourceLocation resourceLocation, ICondition.IContext ctx) {
        HashMap map = new HashMap();
        if (!CraftingHelper.processConditions((JsonArray)GsonHelper.m_13832_((JsonObject)jsonObject, (String)"conditions", (JsonArray)new JsonArray()), (ICondition.IContext)ctx)) {
            Curios.LOGGER.debug("Skipping loading entity file {} as its conditions were not met", (Object)resourceLocation);
            return map;
        }
        JsonArray jsonEntities = GsonHelper.m_13832_((JsonObject)jsonObject, (String)"entities", (JsonArray)new JsonArray());
        ITagManager tagManager = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags());
        HashSet<EntityType> toAdd = new HashSet<EntityType>();
        for (JsonElement jsonEntity : jsonEntities) {
            String entity = jsonEntity.getAsString();
            if (entity.startsWith("#")) {
                for (EntityType entityType : tagManager.getTag(tagManager.createTagKey(new ResourceLocation(entity)))) {
                    toAdd.add(entityType);
                }
                continue;
            }
            EntityType type = (EntityType)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entity));
            if (type != null) {
                toAdd.add(type);
                continue;
            }
            Curios.LOGGER.error("{} is not a registered entity type!", (Object)entity);
        }
        JsonArray jsonSlots = GsonHelper.m_13832_((JsonObject)jsonObject, (String)"slots", (JsonArray)new JsonArray());
        HashMap slots = new HashMap();
        for (JsonElement jsonSlot : jsonSlots) {
            String id = jsonSlot.getAsString();
            CuriosSlotManager.SERVER.getSlot(id).ifPresentOrElse(slot -> slots.put(id, slot), () -> Curios.LOGGER.error("{} is not a registered slot type!", (Object)id));
        }
        for (EntityType entityType : toAdd) {
            map.computeIfAbsent(entityType, k -> new HashMap()).putAll(slots);
        }
        return map;
    }

    public Map<String, ISlotType> getEntitySlots(EntityType<?> type) {
        if (this.entitySlots.containsKey(type)) {
            return this.entitySlots.get(type);
        }
        return ImmutableMap.of();
    }

    public Map<String, Set<String>> getModsFromSlots() {
        return ImmutableMap.copyOf(this.idToMods);
    }
}

