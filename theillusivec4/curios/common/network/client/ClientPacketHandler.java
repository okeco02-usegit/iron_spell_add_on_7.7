/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package top.theillusivec4.curios.common.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.server.SPacketQuickMove;

public class ClientPacketHandler {
    public static void handlePacket(SPacketQuickMove msg) {
        AbstractContainerMenu abstractContainerMenu;
        Minecraft mc = Minecraft.m_91087_();
        LocalPlayer player = mc.f_91074_;
        if (player != null && (abstractContainerMenu = player.f_36096_) instanceof CuriosContainerV2) {
            CuriosContainerV2 container = (CuriosContainerV2)abstractContainerMenu;
            container.m_7648_((Player)player, msg.moveIndex);
        }
    }
}

