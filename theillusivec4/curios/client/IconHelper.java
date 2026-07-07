/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package top.theillusivec4.curios.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.type.util.IIconHelper;

public class IconHelper
implements IIconHelper {
    private Map<String, ResourceLocation> idToIcon = new HashMap<String, ResourceLocation>();

    @Override
    public void clearIcons() {
        this.idToIcon.clear();
    }

    @Override
    public void addIcon(String identifier, ResourceLocation resourceLocation) {
        this.idToIcon.putIfAbsent(identifier, resourceLocation);
    }

    @Override
    public ResourceLocation getIcon(String identifier) {
        return this.idToIcon.getOrDefault(identifier, new ResourceLocation("curios", "slot/empty_curio_slot"));
    }
}

