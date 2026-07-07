/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  javax.annotation.Nullable
 *  net.minecraft.advancements.critereon.MinMaxBounds$Ints
 *  net.minecraft.util.GsonHelper
 */
package top.theillusivec4.curios.api;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.GsonHelper;
import top.theillusivec4.curios.api.SlotContext;

public class SlotPredicate {
    public static final SlotPredicate ANY = new SlotPredicate();
    private final Set<String> identifiers;
    private final MinMaxBounds.Ints indices;

    public SlotPredicate() {
        this.identifiers = new HashSet<String>();
        this.indices = MinMaxBounds.Ints.f_55364_;
    }

    public SlotPredicate(Set<String> identifiers, MinMaxBounds.Ints indices) {
        this.identifiers = identifiers;
        this.indices = indices;
    }

    public boolean matches(SlotContext slotContext) {
        if (this == ANY) {
            return true;
        }
        if (!this.identifiers.contains(slotContext.identifier())) {
            return false;
        }
        return this.indices.m_55390_(slotContext.index());
    }

    public static SlotPredicate fromJson(@Nullable JsonElement pJson) {
        if (pJson != null && !pJson.isJsonNull()) {
            JsonObject jsonobject = GsonHelper.m_13918_((JsonElement)pJson, (String)"curios:slot");
            MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.m_55373_((JsonElement)jsonobject.get("index"));
            JsonArray jsonarray = GsonHelper.m_13832_((JsonObject)jsonobject, (String)"slots", (JsonArray)new JsonArray());
            ImmutableSet.Builder builder = ImmutableSet.builder();
            for (JsonElement jsonelement : jsonarray) {
                builder.add((Object)jsonelement.getAsString());
            }
            ImmutableSet set = builder.build();
            return new SlotPredicate((Set<String>)set, minmaxbounds$ints);
        }
        return ANY;
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        }
        JsonObject jsonobject = new JsonObject();
        JsonArray jsonarray = new JsonArray();
        for (String id : this.identifiers) {
            jsonarray.add(id);
        }
        jsonobject.add("slots", (JsonElement)jsonarray);
        jsonobject.add("index", this.indices.m_55328_());
        return jsonobject;
    }

    public static class Builder {
        private Set<String> identifiers = new HashSet<String>();
        private MinMaxBounds.Ints indices = MinMaxBounds.Ints.f_55364_;

        private Builder() {
        }

        public static Builder slot() {
            return new Builder();
        }

        public Builder of(String ... identifiers) {
            this.identifiers = (Set)Stream.of(identifiers).collect(ImmutableSet.toImmutableSet());
            return this;
        }

        public Builder withIndex(MinMaxBounds.Ints index) {
            this.indices = index;
            return this;
        }

        public SlotPredicate build() {
            return new SlotPredicate(this.identifiers, this.indices);
        }
    }
}

