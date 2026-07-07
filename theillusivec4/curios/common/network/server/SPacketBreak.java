/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class SPacketBreak {
    private int entityId;
    private int slotId;
    private String curioId;

    public SPacketBreak(int entityId, String curioId, int slotId) {
        this.entityId = entityId;
        this.slotId = slotId;
        this.curioId = curioId;
    }

    public static void encode(SPacketBreak msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.m_130070_(msg.curioId);
        buf.writeInt(msg.slotId);
    }

    public static SPacketBreak decode(FriendlyByteBuf buf) {
        return new SPacketBreak(buf.readInt(), buf.m_130277_(), buf.readInt());
    }

    public static void handle(SPacketBreak msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity;
            ClientLevel world = Minecraft.m_91087_().f_91073_;
            if (world != null && (entity = Minecraft.m_91087_().f_91073_.m_6815_(msg.entityId)) instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> handler.getStacksHandler(msg.curioId).ifPresent(stacks -> {
                    ItemStack stack = stacks.getStacks().getStackInSlot(msg.slotId);
                    LazyOptional<ICurio> possibleCurio = CuriosApi.getCurio(stack);
                    NonNullList<Boolean> renderStates = stacks.getRenders();
                    possibleCurio.ifPresent(curio -> curio.curioBreak(new SlotContext(msg.curioId, livingEntity, msg.slotId, false, renderStates.size() > msg.slotId && (Boolean)renderStates.get(msg.slotId) != false)));
                    if (!possibleCurio.isPresent()) {
                        ICurio.playBreakAnimation(stack, livingEntity);
                    }
                }));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

