/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSyntaxException
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.Util
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.GsonHelper
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.Serializer
 *  net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction
 *  net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction$Serializer
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParam
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 *  net.minecraft.world.level.storage.loot.providers.number.NumberProvider
 *  net.minecraftforge.registries.ForgeRegistries
 */
package top.theillusivec4.curios.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;

public class SetCurioAttributesFunction
extends LootItemConditionalFunction {
    public static LootItemFunctionType TYPE = null;
    final List<Modifier> modifiers;

    SetCurioAttributesFunction(LootItemCondition[] conditions, List<Modifier> modifiers) {
        super(conditions);
        this.modifiers = ImmutableList.copyOf(modifiers);
    }

    public static void register() {
        TYPE = (LootItemFunctionType)Registry.m_122965_((Registry)BuiltInRegistries.f_256753_, (ResourceLocation)new ResourceLocation("curios", "set_curio_attributes"), (Object)new LootItemFunctionType((net.minecraft.world.level.storage.loot.Serializer)new Serializer()));
    }

    @Nonnull
    public LootItemFunctionType m_7162_() {
        return TYPE;
    }

    @Nonnull
    public Set<LootContextParam<?>> m_6231_() {
        return (Set)this.modifiers.stream().flatMap(mod -> mod.amount.m_6231_().stream()).collect(ImmutableSet.toImmutableSet());
    }

    @Nonnull
    public ItemStack m_7372_(@Nonnull ItemStack stack, LootContext context) {
        RandomSource random = context.m_230907_();
        for (Modifier modifier : this.modifiers) {
            UUID uuid = modifier.id;
            String slot = (String)Util.m_214670_((Object[])modifier.slots, (RandomSource)random);
            Attribute attribute = modifier.attribute;
            if (attribute instanceof SlotAttribute) {
                SlotAttribute wrapper = (SlotAttribute)attribute;
                CuriosApi.addSlotModifier(stack, wrapper.getIdentifier(), modifier.name, uuid, modifier.amount.m_142688_(context), modifier.operation, slot);
                continue;
            }
            CuriosApi.addModifier(stack, modifier.attribute, modifier.name, uuid, modifier.amount.m_142688_(context), modifier.operation, slot);
        }
        return stack;
    }

    public static class Serializer
    extends LootItemConditionalFunction.Serializer<SetCurioAttributesFunction> {
        public void serialize(@Nonnull JsonObject object, @Nonnull SetCurioAttributesFunction function, @Nonnull JsonSerializationContext context) {
            super.m_6170_(object, (LootItemConditionalFunction)function, context);
            JsonArray jsonarray = new JsonArray();
            for (Modifier modifier : function.modifiers) {
                jsonarray.add((JsonElement)modifier.serialize(context));
            }
            object.add("modifiers", (JsonElement)jsonarray);
        }

        @Nonnull
        public SetCurioAttributesFunction deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext context, @Nonnull LootItemCondition[] conditions) {
            JsonArray jsonarray = GsonHelper.m_13933_((JsonObject)object, (String)"modifiers");
            ArrayList list = Lists.newArrayListWithExpectedSize((int)jsonarray.size());
            for (JsonElement jsonelement : jsonarray) {
                list.add(Modifier.deserialize(GsonHelper.m_13918_((JsonElement)jsonelement, (String)"modifier"), context));
            }
            if (list.isEmpty()) {
                throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
            }
            return new SetCurioAttributesFunction(conditions, list);
        }
    }

    static class Modifier {
        final String name;
        final Attribute attribute;
        final AttributeModifier.Operation operation;
        final NumberProvider amount;
        @Nullable
        final UUID id;
        final String[] slots;

        Modifier(String name, Attribute attribute, AttributeModifier.Operation operation, NumberProvider amount, String[] slots, @Nullable UUID uuid) {
            this.name = name;
            this.attribute = attribute;
            this.operation = operation;
            this.amount = amount;
            this.id = uuid;
            this.slots = slots;
        }

        public JsonObject serialize(JsonSerializationContext context) {
            ResourceLocation rl;
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("name", this.name);
            String[] stringArray = this.attribute;
            if (stringArray instanceof SlotAttribute) {
                SlotAttribute wrapper = (SlotAttribute)stringArray;
                rl = new ResourceLocation("curios", wrapper.getIdentifier());
            } else {
                rl = ForgeRegistries.ATTRIBUTES.getKey((Object)this.attribute);
            }
            if (rl != null) {
                jsonobject.addProperty("attribute", rl.toString());
            }
            jsonobject.addProperty("operation", Modifier.operationToString(this.operation));
            jsonobject.add("amount", context.serialize((Object)this.amount));
            if (this.id != null) {
                jsonobject.addProperty("id", this.id.toString());
            }
            if (this.slots.length == 1) {
                jsonobject.addProperty("slot", this.slots[0]);
            } else {
                JsonArray jsonarray = new JsonArray();
                for (String slot : this.slots) {
                    jsonarray.add((JsonElement)new JsonPrimitive(slot));
                }
                jsonobject.add("slot", (JsonElement)jsonarray);
            }
            return jsonobject;
        }

        public static Modifier deserialize(JsonObject object, JsonDeserializationContext context) {
            String[] slots;
            Attribute attribute;
            String s = GsonHelper.m_13906_((JsonObject)object, (String)"name");
            ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.m_13906_((JsonObject)object, (String)"attribute"));
            if (resourcelocation.m_135827_().equals("curios")) {
                String identifier = resourcelocation.m_135815_();
                if (CuriosApi.getSlot(identifier, false).isEmpty()) {
                    throw new JsonSyntaxException("Unknown curios slot type: " + identifier);
                }
                attribute = SlotAttribute.getOrCreate(identifier);
            } else {
                attribute = (Attribute)ForgeRegistries.ATTRIBUTES.getValue(resourcelocation);
            }
            if (attribute == null) {
                throw new JsonSyntaxException("Unknown attribute: " + String.valueOf(resourcelocation));
            }
            AttributeModifier.Operation operation = Modifier.operationFromString(GsonHelper.m_13906_((JsonObject)object, (String)"operation"));
            NumberProvider numberprovider = (NumberProvider)GsonHelper.m_13836_((JsonObject)object, (String)"amount", (JsonDeserializationContext)context, NumberProvider.class);
            UUID uuid = null;
            if (GsonHelper.m_13813_((JsonObject)object, (String)"slot")) {
                slots = new String[]{GsonHelper.m_13906_((JsonObject)object, (String)"slot")};
            } else {
                if (!GsonHelper.m_13885_((JsonObject)object, (String)"slot")) {
                    throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
                }
                JsonArray jsonarray = GsonHelper.m_13933_((JsonObject)object, (String)"slot");
                slots = new String[jsonarray.size()];
                int i = 0;
                for (JsonElement jsonelement : jsonarray) {
                    slots[i++] = GsonHelper.m_13805_((JsonElement)jsonelement, (String)"slot");
                }
                if (slots.length == 0) {
                    throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
                }
            }
            if (object.has("id")) {
                String s1 = GsonHelper.m_13906_((JsonObject)object, (String)"id");
                try {
                    uuid = UUID.fromString(s1);
                }
                catch (IllegalArgumentException illegalargumentexception) {
                    throw new JsonSyntaxException("Invalid attribute modifier id '" + s1 + "' (must be UUID format, with dashes)");
                }
            }
            return new Modifier(s, attribute, operation, numberprovider, slots, uuid);
        }

        private static String operationToString(AttributeModifier.Operation operation) {
            return switch (operation) {
                default -> throw new IncompatibleClassChangeError();
                case AttributeModifier.Operation.ADDITION -> "addition";
                case AttributeModifier.Operation.MULTIPLY_BASE -> "multiply_base";
                case AttributeModifier.Operation.MULTIPLY_TOTAL -> "multiply_total";
            };
        }

        private static AttributeModifier.Operation operationFromString(String operation) {
            return switch (operation) {
                case "addition" -> AttributeModifier.Operation.ADDITION;
                case "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
                case "multiply_total" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
                default -> throw new JsonSyntaxException("Unknown attribute modifier operation " + operation);
            };
        }
    }
}

