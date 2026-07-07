/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server.sync;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;

public class SPacketSyncData {
    private final ListTag slotData;
    private final ListTag entityData;

    public SPacketSyncData(ListTag slotData, ListTag entityData) {
        this.slotData = slotData;
        this.entityData = entityData;
    }

    public static void encode(SPacketSyncData msg, FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.m_128365_("SlotData", (Tag)msg.slotData);
        tag.m_128365_("EntityData", (Tag)msg.entityData);
        buf.m_130079_(tag);
    }

    public static SPacketSyncData decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.m_130260_();
        if (tag != null) {
            return new SPacketSyncData(tag.m_128437_("SlotData", 10), tag.m_128437_("EntityData", 10));
        }
        return new SPacketSyncData(new ListTag(), new ListTag());
    }

    public static void handle(SPacketSyncData msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CuriosSlotManager.applySyncPacket(msg.slotData);
            CuriosEntityManager.applySyncPacket(msg.entityData);
        });
        ctx.get().setPacketHandled(true);
    }
}

