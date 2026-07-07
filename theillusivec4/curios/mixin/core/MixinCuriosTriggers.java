/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.CriterionTriggerInstance
 *  net.minecraft.advancements.critereon.ContextAwarePredicate
 *  net.minecraft.advancements.critereon.ItemPredicate$Builder
 *  net.minecraft.advancements.critereon.LocationPredicate
 *  net.minecraft.advancements.critereon.LocationPredicate$Builder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosTriggers;
import top.theillusivec4.curios.api.SlotPredicate;
import top.theillusivec4.curios.common.util.EquipCurioTrigger;

@Mixin(value={CuriosTriggers.class}, remap=false)
public class MixinCuriosTriggers {
    @Inject(at={@At(value="HEAD")}, method={"equip(Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;)Lnet/minecraft/advancements/CriterionTriggerInstance;"}, cancellable=true)
    private static void curios$equip(ItemPredicate.Builder itemPredicate, CallbackInfoReturnable<CriterionTriggerInstance> cir) {
        cir.setReturnValue((Object)new EquipCurioTrigger.Instance(ContextAwarePredicate.f_285567_, itemPredicate.m_45077_(), LocationPredicate.f_52592_, SlotPredicate.ANY));
    }

    @Inject(at={@At(value="HEAD")}, method={"equipAtLocation"}, cancellable=true)
    private static void curios$equipAtLocation(ItemPredicate.Builder itemPredicate, LocationPredicate.Builder locationPredicate, CallbackInfoReturnable<CriterionTriggerInstance> cir) {
        cir.setReturnValue((Object)new EquipCurioTrigger.Instance(ContextAwarePredicate.f_285567_, itemPredicate.m_45077_(), locationPredicate.m_52658_(), SlotPredicate.ANY));
    }
}

