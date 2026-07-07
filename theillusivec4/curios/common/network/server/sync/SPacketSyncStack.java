/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server.sync;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class SPacketSyncStack {
    private int entityId;
    private int slotId;
    private String curioId;
    private ItemStack stack;
    private int handlerType;
    private CompoundTag compound;

    public SPacketSyncStack(int entityId, String curioId, int slotId, ItemStack stack, HandlerType handlerType, CompoundTag data) {
        this.entityId = entityId;
        this.slotId = slotId;
        this.stack = stack.m_41777_();
        this.curioId = curioId;
        this.handlerType = handlerType.ordinal();
        this.compound = data;
    }

    public static void encode(SPacketSyncStack msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.m_130070_(msg.curioId);
        buf.writeInt(msg.slotId);
        buf.m_130055_(msg.stack);
        buf.writeInt(msg.handlerType);
        buf.m_130079_(msg.compound);
    }

    public static SPacketSyncStack decode(FriendlyByteBuf buf) {
        return new SPacketSyncStack(buf.readInt(), buf.m_130277_(), buf.readInt(), buf.m_130267_(), HandlerType.fromValue(buf.readInt()), buf.m_130260_());
    }

    public static void handle(SPacketSyncStack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity;
            ClientLevel world = Minecraft.m_91087_().f_91073_;
            if (world != null && (entity = world.m_6815_(msg.entityId)) instanceof LivingEntity) {
                CuriosApi.getCuriosInventory((LivingEntity)entity).ifPresent(handler -> handler.getStacksHandler(msg.curioId).ifPresent(stacksHandler -> {
                    boolean cosmetic;
                    ItemStack stack = msg.stack;
                    CompoundTag compoundNBT = msg.compound;
                    int slot = msg.slotId;
                    boolean bl = cosmetic = HandlerType.fromValue(msg.handlerType) == HandlerType.COSMETIC;
                    if (!compoundNBT.m_128456_()) {
                        NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                        CuriosApi.getCurio(stack).ifPresent(curio -> curio.readSyncData(new SlotContext(msg.curioId, (LivingEntity)entity, slot, cosmetic, renderStates.size() > slot && (Boolean)renderStates.get(slot) != false), compoundNBT));
                    }
                    if (cosmetic) {
                        stacksHandler.getCosmeticStacks().setStackInSlot(slot, stack);
                    } else {
                        stacksHandler.getStacks().setStackInSlot(slot, stack);
                    }
                }));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static enum HandlerType {
        EQUIPMENT,
        COSMETIC;


        public static HandlerType fromValue(int value) {
            try {
                return HandlerType.values()[value];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Unknown handler value: " + value);
            }
        }
    }
}

