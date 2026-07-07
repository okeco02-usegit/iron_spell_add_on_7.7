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
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

public class CPacketToggleCosmetics {
    private final int windowId;

    public CPacketToggleCosmetics(int windowId) {
        this.windowId = windowId;
    }

    public static void encode(CPacketToggleCosmetics msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
    }

    public static CPacketToggleCosmetics decode(FriendlyByteBuf buf) {
        return new CPacketToggleCosmetics(buf.readInt());
    }

    public static void handle(CPacketToggleCosmetics msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AbstractContainerMenu container;
            ServerPlayer sender = ((NetworkEvent.Context)ctx.get()).getSender();
            if (sender != null && (container = sender.f_36096_) instanceof CuriosContainerV2 && container.f_38840_ == msg.windowId) {
                ((CuriosContainerV2)container).toggleCosmetics();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

