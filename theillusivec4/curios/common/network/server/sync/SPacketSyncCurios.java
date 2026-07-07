/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server.sync;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.common.inventory.CurioStacksHandler;

public class SPacketSyncCurios {
    private int entityId;
    private int entrySize;
    private Map<String, CompoundTag> map;

    public SPacketSyncCurios(int entityId, Map<String, ICurioStacksHandler> map) {
        LinkedHashMap<String, CompoundTag> result = new LinkedHashMap<String, CompoundTag>();
        for (Map.Entry<String, ICurioStacksHandler> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getSyncTag());
        }
        this.entityId = entityId;
        this.entrySize = map.size();
        this.map = result;
    }

    public SPacketSyncCurios(Map<String, CompoundTag> map, int entityId) {
        this.entityId = entityId;
        this.entrySize = map.size();
        this.map = map;
    }

    public static void encode(SPacketSyncCurios msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.entrySize);
        for (Map.Entry<String, CompoundTag> entry : msg.map.entrySet()) {
            buf.m_130070_(entry.getKey());
            buf.m_130079_(entry.getValue());
        }
    }

    public static SPacketSyncCurios decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int entrySize = buf.readInt();
        LinkedHashMap<String, CompoundTag> map = new LinkedHashMap<String, CompoundTag>();
        for (int i = 0; i < entrySize; ++i) {
            String key = buf.m_130277_();
            map.put(key, buf.m_130260_());
        }
        return new SPacketSyncCurios(map, entityId);
    }

    public static void handle(SPacketSyncCurios msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity;
            ClientLevel world = Minecraft.m_91087_().f_91073_;
            if (world != null && (entity = world.m_6815_(msg.entityId)) instanceof LivingEntity) {
                CuriosApi.getCuriosInventory((LivingEntity)entity).ifPresent(handler -> {
                    LinkedHashMap<String, ICurioStacksHandler> stacks = new LinkedHashMap<String, ICurioStacksHandler>();
                    for (Map.Entry<String, CompoundTag> entry : msg.map.entrySet()) {
                        CurioStacksHandler stacksHandler = new CurioStacksHandler((ICuriosItemHandler)handler, entry.getKey());
                        stacksHandler.applySyncTag(entry.getValue());
                        stacks.put(entry.getKey(), stacksHandler);
                    }
                    handler.setCurios(stacks);
                    if (entity instanceof LocalPlayer) {
                        LocalPlayer player = (LocalPlayer)entity;
                        AbstractContainerMenu patt4037$temp = player.f_36096_;
                        if (patt4037$temp instanceof ICuriosMenu) {
                            ICuriosMenu curiosContainer = (ICuriosMenu)patt4037$temp;
                            curiosContainer.resetSlots();
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

