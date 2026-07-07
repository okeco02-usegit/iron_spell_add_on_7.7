/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package top.theillusivec4.curios.mixin.core;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.mixin.CuriosUtilMixinHooks;

@Mixin(value={Inventory.class}, priority=4)
public abstract class MixinInventory
implements Container {
    @Shadow
    @Final
    public Player f_35978_;

    @Inject(at={@At(value="TAIL")}, method={"contains(Lnet/minecraft/world/item/ItemStack;)Z"}, cancellable=true)
    private void curios$containsStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (CuriosUtilMixinHooks.containsStack(this.f_35978_, stack)) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(at={@At(value="TAIL")}, method={"contains(Lnet/minecraft/tags/TagKey;)Z"}, cancellable=true)
    private void curios$containsTag(TagKey<Item> tagKey, CallbackInfoReturnable<Boolean> cir) {
        if (CuriosUtilMixinHooks.containsTag(this.f_35978_, tagKey)) {
            cir.setReturnValue((Object)true);
        }
    }

    public boolean m_216874_(@Nonnull Predicate<ItemStack> predicate) {
        return super.m_216874_(predicate);
    }

    @Inject(at={@At(value="TAIL")}, method={"hasAnyMatching(Ljava/util/function/Predicate;)Z"}, cancellable=true)
    private void curios$hasAnyMatching(Predicate<ItemStack> predicate, CallbackInfoReturnable<Boolean> cir) {
        if (!((Boolean)cir.getReturnValue()).booleanValue() && CuriosUtilMixinHooks.hasAnyMatching(this.f_35978_, predicate)) {
            cir.setReturnValue((Object)true);
        }
    }
}

