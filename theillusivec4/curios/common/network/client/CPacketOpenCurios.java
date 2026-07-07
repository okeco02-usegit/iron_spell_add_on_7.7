/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.network.NetworkEvent$Context
 *  net.minecraftforge.network.NetworkHooks
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerProvider;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketGrabbedItem;

public class CPacketOpenCurios {
    private final ItemStack carried;

    public CPacketOpenCurios(ItemStack stack) {
        this.carried = stack;
    }

    public static void encode(CPacketOpenCurios msg, FriendlyByteBuf buf) {
        buf.m_130055_(msg.carried);
    }

    public static CPacketOpenCurios decode(FriendlyByteBuf buf) {
        return new CPacketOpenCurios(buf.m_130267_());
    }

    public static void handle(CPacketOpenCurios msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context)ctx.get()).getSender();
            if (sender != null) {
                ItemStack stack = sender.m_7500_() ? msg.carried : sender.f_36096_.m_142621_();
                sender.f_36096_.m_142503_(ItemStack.f_41583_);
                NetworkHooks.openScreen((ServerPlayer)sender, (MenuProvider)new CuriosContainerProvider());
                if (!stack.m_41619_()) {
                    sender.f_36096_.m_142503_(stack);
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), (Object)new SPacketGrabbedItem(stack));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

