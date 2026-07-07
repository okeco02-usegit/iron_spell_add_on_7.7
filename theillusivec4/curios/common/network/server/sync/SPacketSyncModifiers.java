/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server.sync;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

public class SPacketSyncModifiers {
    private int entityId;
    private int entrySize;
    private Map<String, CompoundTag> updates;

    public SPacketSyncModifiers(int entityId, Set<ICurioStacksHandler> updates) {
        LinkedHashMap<String, CompoundTag> result = new LinkedHashMap<String, CompoundTag>();
        for (ICurioStacksHandler stacksHandler : updates) {
            result.put(stacksHandler.getIdentifier(), stacksHandler.getSyncTag());
        }
        this.entityId = entityId;
        this.entrySize = result.size();
        this.updates = result;
    }

    public SPacketSyncModifiers(Map<String, CompoundTag> map, int entityId) {
        this.entityId = entityId;
        this.entrySize = map.size();
        this.updates = map;
    }

    public static void encode(SPacketSyncModifiers msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.entrySize);
        for (Map.Entry<String, CompoundTag> entry : msg.updates.entrySet()) {
            buf.m_130070_(entry.getKey());
            buf.m_130079_(entry.getValue());
        }
    }

    public static SPacketSyncModifiers decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int entrySize = buf.readInt();
        LinkedHashMap<String, CompoundTag> map = new LinkedHashMap<String, CompoundTag>();
        for (int i = 0; i < entrySize; ++i) {
            String key = buf.m_130277_();
            map.put(key, buf.m_130260_());
        }
        return new SPacketSyncModifiers(map, entityId);
    }

    public static void handle(SPacketSyncModifiers msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity;
            Minecraft mc = Minecraft.m_91087_();
            ClientLevel world = mc.f_91073_;
            if (world != null && (entity = world.m_6815_(msg.entityId)) instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                    Map<String, ICurioStacksHandler> curios = handler.getCurios();
                    for (Map.Entry<String, CompoundTag> entry : msg.updates.entrySet()) {
                        String id = entry.getKey();
                        ICurioStacksHandler stacksHandler = curios.get(id);
                        if (stacksHandler == null) continue;
                        stacksHandler.applySyncTag(entry.getValue());
                    }
                    if (!msg.updates.isEmpty()) {
                        MinecraftForge.EVENT_BUS.post((Event)new SlotModifiersUpdatedEvent(livingEntity, msg.updates.keySet()));
                    }
                    if (entity instanceof LocalPlayer) {
                        Screen patt3670$temp;
                        LocalPlayer player = (LocalPlayer)entity;
                        if (player.f_36096_ instanceof ICuriosMenu) {
                            ((ICuriosMenu)player.f_36096_).resetSlots();
                        }
                        if ((patt3670$temp = mc.f_91080_) instanceof CuriosScreenV2) {
                            CuriosScreenV2 screen = (CuriosScreenV2)patt3670$temp;
                            screen.updateRenderButtons();
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

