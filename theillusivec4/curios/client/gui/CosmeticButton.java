/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.ImageButton
 *  net.minecraft.client.gui.components.Tooltip
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.client.gui;

import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketToggleCosmetics;

public class CosmeticButton
extends ImageButton {
    private static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory_revamp.png");
    private final CuriosScreenV2 parentGui;

    CosmeticButton(CuriosScreenV2 parentGui, int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn, 0, 0, CURIO_INVENTORY, button -> {
            ((CuriosContainerV2)parentGui.m_6262_()).toggleCosmetics();
            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketToggleCosmetics(((CuriosContainerV2)parentGui.m_6262_()).f_38840_));
        });
        this.parentGui = parentGui;
        this.m_257544_(Tooltip.m_257550_((Component)Component.m_237115_((String)"gui.curios.toggle.cosmetics")));
    }

    public void m_87963_(@Nonnull GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        int yTex = 0;
        int xTex = ((CuriosContainerV2)this.parentGui.m_6262_()).isViewingCosmetics ? 143 : 123;
        if (this.m_198029_()) {
            yTex = 17;
        }
        this.m_252865_(this.parentGui.getGuiLeft() - 27);
        this.m_253211_(this.parentGui.getGuiTop() - 18);
        guiGraphics.m_280218_(this.f_94223_, this.m_252754_(), this.m_252907_(), xTex, yTex, this.f_93618_, this.f_93619_);
    }
}

