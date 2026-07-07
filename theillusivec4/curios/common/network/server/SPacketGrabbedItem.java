/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SPacketGrabbedItem {
    private final ItemStack stack;

    public SPacketGrabbedItem(ItemStack stackIn) {
        this.stack = stackIn;
    }

    public static void encode(SPacketGrabbedItem msg, FriendlyByteBuf buf) {
        buf.m_130055_(msg.stack);
    }

    public static SPacketGrabbedItem decode(FriendlyByteBuf buf) {
        return new SPacketGrabbedItem(buf.m_130267_());
    }

    public static void handle(SPacketGrabbedItem msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer clientPlayer = Minecraft.m_91087_().f_91074_;
            if (clientPlayer != null) {
                clientPlayer.f_36096_.m_142503_(msg.stack);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

