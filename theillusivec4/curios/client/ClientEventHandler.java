/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentContents
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.contents.TranslatableContents
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.InputEvent$Key
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.entity.player.ItemTooltipEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.loading.FMLLoader
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.client.KeyRegistry;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;

public class ClientEventHandler {
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.m_91087_();
        if (KeyRegistry.openCurios.m_90859_() && mc.m_91302_()) {
            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketOpenCurios(ItemStack.f_41583_));
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key evt) {
        Minecraft mc = Minecraft.m_91087_();
        LocalPlayer localPlayer = mc.f_91074_;
        if (localPlayer != null && localPlayer.m_242612_() && !(localPlayer.f_36096_ instanceof ICuriosMenu) && evt.getKey() == KeyRegistry.openCurios.getKey().m_84873_() && evt.getAction() == 1) {
            localPlayer.m_6915_();
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent evt) {
        ItemStack stack = evt.getItemStack();
        Player player = evt.getEntity();
        if (!stack.m_41619_()) {
            ArrayList<String> slots;
            Object key;
            List tooltip = evt.getToolTip();
            for (int i = 0; i < tooltip.size(); ++i) {
                Component component = (Component)tooltip.get(i);
                ComponentContents componentContents = component.m_214077_();
                if (!(componentContents instanceof TranslatableContents)) continue;
                TranslatableContents contents = (TranslatableContents)componentContents;
                boolean replace = false;
                Object[] args = contents.m_237523_();
                if (args != null) {
                    for (int i1 = 0; i1 < args.length; ++i1) {
                        TranslatableContents contents1;
                        MutableComponent mutableComponent;
                        ComponentContents componentContents2;
                        Iterator<ResourceLocation> arg = args[i1];
                        if (!(arg instanceof MutableComponent) || !((componentContents2 = (mutableComponent = (MutableComponent)arg).m_214077_()) instanceof TranslatableContents) || (key = (contents1 = (TranslatableContents)componentContents2).m_237508_()) == null || !((String)key).startsWith("curios.slot.")) continue;
                        String actualKey = contents1.m_237508_().replace(".slot.", ".identifier.");
                        contents.m_237523_()[i1] = Component.m_237110_((String)actualKey, (Object[])contents1.m_237523_());
                        replace = true;
                        break;
                    }
                }
                if (!replace) continue;
                tooltip.set(i, Component.m_237110_((String)contents.m_237508_().replace("attribute.modifier.", "curios.modifiers.slots."), (Object[])contents.m_237523_()).m_130948_(component.m_7383_()));
            }
            CompoundTag tag = stack.m_41783_();
            int i = 0;
            if (tag != null && tag.m_128425_("HideFlags", 99)) {
                i = tag.m_128451_("HideFlags");
            }
            Map<String, ISlotType> map = player != null ? CuriosApi.getItemStackSlots(stack, (LivingEntity)player) : CuriosApi.getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT);
            map = new HashMap<String, ISlotType>(map);
            HashSet<String> toRemove = new HashSet<String>();
            block2: for (ISlotType value : map.values()) {
                for (ResourceLocation validator : value.getValidators()) {
                    if (!validator.m_135827_().equals("curios") || !validator.m_135815_().equals("all")) continue;
                    toRemove.add(value.getIdentifier());
                    continue block2;
                }
            }
            for (String s : toRemove) {
                map.remove(s);
            }
            Set<String> curioTags = Set.copyOf(map.keySet());
            if (curioTags.contains("curio")) {
                curioTags = Set.of("curio");
            }
            if (!(slots = new ArrayList<String>(curioTags)).isEmpty()) {
                ArrayList<MutableComponent> tagTooltips = new ArrayList<MutableComponent>();
                MutableComponent slotsTooltip = Component.m_237115_((String)"curios.tooltip.slot").m_130946_(" ").m_130940_(ChatFormatting.GOLD);
                for (int j = 0; j < slots.size(); ++j) {
                    key = "curios.identifier." + (String)slots.get(j);
                    MutableComponent type = Component.m_237115_((String)key);
                    if (j < slots.size() - 1) {
                        type = type.m_130946_(", ");
                    }
                    type = type.m_130940_(ChatFormatting.YELLOW);
                    slotsTooltip.m_7220_((Component)type);
                }
                tagTooltips.add(slotsTooltip);
                LazyOptional<ICurio> optionalCurio = CuriosApi.getCurio(stack);
                optionalCurio.ifPresent(curio -> {
                    List<Component> actualSlotsTooltip = curio.getSlotsTooltip(tagTooltips);
                    if (!actualSlotsTooltip.isEmpty()) {
                        tooltip.addAll(1, actualSlotsTooltip);
                    }
                });
                if (!optionalCurio.isPresent()) {
                    tooltip.addAll(1, tagTooltips);
                }
                ArrayList<MutableComponent> attributeTooltip = new ArrayList<MutableComponent>();
                for (String identifier : slots) {
                    UUID uuid;
                    Multimap<Attribute, AttributeModifier> multimap = CuriosApi.getAttributeModifiers(new SlotContext(identifier, (LivingEntity)player, 0, false, true), uuid = UUID.nameUUIDFromBytes(identifier.getBytes()), stack);
                    if (multimap.isEmpty() || (i & 2) != 0) continue;
                    LinkedHashMap<Attribute, Map> collapsed = new LinkedHashMap<Attribute, Map>();
                    for (Map.Entry entry : multimap.entries()) {
                        if (entry.getKey() == null) continue;
                        Attribute attribute = (Attribute)entry.getKey();
                        AttributeModifier modifier = (AttributeModifier)entry.getValue();
                        AttributeModifier.Operation operation = modifier.m_22217_();
                        collapsed.computeIfAbsent(attribute, k -> new HashMap()).merge(operation, modifier.m_22218_(), Double::sum);
                    }
                    boolean init = false;
                    HashMultimap processed = HashMultimap.create();
                    for (Map.Entry entry : multimap.entries()) {
                        Attribute attribute = (Attribute)entry.getKey();
                        if (attribute == null) continue;
                        AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
                        AttributeModifier.Operation operation = attributemodifier.m_22217_();
                        if (processed.get((Object)attribute).contains(operation)) continue;
                        processed.put((Object)attribute, (Object)operation);
                        if (!init) {
                            attributeTooltip.add(Component.m_237119_());
                            attributeTooltip.add(Component.m_237115_((String)("curios.modifiers." + identifier)).m_130940_(ChatFormatting.GOLD));
                            init = true;
                        }
                        double amount = (Double)((Map)collapsed.get(attribute)).get(operation);
                        boolean flag = false;
                        if (player == null) continue;
                        if (attributemodifier.m_22209_() == ATTACK_DAMAGE_MODIFIER) {
                            AttributeInstance att = player.m_21051_(Attributes.f_22281_);
                            if (att != null) {
                                amount += att.m_22115_();
                            }
                            amount += (double)EnchantmentHelper.m_44833_((ItemStack)stack, (MobType)MobType.f_21640_);
                            flag = true;
                        } else if (attributemodifier.m_22209_() == ATTACK_SPEED_MODIFIER) {
                            AttributeInstance att = player.m_21051_(Attributes.f_22283_);
                            if (att != null) {
                                amount += att.m_22115_();
                            }
                            flag = true;
                        }
                        double d1 = attributemodifier.m_22217_() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.m_22217_() != AttributeModifier.Operation.MULTIPLY_TOTAL ? (((Attribute)entry.getKey()).equals(Attributes.f_22278_) ? amount * 10.0 : amount) : amount * 100.0;
                        Object k2 = entry.getKey();
                        if (k2 instanceof SlotAttribute) {
                            SlotAttribute slotAttribute = (SlotAttribute)((Object)k2);
                            if (amount > 0.0) {
                                attributeTooltip.add(Component.m_237110_((String)("curios.modifiers.slots.plus." + attributemodifier.m_22217_().m_22235_()), (Object[])new Object[]{ItemStack.f_41584_.format(d1), Component.m_237115_((String)("curios.identifier." + slotAttribute.getIdentifier()))}).m_130940_(ChatFormatting.BLUE));
                                continue;
                            }
                            attributeTooltip.add(Component.m_237110_((String)("curios.modifiers.slots.take." + attributemodifier.m_22217_().m_22235_()), (Object[])new Object[]{ItemStack.f_41584_.format(d1 *= -1.0), Component.m_237115_((String)("curios.identifier." + slotAttribute.getIdentifier()))}).m_130940_(ChatFormatting.RED));
                            continue;
                        }
                        if (flag) {
                            attributeTooltip.add(Component.m_237113_((String)" ").m_7220_((Component)Component.m_237110_((String)("attribute.modifier.equals." + attributemodifier.m_22217_().m_22235_()), (Object[])new Object[]{ItemStack.f_41584_.format(d1), Component.m_237115_((String)((Attribute)entry.getKey()).m_22087_())})).m_130940_(ChatFormatting.DARK_GREEN));
                            continue;
                        }
                        if (amount > 0.0) {
                            attributeTooltip.add(Component.m_237110_((String)("attribute.modifier.plus." + attributemodifier.m_22217_().m_22235_()), (Object[])new Object[]{ItemStack.f_41584_.format(d1), Component.m_237115_((String)((Attribute)entry.getKey()).m_22087_())}).m_130940_(ChatFormatting.BLUE));
                            continue;
                        }
                        if (!(amount < 0.0)) continue;
                        attributeTooltip.add(Component.m_237110_((String)("attribute.modifier.take." + attributemodifier.m_22217_().m_22235_()), (Object[])new Object[]{ItemStack.f_41584_.format(d1 *= -1.0), Component.m_237115_((String)((Attribute)entry.getKey()).m_22087_())}).m_130940_(ChatFormatting.RED));
                    }
                }
                optionalCurio.ifPresent(curio -> {
                    List<Component> actualAttributeTooltips = curio.getAttributesTooltip(attributeTooltip);
                    if (!actualAttributeTooltips.isEmpty()) {
                        tooltip.addAll(actualAttributeTooltips);
                    }
                });
                if (!optionalCurio.isPresent()) {
                    tooltip.addAll(attributeTooltip);
                }
            }
        }
    }
}

