/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.block.PowderSnowBlock
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.mixin.CuriosUtilMixinHooks;

@Mixin(value={PowderSnowBlock.class})
public class MixinPowderSnowBlock {
    @Inject(at={@At(value="RETURN")}, method={"canEntityWalkOnPowderSnow"}, cancellable=true)
    private static void curios$canEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity;
        if (entity instanceof LivingEntity && CuriosUtilMixinHooks.canWalkOnPowderSnow(livingEntity = (LivingEntity)entity)) {
            cir.setReturnValue((Object)true);
        }
    }
}

