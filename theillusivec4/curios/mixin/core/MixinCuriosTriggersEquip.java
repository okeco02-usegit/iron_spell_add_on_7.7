/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.CriterionTriggerInstance
 *  net.minecraft.advancements.critereon.ContextAwarePredicate
 *  net.minecraft.advancements.critereon.ItemPredicate$Builder
 *  net.minecraft.advancements.critereon.LocationPredicate$Builder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosTriggers;
import top.theillusivec4.curios.api.SlotPredicate;
import top.theillusivec4.curios.common.util.EquipCurioTrigger;

@Mixin(value={CuriosTriggers.EquipBuilder.class}, remap=false)
public class MixinCuriosTriggersEquip {
    @Shadow
    private ItemPredicate.Builder itemPredicate;
    @Shadow
    private LocationPredicate.Builder locationPredicate;
    @Shadow
    private SlotPredicate.Builder slotPredicate;

    @Inject(at={@At(value="HEAD")}, method={"build"}, cancellable=true)
    private void curios$equipAtLocation(CallbackInfoReturnable<CriterionTriggerInstance> cir) {
        cir.setReturnValue((Object)new EquipCurioTrigger.Instance(ContextAwarePredicate.f_285567_, this.itemPredicate.m_45077_(), this.locationPredicate.m_52658_(), this.slotPredicate.build()));
    }
}

