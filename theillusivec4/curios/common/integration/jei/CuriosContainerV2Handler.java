/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  mezz.jei.api.gui.handlers.IGuiContainerHandler
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.Rect2i
 */
package top.theillusivec4.curios.common.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

public class CuriosContainerV2Handler
implements IGuiContainerHandler<CuriosScreenV2> {
    @Nonnull
    public List<Rect2i> getGuiExtraAreas(CuriosScreenV2 containerScreen) {
        LocalPlayer player = containerScreen.getMinecraft().f_91074_;
        if (player != null) {
            ArrayList<Rect2i> areas = new ArrayList<Rect2i>();
            int left = containerScreen.getGuiLeft() - containerScreen.panelWidth;
            int top = containerScreen.getGuiTop();
            List<Integer> list = ((CuriosContainerV2)containerScreen.m_6262_()).grid;
            int height = 0;
            if (!list.isEmpty()) {
                height = list.get(0) * 18 + 14;
                if (((CuriosContainerV2)containerScreen.m_6262_()).hasCosmetics) {
                    areas.add(new Rect2i(containerScreen.getGuiLeft() - 30, top - 34, 28, 34));
                }
            }
            areas.add(new Rect2i(left, top, containerScreen.panelWidth, height));
            return areas;
        }
        return Collections.emptyList();
    }
}

