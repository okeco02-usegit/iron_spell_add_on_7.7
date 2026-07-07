/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 */
package top.theillusivec4.curios.api.type.util;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

@Deprecated(forRemoval=true, since="1.20.1")
@ApiStatus.ScheduledForRemoval(inVersion="1.22")
public interface ISlotHelper {
    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public void addSlotType(ISlotType var1);

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public void clear();

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public Optional<ISlotType> getSlotType(String var1);

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public Collection<ISlotType> getSlotTypes();

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public Collection<ISlotType> getSlotTypes(LivingEntity var1);

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public Set<String> getSlotTypeIds();

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public int getSlotsForType(LivingEntity var1, String var2);

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public void setSlotsForType(String var1, LivingEntity var2, int var3);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public SortedMap<ISlotType, ICurioStacksHandler> createSlots(LivingEntity var1);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public SortedMap<ISlotType, ICurioStacksHandler> createSlots();

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void growSlotType(String var1, LivingEntity var2);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void growSlotType(String var1, int var2, LivingEntity var3);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void shrinkSlotType(String var1, LivingEntity var2);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void shrinkSlotType(String var1, int var2, LivingEntity var3);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void unlockSlotType(String var1, LivingEntity var2);

    @Deprecated(forRemoval=true)
    @ApiStatus.ScheduledForRemoval(inVersion="1.21")
    public void lockSlotType(String var1, LivingEntity var2);
}

