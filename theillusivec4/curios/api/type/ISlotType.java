/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 */
package top.theillusivec4.curios.api.type;

import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import top.theillusivec4.curios.api.type.capability.ICurio;

public interface ISlotType
extends Comparable<ISlotType> {
    public String getIdentifier();

    public ResourceLocation getIcon();

    public int getOrder();

    public int getSize();

    public boolean useNativeGui();

    public boolean hasCosmetic();

    public boolean canToggleRendering();

    public ICurio.DropRule getDropRule();

    default public Set<ResourceLocation> getValidators() {
        return Set.of();
    }

    default public CompoundTag writeNbt() {
        return new CompoundTag();
    }

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    default public boolean isLocked() {
        return this.getSize() == 0;
    }

    @Deprecated(since="1.20.1", forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    default public int getPriority() {
        return this.getOrder();
    }

    @Deprecated(since="1.20.1", forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    default public boolean isVisible() {
        return this.useNativeGui();
    }
}

