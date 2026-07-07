/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  mezz.jei.api.IModPlugin
 *  mezz.jei.api.JeiPlugin
 *  mezz.jei.api.gui.handlers.IGuiContainerHandler
 *  mezz.jei.api.registration.IGuiHandlerRegistration
 *  net.minecraft.resources.ResourceLocation
 */
package top.theillusivec4.curios.common.integration.jei;

import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.integration.jei.CuriosContainerHandler;
import top.theillusivec4.curios.common.integration.jei.CuriosContainerV2Handler;

@JeiPlugin
public class CuriosJeiPlugin
implements IModPlugin {
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("curios", "curios");
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CuriosScreen.class, (IGuiContainerHandler)new CuriosContainerHandler());
        registration.addGuiContainerHandler(CuriosScreenV2.class, (IGuiContainerHandler)new CuriosContainerV2Handler());
    }
}

