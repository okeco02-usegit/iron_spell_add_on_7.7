/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 */
package top.theillusivec4.curios.api.type.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@Deprecated(forRemoval=true, since="1.20.1")
@ApiStatus.ScheduledForRemoval(inVersion="1.22")
public interface IIconHelper {
    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public void clearIcons();

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public void addIcon(String var1, ResourceLocation var2);

    @Deprecated(forRemoval=true, since="1.20.1")
    @ApiStatus.ScheduledForRemoval(inVersion="1.22")
    public ResourceLocation getIcon(String var1);
}

