/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.world.entity.EntityType
 *  net.minecraftforge.common.crafting.CraftingHelper
 *  net.minecraftforge.common.crafting.conditions.ICondition
 *  net.minecraftforge.registries.ForgeRegistries
 */
package top.theillusivec4.curios.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.type.data.IEntitiesData;

public class EntitiesData
implements IEntitiesData {
    private final Set<EntityType<?>> entities = new HashSet();
    private final Set<String> slots = new HashSet<String>();
    private Boolean replace;
    private List<ICondition> conditions;

    @Override
    public EntitiesData replace(boolean replace) {
        this.replace = replace;
        return this;
    }

    @Override
    public EntitiesData addPlayer() {
        return this.addEntities(new EntityType[]{EntityType.f_20532_});
    }

    @Override
    public EntitiesData addEntities(EntityType<?> ... entityTypes) {
        this.entities.addAll(Arrays.stream(entityTypes).toList());
        return this;
    }

    @Override
    public EntitiesData addSlots(String ... slots) {
        this.slots.addAll(Arrays.stream(slots).toList());
        return this;
    }

    @Override
    public EntitiesData addCondition(ICondition condition) {
        if (this.conditions == null) {
            this.conditions = new ArrayList<ICondition>();
        }
        this.conditions.add(condition);
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonArray arr;
        JsonObject jsonObject = new JsonObject();
        if (this.replace != null) {
            jsonObject.addProperty("replace", this.replace);
        }
        if (!this.entities.isEmpty()) {
            arr = new JsonArray();
            this.entities.forEach(entityType -> arr.add(Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entityType)).toString()));
            jsonObject.add("entities", (JsonElement)arr);
        }
        if (!this.slots.isEmpty()) {
            arr = new JsonArray();
            this.slots.forEach(arg_0 -> ((JsonArray)arr).add(arg_0));
            jsonObject.add("slots", (JsonElement)arr);
        }
        if (this.conditions != null) {
            jsonObject.add("conditions", (JsonElement)CraftingHelper.serialize((ICondition[])((ICondition[])this.conditions.toArray(ICondition[]::new))));
        }
        return jsonObject;
    }
}

