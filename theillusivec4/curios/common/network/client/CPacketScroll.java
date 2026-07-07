/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

public class CPacketScroll {
    private int windowId;
    private int index;

    public CPacketScroll(int windowId, int index) {
        this.windowId = windowId;
        this.index = index;
    }

    public static void encode(CPacketScroll msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeInt(msg.index);
    }

    public static CPacketScroll decode(FriendlyByteBuf buf) {
        return new CPacketScroll(buf.readInt(), buf.readInt());
    }

    public static void handle(CPacketScroll msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AbstractContainerMenu container;
            ServerPlayer sender = ((NetworkEvent.Context)ctx.get()).getSender();
            if (sender != null && (container = sender.f_36096_) instanceof CuriosContainer && container.f_38840_ == msg.windowId) {
                ((CuriosContainer)container).scrollToIndex(msg.index);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

