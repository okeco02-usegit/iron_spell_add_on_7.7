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
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.PackType
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
 *  net.minecraft.util.GsonHelper
 *  net.minecraft.util.profiling.ProfilerFiller
 *  net.minecraftforge.common.crafting.CraftingHelper
 *  net.minecraftforge.common.crafting.conditions.ICondition$IContext
 *  org.apache.commons.lang3.EnumUtils
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.apache.commons.lang3.EnumUtils;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;
import top.theillusivec4.curios.common.slottype.SlotType;

public class CuriosSlotManager
extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static CuriosSlotManager SERVER = new CuriosSlotManager();
    public static CuriosSlotManager CLIENT = new CuriosSlotManager();
    private Map<String, ISlotType> slots = ImmutableMap.of();
    private Set<String> configSlots = ImmutableSet.of();
    private Map<String, ResourceLocation> icons = ImmutableMap.of();
    private Map<String, Set<String>> idToMods = ImmutableMap.of();
    private ICondition.IContext ctx = ICondition.IContext.EMPTY;

    public CuriosSlotManager() {
        super(GSON, "curios/slots");
    }

    public CuriosSlotManager(ICondition.IContext ctx) {
        super(GSON, "curios/slots");
        this.ctx = ctx;
    }

    protected void apply(@Nonnull Map<ResourceLocation, JsonElement> pObject, @Nonnull ResourceManager pResourceManager, @Nonnull ProfilerFiller pProfiler) {
        String id;
        ResourceLocation resourcelocation;
        HashMap<String, SlotType.Builder> map = new HashMap<String, SlotType.Builder>();
        HashMap<String, ImmutableSet.Builder> modMap = new HashMap<String, ImmutableSet.Builder>();
        LinkedHashMap sorted = new LinkedHashMap();
        pResourceManager.m_7536_().forEach(packResources -> {
            Set namespaces = packResources.m_5698_(PackType.SERVER_DATA);
            namespaces.forEach(namespace -> packResources.m_8031_(PackType.SERVER_DATA, namespace, "curios/slots", (resourceLocation, inputStreamIoSupplier) -> {
                String path = resourceLocation.m_135815_();
                ResourceLocation rl = new ResourceLocation(namespace, path.substring("curios/slots/".length(), path.length() - ".json".length()));
                JsonElement el = (JsonElement)pObject.get(rl);
                if (el != null) {
                    sorted.put(rl, el);
                }
            }));
        });
        for (Map.Entry entry2 : sorted.entrySet()) {
            resourcelocation = (ResourceLocation)entry2.getKey();
            if (!resourcelocation.m_135827_().equals("curios")) continue;
            try {
                id = resourcelocation.m_135815_();
                if (!CraftingHelper.processConditions((JsonArray)GsonHelper.m_13832_((JsonObject)((JsonElement)entry2.getValue()).getAsJsonObject(), (String)"conditions", (JsonArray)new JsonArray()), (ICondition.IContext)this.ctx)) {
                    Curios.LOGGER.debug("Skipping loading slot {} as its conditions were not met", (Object)resourcelocation);
                    continue;
                }
                CuriosSlotManager.fromJson(map.computeIfAbsent(id, k -> new SlotType.Builder(id)), GsonHelper.m_13918_((JsonElement)((JsonElement)entry2.getValue()), (String)"top element"));
                modMap.computeIfAbsent(id, k -> ImmutableSet.builder()).add((Object)resourcelocation.m_135827_());
            }
            catch (JsonParseException | IllegalArgumentException e) {
                Curios.LOGGER.error("Parsing error loading curio slot {}", (Object)resourcelocation, (Object)e);
            }
        }
        for (Map.Entry<Object, Object> entry3 : LegacySlotManager.getImcBuilders().entrySet()) {
            SlotType.Builder builder = map.computeIfAbsent((String)entry3.getKey(), k -> new SlotType.Builder((String)entry2.getKey()));
            builder.apply((SlotType.Builder)entry3.getValue());
        }
        for (Map.Entry<Object, Object> entry4 : LegacySlotManager.getIdsToMods().entrySet()) {
            modMap.computeIfAbsent((String)entry4.getKey(), k -> ImmutableSet.builder()).addAll((Iterable)entry4.getValue());
        }
        for (Map.Entry<Object, Object> entry5 : sorted.entrySet()) {
            resourcelocation = (ResourceLocation)entry5.getKey();
            if (resourcelocation.m_135815_().startsWith("_") || resourcelocation.m_135827_().equals("curios")) continue;
            try {
                id = resourcelocation.m_135815_();
                if (!CraftingHelper.processConditions((JsonArray)GsonHelper.m_13832_((JsonObject)((JsonElement)entry5.getValue()).getAsJsonObject(), (String)"conditions", (JsonArray)new JsonArray()), (ICondition.IContext)this.ctx)) {
                    Curios.LOGGER.debug("Skipping loading slot {} as its conditions were not met", (Object)resourcelocation);
                    continue;
                }
                CuriosSlotManager.fromJson(map.computeIfAbsent(id, k -> new SlotType.Builder(id)), GsonHelper.m_13918_((JsonElement)((JsonElement)entry5.getValue()), (String)"top element"));
                modMap.computeIfAbsent(id, k -> ImmutableSet.builder()).add((Object)resourcelocation.m_135827_());
            }
            catch (JsonParseException | IllegalArgumentException e) {
                Curios.LOGGER.error("Parsing error loading curio slot {}", (Object)resourcelocation, (Object)e);
            }
        }
        try {
            Set<String> configs = CuriosSlotManager.fromConfig(map);
            this.configSlots = ImmutableSet.copyOf(configs);
            for (String id2 : configs) {
                modMap.computeIfAbsent(id2, k -> ImmutableSet.builder()).add((Object)"config");
            }
        }
        catch (IllegalArgumentException e) {
            Curios.LOGGER.error("Config parsing error", (Throwable)e);
        }
        this.slots = (Map)map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((SlotType.Builder)entry.getValue()).build()));
        this.idToMods = (Map)modMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((ImmutableSet.Builder)entry.getValue()).build()));
        Curios.LOGGER.info("Loaded {} curio slots", (Object)map.size());
    }

    public Map<String, ISlotType> getSlots() {
        return this.slots;
    }

    public Optional<ISlotType> getSlot(String id) {
        return Optional.ofNullable(this.slots.get(id));
    }

    public static ListTag getSyncPacket() {
        ListTag tag = new ListTag();
        for (Map.Entry<String, ISlotType> entry : CuriosSlotManager.SERVER.slots.entrySet()) {
            tag.add((Object)entry.getValue().writeNbt());
        }
        return tag;
    }

    public static void applySyncPacket(ListTag tag) {
        ImmutableMap.Builder map = ImmutableMap.builder();
        for (Tag tag1 : tag) {
            if (!(tag1 instanceof CompoundTag)) continue;
            CompoundTag slotType = (CompoundTag)tag1;
            ISlotType type = SlotType.from(slotType);
            map.put((Object)type.getIdentifier(), (Object)type);
        }
        CuriosSlotManager.CLIENT.slots = map.build();
    }

    public void setIcons(Map<String, ResourceLocation> icons) {
        this.icons = ImmutableMap.copyOf(icons);
    }

    public Set<String> getConfigSlots() {
        return this.configSlots;
    }

    public Map<String, ResourceLocation> getIcons() {
        return this.icons;
    }

    public ResourceLocation getIcon(String identifier) {
        return this.icons.getOrDefault(identifier, new ResourceLocation("curios", "slot/empty_curio_slot"));
    }

    public Map<String, Set<String>> getModsFromSlots() {
        return this.idToMods;
    }

    public static Set<String> fromConfig(Map<String, SlotType.Builder> map) throws IllegalArgumentException {
        ArrayList parsed = new ArrayList();
        List list = (List)CuriosConfig.COMMON.slots.get();
        HashSet<String> results = new HashSet<String>();
        for (String string : list) {
            StringTokenizer tokenizer = new StringTokenizer(string, ";");
            HashMap<String, String> subMap = new HashMap<String, String>();
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                String[] keyValue = token.split("=");
                subMap.put(keyValue[0], keyValue[1]);
            }
            if (subMap.containsKey("id")) {
                parsed.add(subMap);
                continue;
            }
            throw new IllegalArgumentException("Cannot load config entry " + string + " due to missing id field");
        }
        for (Map map2 : parsed) {
            Boolean nativeGui;
            Integer size;
            String id = (String)map2.get("id");
            SlotType.Builder builder = map.computeIfAbsent(id, k -> new SlotType.Builder(id));
            Integer n = size = map2.containsKey("size") ? Integer.valueOf(Integer.parseInt((String)map2.get("size"))) : null;
            if (size != null && size < 0) {
                throw new IllegalArgumentException("Size cannot be less than 0!");
            }
            String operation = map2.getOrDefault("operation", "SET");
            if (!(operation.equals("SET") || operation.equals("ADD") || operation.equals("REMOVE"))) {
                throw new IllegalArgumentException(operation + " is not a valid operation!");
            }
            String dropRule = map2.getOrDefault("drop_rule", "");
            if (!dropRule.isEmpty() && !EnumUtils.isValidEnum(ICurio.DropRule.class, (String)dropRule)) {
                throw new IllegalArgumentException(dropRule + " is not a valid drop rule!");
            }
            results.add(id);
            boolean replace = true;
            Integer order = map2.containsKey("order") ? Integer.valueOf(Integer.parseInt((String)map2.get("order"))) : null;
            String icon = map2.getOrDefault("icon", "");
            Boolean toggle = map2.containsKey("render_toggle") ? Boolean.valueOf(Boolean.parseBoolean((String)map2.get("render_toggle"))) : null;
            Boolean cosmetic = map2.containsKey("add_cosmetic") ? Boolean.valueOf(Boolean.parseBoolean((String)map2.get("add_cosmetic"))) : null;
            Boolean bl = nativeGui = map2.containsKey("use_native_gui") ? Boolean.valueOf(Boolean.parseBoolean((String)map2.get("use_native_gui"))) : null;
            if (order != null) {
                builder.order(order, replace);
            }
            if (!icon.isEmpty()) {
                builder.icon(new ResourceLocation(icon));
            }
            if (!dropRule.isEmpty()) {
                builder.dropRule(dropRule);
            }
            if (size != null) {
                builder.size(size, operation, replace);
            }
            if (cosmetic != null) {
                builder.hasCosmetic(cosmetic, replace);
            }
            if (nativeGui != null) {
                builder.useNativeGui(nativeGui, replace);
            }
            if (toggle == null) continue;
            builder.renderToggle(toggle, replace);
        }
        return results;
    }

    public static void fromJson(SlotType.Builder builder, JsonObject jsonObject) throws IllegalArgumentException, JsonParseException {
        JsonArray jsonSlotResultPredicate;
        Integer jsonSize;
        Integer n = jsonSize = jsonObject.has("size") ? Integer.valueOf(GsonHelper.m_13927_((JsonObject)jsonObject, (String)"size")) : null;
        if (jsonSize != null && jsonSize < 0) {
            throw new IllegalArgumentException("Size cannot be less than 0!");
        }
        String operation = GsonHelper.m_13851_((JsonObject)jsonObject, (String)"operation", (String)"SET");
        if (!(operation.equals("SET") || operation.equals("ADD") || operation.equals("REMOVE"))) {
            throw new IllegalArgumentException(operation + " is not a valid operation!");
        }
        String jsonDropRule = GsonHelper.m_13851_((JsonObject)jsonObject, (String)"drop_rule", (String)"");
        if (!jsonDropRule.isEmpty() && !EnumUtils.isValidEnum(ICurio.DropRule.class, (String)jsonDropRule)) {
            throw new IllegalArgumentException(jsonDropRule + " is not a valid drop rule!");
        }
        boolean replace = GsonHelper.m_13855_((JsonObject)jsonObject, (String)"replace", (boolean)false);
        Integer jsonOrder = jsonObject.has("order") ? Integer.valueOf(GsonHelper.m_13927_((JsonObject)jsonObject, (String)"order")) : null;
        String jsonIcon = GsonHelper.m_13851_((JsonObject)jsonObject, (String)"icon", (String)"");
        Boolean jsonToggle = jsonObject.has("render_toggle") ? Boolean.valueOf(GsonHelper.m_13912_((JsonObject)jsonObject, (String)"render_toggle")) : null;
        Boolean jsonCosmetic = jsonObject.has("add_cosmetic") ? Boolean.valueOf(GsonHelper.m_13912_((JsonObject)jsonObject, (String)"add_cosmetic")) : null;
        Boolean jsonNative = jsonObject.has("use_native_gui") ? Boolean.valueOf(GsonHelper.m_13912_((JsonObject)jsonObject, (String)"use_native_gui")) : null;
        JsonArray jsonArray = jsonSlotResultPredicate = jsonObject.has("validators") ? GsonHelper.m_13933_((JsonObject)jsonObject, (String)"validators") : null;
        if (jsonOrder != null) {
            builder.order(jsonOrder, replace);
        }
        if (!jsonIcon.isEmpty()) {
            builder.icon(new ResourceLocation(jsonIcon));
        }
        if (!jsonDropRule.isEmpty()) {
            builder.dropRule(jsonDropRule);
        }
        if (jsonSize != null) {
            builder.size(jsonSize, operation, replace);
        }
        if (jsonCosmetic != null) {
            builder.hasCosmetic(jsonCosmetic, replace);
        }
        if (jsonNative != null) {
            builder.useNativeGui(jsonNative, replace);
        }
        if (jsonToggle != null) {
            builder.renderToggle(jsonToggle, replace);
        }
        if (jsonSlotResultPredicate != null) {
            for (JsonElement jsonElement : jsonSlotResultPredicate) {
                builder.validator(new ResourceLocation(jsonElement.getAsString()));
            }
        }
    }
}

