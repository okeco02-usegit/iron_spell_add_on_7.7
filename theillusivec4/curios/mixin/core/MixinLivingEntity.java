/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.mixin.CuriosUtilMixinHooks;

@Mixin(value={LivingEntity.class})
public class MixinLivingEntity {
    @Inject(at={@At(value="TAIL")}, method={"canFreeze()Z"}, cancellable=true)
    public void curio$canFreeze(CallbackInfoReturnable<Boolean> cir) {
        if (CuriosUtilMixinHooks.isFreezeImmune((LivingEntity)this)) {
            cir.setReturnValue((Object)false);
        }
    }
}

