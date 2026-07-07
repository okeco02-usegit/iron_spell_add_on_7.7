/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.critereon.NbtPredicate
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package top.theillusivec4.curios.mixin.core;

import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.curios.mixin.CuriosUtilMixinHooks;

@Mixin(value={NbtPredicate.class})
public class MixinNbtPredicate {
    @ModifyVariable(at=@At(value="RETURN"), method={"getEntityTagToCompare"})
    private static CompoundTag curios$mergeCuriosInventory(CompoundTag compoundTag, Entity entity) {
        return CuriosUtilMixinHooks.mergeCuriosInventory(compoundTag, entity);
    }
}

