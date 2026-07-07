/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 */
package top.theillusivec4.curios.api.type.inventory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public interface ICurioStacksHandler {
    public IDynamicStackHandler getStacks();

    public IDynamicStackHandler getCosmeticStacks();

    public NonNullList<Boolean> getRenders();

    default public boolean canToggleRendering() {
        return true;
    }

    default public ICurio.DropRule getDropRule() {
        return ICurio.DropRule.DEFAULT;
    }

    public int getSlots();

    public boolean isVisible();

    public boolean hasCosmetic();

    public CompoundTag serializeNBT();

    public void deserializeNBT(CompoundTag var1);

    public String getIdentifier();

    public Map<UUID, AttributeModifier> getModifiers();

    public Set<AttributeModifier> getPermanentModifiers();

    public Set<AttributeModifier> getCachedModifiers();

    public Collection<AttributeModifier> getModifiersByOperation(AttributeModifier.Operation var1);

    public void addTransientModifier(AttributeModifier var1);

    public void addPermanentModifier(AttributeModifier var1);

    public void removeModifier(UUID var1);

    public void clearModifiers();

    public void clearCachedModifiers();

    public void copyModifiers(ICurioStacksHandler var1);

    public void update();

    public CompoundTag getSyncTag();

    public void applySyncTag(CompoundTag var1);

    @Deprecated
    public int getSizeShift();

    @Deprecated
    public void grow(int var1);

    @Deprecated
    public void shrink(int var1);
}

