/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.util.Tuple
 *  net.minecraft.world.inventory.Slot
 *  net.minecraftforge.client.event.ScreenEvent$Init$Post
 *  net.minecraftforge.client.event.ScreenEvent$MouseButtonPressed$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Render$Pre
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.client.gui.CuriosButton;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketDestroy;

public class GuiEventHandler {
    @SubscribeEvent
    public void onInventoryGuiInit(ScreenEvent.Init.Post evt) {
        Screen screen = evt.getScreen();
        if (!((Boolean)CuriosClientConfig.CLIENT.enableButton.get()).booleanValue()) {
            return;
        }
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            AbstractContainerScreen gui = (AbstractContainerScreen)screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;
            Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(isCreative);
            int x = (Integer)offsets.m_14418_();
            int y = (Integer)offsets.m_14419_();
            int size = isCreative ? 10 : 14;
            int textureOffsetX = isCreative ? 64 : 50;
            int yOffset = isCreative ? 68 : 83;
            evt.addListener((GuiEventListener)new CuriosButton(gui, gui.getGuiLeft() + x, gui.getGuiTop() + y + yOffset, size, size, textureOffsetX, 0, size, CuriosScreen.CURIO_INVENTORY));
        }
    }

    @SubscribeEvent
    public void onInventoryGuiDrawBackground(ScreenEvent.Render.Pre evt) {
        Screen screen = evt.getScreen();
        if (!(screen instanceof InventoryScreen)) {
            return;
        }
        InventoryScreen gui = (InventoryScreen)screen;
        gui.f_98831_ = evt.getMouseX();
        gui.f_98832_ = evt.getMouseY();
    }

    @SubscribeEvent
    public void onMouseClick(ScreenEvent.MouseButtonPressed.Pre evt) {
        CreativeModeInventoryScreen gui;
        block6: {
            block5: {
                long handle = Minecraft.m_91087_().m_91268_().m_85439_();
                boolean isLeftShiftDown = InputConstants.m_84830_((long)handle, (int)340);
                boolean isRightShiftDown = InputConstants.m_84830_((long)handle, (int)344);
                boolean isShiftDown = isLeftShiftDown || isRightShiftDown;
                Screen screen = evt.getScreen();
                if (!(screen instanceof CreativeModeInventoryScreen)) break block5;
                gui = (CreativeModeInventoryScreen)screen;
                if (isShiftDown) break block6;
            }
            return;
        }
        if (!gui.m_258017_()) {
            return;
        }
        Slot destroyItemSlot = gui.f_98512_;
        Slot slot = gui.m_97744_(evt.getMouseX(), evt.getMouseY());
        if (destroyItemSlot != null && slot == destroyItemSlot) {
            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketDestroy());
        }
    }
}

