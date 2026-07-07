/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.data.IEntitiesData;
import top.theillusivec4.curios.api.type.data.ISlotData;
import top.theillusivec4.curios.common.data.EntitiesData;
import top.theillusivec4.curios.common.data.SlotData;

@Mixin(value={CuriosDataProvider.class}, remap=false)
public class MixinCuriosDataProvider {
    @Inject(at={@At(value="HEAD")}, method={"createSlotData"}, cancellable=true)
    private static void curios$createSlotData(CallbackInfoReturnable<ISlotData> cir) {
        cir.setReturnValue((Object)new SlotData());
    }

    @Inject(at={@At(value="HEAD")}, method={"createEntitiesData"}, cancellable=true)
    private static void curios$createEntitiesData(CallbackInfoReturnable<IEntitiesData> cir) {
        cir.setReturnValue((Object)new EntitiesData());
    }
}

