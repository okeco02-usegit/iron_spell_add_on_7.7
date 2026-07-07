/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  javax.annotation.Nonnull
 *  net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance
 *  net.minecraft.advancements.critereon.ContextAwarePredicate
 *  net.minecraft.advancements.critereon.DeserializationContext
 *  net.minecraft.advancements.critereon.ItemPredicate
 *  net.minecraft.advancements.critereon.LocationPredicate
 *  net.minecraft.advancements.critereon.SerializationContext
 *  net.minecraft.advancements.critereon.SimpleCriterionTrigger
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.ItemStack
 */
package top.theillusivec4.curios.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotPredicate;

public class EquipCurioTrigger
extends SimpleCriterionTrigger<Instance> {
    public static final ResourceLocation ID = new ResourceLocation("curios", "equip_curio");
    public static final EquipCurioTrigger INSTANCE = new EquipCurioTrigger();

    private EquipCurioTrigger() {
    }

    @Nonnull
    public ResourceLocation m_7295_() {
        return ID;
    }

    @Nonnull
    public Instance createInstance(@Nonnull JsonObject json, @Nonnull ContextAwarePredicate playerPred, @Nonnull DeserializationContext conditions) {
        return new Instance(playerPred, ItemPredicate.m_45051_((JsonElement)json.get("item")), LocationPredicate.m_52629_((JsonElement)json.get("location")), SlotPredicate.fromJson(json.get("curios:slot")));
    }

    public void trigger(ServerPlayer player, ItemStack stack, ServerLevel world, double x, double y, double z) {
        this.m_66234_(player, instance -> instance.test(null, stack, world, x, y, z));
    }

    public void trigger(SlotContext slotContext, ServerPlayer player, ItemStack stack, ServerLevel world, double x, double y, double z) {
        this.m_66234_(player, instance -> instance.test(slotContext, stack, world, x, y, z));
    }

    public static class Instance
    extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        private final LocationPredicate location;
        private final SlotPredicate slot;

        public Instance(ContextAwarePredicate playerPred, ItemPredicate count, LocationPredicate indexPos, SlotPredicate slotPredicate) {
            super(ID, playerPred);
            this.item = count;
            this.location = indexPos;
            this.slot = slotPredicate;
        }

        @Nonnull
        public JsonObject m_7683_(@Nonnull SerializationContext pContext) {
            JsonObject jsonobject = super.m_7683_(pContext);
            jsonobject.add("location", this.location.m_52616_());
            jsonobject.add("item", this.item.m_45048_());
            jsonobject.add("curios:slot", this.slot.serializeToJson());
            return jsonobject;
        }

        @Nonnull
        public ResourceLocation m_7294_() {
            return ID;
        }

        boolean test(SlotContext slotContext, ItemStack stack, ServerLevel world, double x, double y, double z) {
            if (this.slot != null && slotContext != null && !this.slot.matches(slotContext)) {
                return false;
            }
            return this.item.m_45049_(stack) && this.location.m_52617_(world, x, y, z);
        }
    }
}

