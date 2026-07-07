/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.ImageButton
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Tuple
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.client.gui;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.client.ICuriosScreen;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;
import top.theillusivec4.curios.common.network.client.CPacketOpenVanilla;

public class CuriosButton
extends ImageButton {
    private final AbstractContainerScreen<?> parentGui;

    CuriosButton(AbstractContainerScreen<?> parentGui, int xIn, int yIn, int widthIn, int heightIn, int textureOffsetX, int textureOffsetY, int yDiffText, ResourceLocation resource) {
        super(xIn, yIn, widthIn, heightIn, textureOffsetX, textureOffsetY, yDiffText, resource, button -> {
            Minecraft mc = Minecraft.m_91087_();
            if (mc.f_91074_ != null) {
                ItemStack stack = mc.f_91074_.f_36096_.m_142621_();
                mc.f_91074_.f_36096_.m_142503_(ItemStack.f_41583_);
                if (parentGui instanceof ICuriosScreen) {
                    InventoryScreen inventory = new InventoryScreen((Player)mc.f_91074_);
                    mc.m_91152_((Screen)inventory);
                    mc.f_91074_.f_36096_.m_142503_(stack);
                    NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketOpenVanilla(stack));
                } else {
                    InventoryScreen inventory;
                    RecipeBookComponent recipeBookGui;
                    if (parentGui instanceof InventoryScreen && (recipeBookGui = (inventory = (InventoryScreen)parentGui).m_5564_()).m_100385_()) {
                        recipeBookGui.m_100384_();
                    }
                    NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketOpenCurios(stack));
                }
            }
        });
        this.parentGui = parentGui;
    }

    public void m_87963_(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(this.parentGui instanceof CreativeModeInventoryScreen);
        this.m_252865_(this.parentGui.getGuiLeft() + (Integer)offsets.m_14418_());
        int yOffset = this.parentGui instanceof CreativeModeInventoryScreen ? 68 : 83;
        this.m_253211_(this.parentGui.getGuiTop() + (Integer)offsets.m_14419_() + yOffset);
        AbstractContainerScreen<?> abstractContainerScreen = this.parentGui;
        if (abstractContainerScreen instanceof CreativeModeInventoryScreen) {
            boolean isInventoryTab;
            CreativeModeInventoryScreen gui = (CreativeModeInventoryScreen)abstractContainerScreen;
            this.f_93623_ = isInventoryTab = gui.m_258017_();
            if (!isInventoryTab) {
                return;
            }
        }
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
    }
}

