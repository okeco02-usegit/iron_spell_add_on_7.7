/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.network.NetworkEvent$Context
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.common.network.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncStack;

public class CPacketDestroy {
    public static void encode(CPacketDestroy msg, FriendlyByteBuf buf) {
    }

    public static CPacketDestroy decode(FriendlyByteBuf buf) {
        return new CPacketDestroy();
    }

    public static void handle(CPacketDestroy msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context)ctx.get()).getSender();
            if (sender != null) {
                CuriosApi.getCuriosInventory((LivingEntity)sender).ifPresent(handler -> handler.getCurios().values().forEach(stacksHandler -> {
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    IDynamicStackHandler cosmeticStackHandler = stacksHandler.getCosmeticStacks();
                    String id = stacksHandler.getIdentifier();
                    for (int i = 0; i < stackHandler.getSlots(); ++i) {
                        NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                        SlotContext slotContext = new SlotContext(id, (LivingEntity)sender, i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false);
                        UUID uuid = CuriosApi.getSlotUuid(slotContext);
                        ItemStack stack = stackHandler.getStackInSlot(i);
                        Multimap<Attribute, AttributeModifier> map = CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
                        HashMultimap slots = HashMultimap.create();
                        HashSet<SlotAttribute> toRemove = new HashSet<SlotAttribute>();
                        for (Attribute attribute : map.keySet()) {
                            if (!(attribute instanceof SlotAttribute)) continue;
                            SlotAttribute wrapper = (SlotAttribute)attribute;
                            slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                            toRemove.add(wrapper);
                        }
                        for (Attribute attribute : toRemove) {
                            map.removeAll((Object)attribute);
                        }
                        sender.m_21204_().m_22161_(map);
                        handler.removeSlotModifiers((Multimap<String, AttributeModifier>)slots);
                        CuriosApi.getCurio(stack).ifPresent(curio -> curio.onUnequip(slotContext, stack));
                        stackHandler.setStackInSlot(i, ItemStack.f_41583_);
                        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sender), (Object)new SPacketSyncStack(sender.m_19879_(), id, i, ItemStack.f_41583_, SPacketSyncStack.HandlerType.EQUIPMENT, new CompoundTag()));
                        cosmeticStackHandler.setStackInSlot(i, ItemStack.f_41583_);
                        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sender), (Object)new SPacketSyncStack(sender.m_19879_(), id, i, ItemStack.f_41583_, SPacketSyncStack.HandlerType.COSMETIC, new CompoundTag()));
                    }
                }));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

