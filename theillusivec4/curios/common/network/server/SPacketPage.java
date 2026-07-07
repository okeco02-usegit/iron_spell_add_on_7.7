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
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

public class SPacketPage {
    private final int windowId;
    private final int page;

    public SPacketPage(int windowId, int page) {
        this.windowId = windowId;
        this.page = page;
    }

    public static void encode(SPacketPage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeInt(msg.page);
    }

    public static SPacketPage decode(FriendlyByteBuf buf) {
        return new SPacketPage(buf.readInt(), buf.readInt());
    }

    public static void handle(SPacketPage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AbstractContainerMenu container;
            Minecraft mc = Minecraft.m_91087_();
            LocalPlayer clientPlayer = mc.f_91074_;
            Screen screen = mc.f_91080_;
            if (clientPlayer != null && (container = clientPlayer.f_36096_) instanceof CuriosContainerV2 && container.f_38840_ == msg.windowId) {
                ((CuriosContainerV2)container).setPage(msg.page);
            }
            if (screen instanceof CuriosScreenV2) {
                ((CuriosScreenV2)screen).updateRenderButtons();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

