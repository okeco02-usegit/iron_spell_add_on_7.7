/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import java.util.Map;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.common.CuriosHelper;

@Mixin(value={SlotAttribute.class}, remap=false)
public class MixinSlotAttribute {
    @Shadow
    @Final
    private static Map<String, SlotAttribute> SLOT_ATTRIBUTES;

    @Inject(at={@At(value="HEAD")}, method={"getOrCreate"}, cancellable=true)
    private static void curios$slotAttribute(String id, CallbackInfoReturnable<SlotAttribute> ci) {
        ci.setReturnValue((Object)SLOT_ATTRIBUTES.computeIfAbsent(id, CuriosHelper.SlotAttributeWrapper::new));
    }
}

