/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.piglin.PiglinAi
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.mixin.CuriosUtilMixinHooks;

@Mixin(value={PiglinAi.class})
public class MixinPiglinAi {
    @Inject(at={@At(value="RETURN")}, method={"isWearingGold"}, cancellable=true)
    private static void curios$isWearingGold(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (CuriosUtilMixinHooks.canNeutralizePiglins(livingEntity)) {
            cir.setReturnValue((Object)true);
        }
    }
}

