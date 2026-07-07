/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

public class SPacketScroll {
    private int windowId;
    private int index;

    public SPacketScroll(int windowId, int index) {
        this.windowId = windowId;
        this.index = index;
    }

    public static void encode(SPacketScroll msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeInt(msg.index);
    }

    public static SPacketScroll decode(FriendlyByteBuf buf) {
        return new SPacketScroll(buf.readInt(), buf.readInt());
    }

    public static void handle(SPacketScroll msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AbstractContainerMenu container;
            Minecraft mc = Minecraft.m_91087_();
            LocalPlayer clientPlayer = mc.f_91074_;
            Screen screen = mc.f_91080_;
            if (clientPlayer != null && (container = clientPlayer.f_36096_) instanceof CuriosContainer && container.f_38840_ == msg.windowId) {
                ((CuriosContainer)container).scrollToIndex(msg.index);
            }
            if (screen instanceof CuriosScreen) {
                ((CuriosScreen)screen).updateRenderButtons();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

