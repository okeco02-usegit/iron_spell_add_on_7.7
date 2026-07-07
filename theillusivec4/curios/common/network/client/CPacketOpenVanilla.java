/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.network.NetworkEvent$Context
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketGrabbedItem;

public class CPacketOpenVanilla {
    private final ItemStack carried;

    public CPacketOpenVanilla(ItemStack stack) {
        this.carried = stack;
    }

    public static void encode(CPacketOpenVanilla msg, FriendlyByteBuf buf) {
        buf.m_130055_(msg.carried);
    }

    public static CPacketOpenVanilla decode(FriendlyByteBuf buf) {
        return new CPacketOpenVanilla(buf.m_130267_());
    }

    public static void handle(CPacketOpenVanilla msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context)ctx.get()).getSender();
            if (sender != null) {
                ItemStack stack = sender.m_7500_() ? msg.carried : sender.f_36096_.m_142621_();
                sender.f_36096_.m_142503_(ItemStack.f_41583_);
                sender.m_9230_();
                if (!stack.m_41619_()) {
                    if (!sender.m_7500_()) {
                        sender.f_36096_.m_142503_(stack);
                    }
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), (Object)new SPacketGrabbedItem(stack));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

