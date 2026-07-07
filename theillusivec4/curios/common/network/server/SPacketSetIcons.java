/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.server.command.CurioArgumentType;

public class SPacketSetIcons {
    private int entrySize;
    private Map<String, ResourceLocation> map;

    public SPacketSetIcons(Map<String, ResourceLocation> map) {
        this.entrySize = map.size();
        this.map = map;
    }

    public static void encode(SPacketSetIcons msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entrySize);
        for (Map.Entry<String, ResourceLocation> entry : msg.map.entrySet()) {
            buf.m_130070_(entry.getKey());
            buf.m_130070_(entry.getValue().toString());
        }
    }

    public static SPacketSetIcons decode(FriendlyByteBuf buf) {
        int entrySize = buf.readInt();
        HashMap<String, ResourceLocation> map = new HashMap<String, ResourceLocation>();
        for (int i = 0; i < entrySize; ++i) {
            map.put(buf.m_130277_(), new ResourceLocation(buf.m_130277_()));
        }
        return new SPacketSetIcons(map);
    }

    public static void handle(SPacketSetIcons msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.m_91087_().f_91073_;
            HashSet<String> slotIds = new HashSet<String>();
            if (world != null) {
                CuriosApi.getIconHelper().clearIcons();
                HashMap<String, ResourceLocation> icons = new HashMap<String, ResourceLocation>();
                for (Map.Entry<String, ResourceLocation> entry : msg.map.entrySet()) {
                    CuriosApi.getIconHelper().addIcon(entry.getKey(), entry.getValue());
                    icons.put(entry.getKey(), entry.getValue());
                    slotIds.add(entry.getKey());
                }
                CuriosSlotManager.CLIENT.setIcons(icons);
            }
            CurioArgumentType.slotIds = slotIds;
        });
        ctx.get().setPacketHandled(true);
    }
}

