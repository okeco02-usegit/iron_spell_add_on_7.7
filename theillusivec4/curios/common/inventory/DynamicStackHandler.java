/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.eventbus.api.Event$Result
 *  net.minecraftforge.items.ItemStackHandler
 */
package top.theillusivec4.curios.common.inventory;

import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class DynamicStackHandler
extends ItemStackHandler
implements IDynamicStackHandler {
    protected NonNullList<ItemStack> previousStacks;
    protected Function<Integer, SlotContext> ctxBuilder;

    public DynamicStackHandler(int size, Function<Integer, SlotContext> ctxBuilder) {
        super(size);
        this.previousStacks = NonNullList.m_122780_((int)size, (Object)ItemStack.f_41583_);
        this.ctxBuilder = ctxBuilder;
    }

    @Override
    public void setPreviousStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.validateSlotIndex(slot);
        this.previousStacks.set(slot, (Object)stack);
        this.onContentsChanged(slot);
    }

    @Override
    @Nonnull
    public ItemStack getPreviousStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return (ItemStack)this.previousStacks.get(slot);
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        SlotContext ctx = this.ctxBuilder.apply(slot);
        CurioEquipEvent equipEvent = new CurioEquipEvent(stack, ctx);
        MinecraftForge.EVENT_BUS.post((Event)equipEvent);
        Event.Result result = equipEvent.getResult();
        if (result == Event.Result.DENY) {
            return false;
        }
        return result == Event.Result.ALLOW || CuriosApi.isStackValid(ctx, stack) && CuriosApi.getCurio(stack).map(curio -> curio.canEquip(ctx)).orElse(true) != false && super.isItemValid(slot, stack);
    }

    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Player player;
        boolean isCreative;
        ItemStack existing = (ItemStack)this.stacks.get(slot);
        SlotContext ctx = this.ctxBuilder.apply(slot);
        CurioUnequipEvent unequipEvent = new CurioUnequipEvent(existing, ctx);
        MinecraftForge.EVENT_BUS.post((Event)unequipEvent);
        Event.Result result = unequipEvent.getResult();
        if (result == Event.Result.DENY) {
            return ItemStack.f_41583_;
        }
        LivingEntity livingEntity = ctx.entity();
        boolean bl = isCreative = livingEntity instanceof Player && (player = (Player)livingEntity).m_7500_();
        if (result == Event.Result.ALLOW || (existing.m_41619_() || isCreative || !EnchantmentHelper.m_44920_((ItemStack)existing)) && CuriosApi.getCurio(existing).map(curio -> curio.canUnequip(ctx)).orElse(true).booleanValue()) {
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.f_41583_;
    }

    @Override
    public void grow(int amount) {
        this.stacks = DynamicStackHandler.getResizedList(this.stacks.size() + amount, (NonNullList<ItemStack>)this.stacks);
        this.previousStacks = DynamicStackHandler.getResizedList(this.previousStacks.size() + amount, this.previousStacks);
    }

    @Override
    public void shrink(int amount) {
        this.stacks = DynamicStackHandler.getResizedList(this.stacks.size() - amount, (NonNullList<ItemStack>)this.stacks);
        this.previousStacks = DynamicStackHandler.getResizedList(this.previousStacks.size() - amount, this.previousStacks);
    }

    private static NonNullList<ItemStack> getResizedList(int size, NonNullList<ItemStack> stacks) {
        NonNullList newList = NonNullList.m_122780_((int)Math.max(0, size), (Object)ItemStack.f_41583_);
        for (int i = 0; i < newList.size() && i < stacks.size(); ++i) {
            newList.set(i, (Object)((ItemStack)stacks.get(i)));
        }
        return newList;
    }
}

