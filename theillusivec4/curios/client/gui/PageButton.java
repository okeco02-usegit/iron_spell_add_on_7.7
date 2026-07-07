/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.network.chat.CommonComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.client.gui;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketPage;

public class PageButton
extends Button {
    private final CuriosScreenV2 parentGui;
    private final Type type;
    private static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory_revamp.png");

    public PageButton(CuriosScreenV2 parentGui, int xIn, int yIn, int widthIn, int heightIn, Type type) {
        super(xIn, yIn, widthIn, heightIn, CommonComponents.f_237098_, button -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketPage(((CuriosContainerV2)parentGui.m_6262_()).f_38840_, type == Type.NEXT)), f_252438_);
        this.parentGui = parentGui;
        this.type = type;
    }

    public void m_87963_(@Nonnull GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        int xText = this.type == Type.NEXT ? 43 : 32;
        int yText = 25;
        if (this.type == Type.NEXT) {
            this.m_252865_(this.parentGui.getGuiLeft() - 17);
            this.f_93623_ = ((CuriosContainerV2)this.parentGui.m_6262_()).currentPage + 1 < ((CuriosContainerV2)this.parentGui.m_6262_()).totalPages;
        } else {
            this.m_252865_(this.parentGui.getGuiLeft() - 28);
            boolean bl = this.f_93623_ = ((CuriosContainerV2)this.parentGui.m_6262_()).currentPage > 0;
        }
        if (!this.m_142518_()) {
            yText += 12;
        } else if (this.m_198029_()) {
            xText += 22;
        }
        if (this.m_274382_()) {
            guiGraphics.m_280557_(Minecraft.m_91087_().f_91062_, (Component)Component.m_237110_((String)"gui.curios.page", (Object[])new Object[]{((CuriosContainerV2)this.parentGui.m_6262_()).currentPage + 1, ((CuriosContainerV2)this.parentGui.m_6262_()).totalPages}), x, y);
        }
        guiGraphics.m_280218_(CURIO_INVENTORY, this.m_252754_(), this.m_252907_(), xText, yText, this.f_93618_, this.f_93619_);
    }

    public static enum Type {
        NEXT,
        PREVIOUS;

    }
}

