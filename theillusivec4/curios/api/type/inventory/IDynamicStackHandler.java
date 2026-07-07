/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.items.IItemHandlerModifiable
 */
package top.theillusivec4.curios.api.type.inventory;

import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IDynamicStackHandler
extends IItemHandlerModifiable {
    public void setStackInSlot(int var1, @Nonnull ItemStack var2);

    @Nonnull
    public ItemStack getStackInSlot(int var1);

    public void setPreviousStackInSlot(int var1, @Nonnull ItemStack var2);

    public ItemStack getPreviousStackInSlot(int var1);

    public int getSlots();

    public void grow(int var1);

    public void shrink(int var1);

    public CompoundTag serializeNBT();

    public void deserializeNBT(CompoundTag var1);
}

