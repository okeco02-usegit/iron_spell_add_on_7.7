/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.players.PlayerList
 *  net.minecraft.util.Mth
 *  net.minecraft.util.Tuple
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.ExperienceOrb
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.event.AttachCapabilitiesEvent
 *  net.minecraftforge.event.OnDatapackSyncEvent
 *  net.minecraftforge.event.entity.EntityJoinLevelEvent
 *  net.minecraftforge.event.entity.living.EnderManAngerEvent
 *  net.minecraftforge.event.entity.living.LivingDropsEvent
 *  net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingTickEvent
 *  net.minecraftforge.event.entity.living.LootingLevelEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$Clone
 *  net.minecraftforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$StartTracking
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.minecraftforge.event.entity.player.PlayerXpEvent$PickupXp
 *  net.minecraftforge.event.level.BlockEvent$BreakEvent
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.eventbus.api.Event$Result
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.common.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;
import top.theillusivec4.curios.common.capability.ItemizedCurioCapability;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketSetIcons;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncCurios;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncData;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncModifiers;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncStack;
import top.theillusivec4.curios.common.util.EquipCurioTrigger;
import top.theillusivec4.curios.mixin.CuriosImplMixinHooks;

public class CuriosEventHandler {
    public static boolean dirtyTags = false;

    private static void handleDrops(String identifier, LivingEntity livingEntity, List<Tuple<Predicate<ItemStack>, ICurio.DropRule>> dropRules, NonNullList<Boolean> renders, IDynamicStackHandler stacks, boolean cosmetic, Collection<ItemEntity> drops, boolean keepInventory, LivingDropsEvent evt) {
        for (int i = 0; i < stacks.getSlots(); ++i) {
            ICurio.DropRule dropRule;
            ItemStack stack = stacks.getStackInSlot(i);
            SlotContext slotContext = new SlotContext(identifier, livingEntity, i, cosmetic, renders.size() > i && (Boolean)renders.get(i) != false);
            if (stack.m_41619_()) continue;
            ICurio.DropRule dropRuleOverride = null;
            for (Tuple<Predicate<ItemStack>, ICurio.DropRule> override : dropRules) {
                if (!((Predicate)override.m_14418_()).test(stack)) continue;
                dropRuleOverride = (ICurio.DropRule)((Object)override.m_14419_());
            }
            ICurio.DropRule dropRule2 = dropRule = dropRuleOverride != null ? dropRuleOverride : CuriosApi.getCurio(stack).map(curio -> curio.getDropRule(slotContext, evt.getSource(), evt.getLootingLevel(), evt.isRecentlyHit())).orElse(ICurio.DropRule.DEFAULT);
            if (dropRule == ICurio.DropRule.DEFAULT) {
                dropRule = CuriosApi.getSlot(identifier, livingEntity.m_9236_()).map(ISlotType::getDropRule).orElse(ICurio.DropRule.DEFAULT);
            }
            if (dropRule == ICurio.DropRule.DEFAULT && keepInventory || dropRule == ICurio.DropRule.ALWAYS_KEEP) continue;
            if (!EnchantmentHelper.m_44924_((ItemStack)stack) && dropRule != ICurio.DropRule.DESTROY) {
                drops.add(CuriosEventHandler.getDroppedItem(stack, livingEntity));
            }
            stacks.setStackInSlot(i, ItemStack.f_41583_);
        }
    }

    private static ItemEntity getDroppedItem(ItemStack droppedItem, LivingEntity livingEntity) {
        double d0 = livingEntity.m_20186_() - (double)0.3f + (double)livingEntity.m_20192_();
        ItemEntity entityitem = new ItemEntity(livingEntity.m_9236_(), livingEntity.m_20185_(), d0, livingEntity.m_20189_(), droppedItem);
        entityitem.m_32010_(40);
        float f = livingEntity.m_9236_().f_46441_.m_188501_() * 0.5f;
        float f1 = livingEntity.m_9236_().f_46441_.m_188501_() * ((float)Math.PI * 2);
        entityitem.m_20334_((double)(-Mth.m_14031_((float)f1) * f), (double)0.2f, (double)(Mth.m_14089_((float)f1) * f));
        return entityitem;
    }

    private static boolean handleMending(Player player, IDynamicStackHandler stacks, PlayerXpEvent.PickupXp evt) {
        for (int i = 0; i < stacks.getSlots(); ++i) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (stack.m_41619_() || stack.getEnchantmentLevel(Enchantments.f_44962_) <= 0 || !stack.m_41768_()) continue;
            evt.setCanceled(true);
            ExperienceOrb orb = evt.getOrb();
            player.f_36101_ = 2;
            player.m_7938_((Entity)orb, 1);
            int toRepair = Math.min(orb.f_20770_ * 2, stack.m_41773_());
            orb.f_20770_ -= toRepair / 2;
            stack.m_41721_(stack.m_41773_() - toRepair);
            if (orb.f_20770_ > 0) {
                player.m_6756_(orb.f_20770_);
            }
            orb.m_142687_(Entity.RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        Player playerEntity = evt.getEntity();
        if (playerEntity instanceof ServerPlayer) {
            Collection<ISlotType> slotTypes = CuriosApi.getPlayerSlots(playerEntity).values();
            HashMap<String, ResourceLocation> icons = new HashMap<String, ResourceLocation>();
            slotTypes.forEach(type -> icons.put(type.getIdentifier(), type.getIcon()));
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)playerEntity), (Object)new SPacketSetIcons(icons));
        }
    }

    @SubscribeEvent
    public void onDatapackSync(OnDatapackSyncEvent evt) {
        if (evt.getPlayer() == null) {
            PlayerList playerList = evt.getPlayerList();
            for (ServerPlayer player : playerList.m_11314_()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), (Object)new SPacketSyncData(CuriosSlotManager.getSyncPacket(), CuriosEntityManager.getSyncPacket()));
                CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(handler -> {
                    Tag tag = handler.writeTag();
                    for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                        ICurioStacksHandler stacks = entry.getValue();
                        for (int i = 0; i < stacks.getSlots(); ++i) {
                            stacks.getStacks().setStackInSlot(i, ItemStack.f_41583_);
                            stacks.getCosmeticStacks().setStackInSlot(i, ItemStack.f_41583_);
                        }
                    }
                    handler.readTag(tag);
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), (Object)new SPacketSyncCurios(player.m_19879_(), handler.getCurios()));
                    AbstractContainerMenu patt10798$temp = player.f_36096_;
                    if (patt10798$temp instanceof ICuriosMenu) {
                        ICuriosMenu curiosContainer = (ICuriosMenu)patt10798$temp;
                        curiosContainer.resetSlots();
                    }
                });
                Collection<ISlotType> slotTypes = CuriosApi.getPlayerSlots((Player)player).values();
                HashMap<String, ResourceLocation> icons = new HashMap<String, ResourceLocation>();
                slotTypes.forEach(type -> icons.put(type.getIdentifier(), type.getIcon()));
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), (Object)new SPacketSetIcons(icons));
            }
        } else {
            ServerPlayer mp = evt.getPlayer();
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), (Object)new SPacketSyncData(CuriosSlotManager.getSyncPacket(), CuriosEntityManager.getSyncPacket()));
            CuriosApi.getCuriosInventory((LivingEntity)mp).ifPresent(handler -> NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), (Object)new SPacketSyncCurios(mp.m_19879_(), handler.getCurios())));
            Collection<ISlotType> slotTypes = CuriosApi.getPlayerSlots((Player)mp).values();
            HashMap<String, ResourceLocation> icons = new HashMap<String, ResourceLocation>();
            slotTypes.forEach(type -> icons.put(type.getIdentifier(), type.getIcon()));
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), (Object)new SPacketSetIcons(icons));
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinLevelEvent evt) {
        Entity entity = evt.getEntity();
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer)entity;
            CuriosApi.getCuriosInventory((LivingEntity)serverPlayerEntity).ifPresent(handler -> {
                ServerPlayer mp = (ServerPlayer)entity;
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), (Object)new SPacketSyncCurios(mp.m_19879_(), handler.getCurios()));
            });
        }
    }

    @SubscribeEvent
    public void attachEntitiesCapabilities(AttachCapabilitiesEvent<Entity> evt) {
        Object object = evt.getObject();
        if (object instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)object;
            evt.addCapability(CuriosCapability.ID_INVENTORY, CurioInventoryCapability.createProvider(livingEntity));
        }
    }

    @SubscribeEvent
    public void attachStackCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = (ItemStack)evt.getObject();
        Item item = stack.m_41720_();
        ICurioItem curioItem = CuriosImplMixinHooks.getCurioFromRegistry(item).orElse(null);
        if (curioItem == null && item instanceof ICurioItem) {
            ICurioItem itemCurio;
            curioItem = itemCurio = (ICurioItem)item;
        }
        if (curioItem != null && curioItem.hasCurioCapability(stack)) {
            ItemizedCurioCapability itemizedCapability = new ItemizedCurioCapability(curioItem, stack);
            evt.addCapability(CuriosCapability.ID_ITEM, CuriosApi.createCurioProvider(itemizedCapability));
        }
    }

    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking evt) {
        Entity target = evt.getTarget();
        Player player = evt.getEntity();
        if (player instanceof ServerPlayer && target instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity)target;
            CuriosApi.getCuriosInventory(livingBase).ifPresent(handler -> NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), (Object)new SPacketSyncCurios(target.m_19879_(), handler.getCurios())));
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone evt) {
        Player player = evt.getEntity();
        Player oldPlayer = evt.getOriginal();
        oldPlayer.revive();
        LazyOptional<ICuriosItemHandler> oldHandler = CuriosApi.getCuriosInventory((LivingEntity)oldPlayer);
        LazyOptional<ICuriosItemHandler> newHandler = CuriosApi.getCuriosInventory((LivingEntity)player);
        oldHandler.ifPresent(oldCurios -> newHandler.ifPresent(newCurios -> newCurios.readTag(oldCurios.writeTag())));
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void playerDrops(LivingDropsEvent evt) {
        LivingEntity livingEntity = evt.getEntity();
        if (!livingEntity.m_5833_()) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                Collection drops = evt.getDrops();
                ArrayList<ItemEntity> curioDrops = new ArrayList<ItemEntity>();
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                DropRulesEvent dropRulesEvent = new DropRulesEvent(livingEntity, (ICuriosItemHandler)handler, evt.getSource(), evt.getLootingLevel(), evt.isRecentlyHit());
                MinecraftForge.EVENT_BUS.post((Event)dropRulesEvent);
                ImmutableList<Tuple<Predicate<ItemStack>, ICurio.DropRule>> dropRules = dropRulesEvent.getOverrides();
                boolean keepInventory = false;
                if (livingEntity instanceof Player) {
                    keepInventory = livingEntity.m_9236_().m_46469_().m_46207_(GameRules.f_46133_);
                    if (CuriosConfig.SERVER.keepCurios.get() != CuriosConfig.KeepCurios.DEFAULT) {
                        keepInventory = CuriosConfig.SERVER.keepCurios.get() == CuriosConfig.KeepCurios.ON;
                    }
                }
                boolean finalKeepInventory = keepInventory;
                curios.forEach((id, stacksHandler) -> {
                    CuriosEventHandler.handleDrops(id, livingEntity, dropRules, stacksHandler.getRenders(), stacksHandler.getStacks(), false, curioDrops, finalKeepInventory, evt);
                    CuriosEventHandler.handleDrops(id, livingEntity, dropRules, stacksHandler.getRenders(), stacksHandler.getCosmeticStacks(), true, curioDrops, finalKeepInventory, evt);
                });
                if (!MinecraftForge.EVENT_BUS.post((Event)new CurioDropsEvent(livingEntity, (ICuriosItemHandler)handler, evt.getSource(), (Collection<ItemEntity>)curioDrops, evt.getLootingLevel(), evt.isRecentlyHit()))) {
                    drops.addAll(curioDrops);
                }
            });
        }
    }

    @SubscribeEvent
    public void playerXPPickUp(PlayerXpEvent.PickupXp evt) {
        Player player = evt.getEntity();
        if (!player.m_9236_().f_46443_) {
            CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                for (ICurioStacksHandler stacksHandler : curios.values()) {
                    if (!CuriosEventHandler.handleMending(player, stacksHandler.getStacks(), evt) && !CuriosEventHandler.handleMending(player, stacksHandler.getCosmeticStacks(), evt)) continue;
                    return;
                }
            });
        }
    }

    @SubscribeEvent
    public void curioRightClick(PlayerInteractEvent.RightClickItem evt) {
        Player player = evt.getEntity();
        ItemStack stack = evt.getItemStack();
        CuriosApi.getCurio(stack).ifPresent(curio -> CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            Tuple firstSlot = null;
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();
                for (int i = 0; i < stackHandler.getSlots(); ++i) {
                    String id = entry.getKey();
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    SlotContext slotContext = new SlotContext(id, (LivingEntity)player, i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false);
                    if (!stackHandler.isItemValid(i, stack) || !curio.canEquipFromUse(slotContext)) continue;
                    ItemStack present = stackHandler.getStackInSlot(i);
                    if (present.m_41619_()) {
                        stackHandler.setStackInSlot(i, stack.m_41777_());
                        curio.onEquipFromUse(slotContext);
                        if (!player.m_7500_()) {
                            int count = stack.m_41613_();
                            stack.m_41774_(count);
                        }
                        evt.setCancellationResult(InteractionResult.m_19078_((boolean)player.m_9236_().m_5776_()));
                        evt.setCanceled(true);
                        return;
                    }
                    if (firstSlot != null) continue;
                    CurioUnequipEvent unequipEvent = new CurioUnequipEvent(present, slotContext);
                    MinecraftForge.EVENT_BUS.post((Event)unequipEvent);
                    Event.Result result = unequipEvent.getResult();
                    if (result == Event.Result.DENY || stackHandler.extractItem(i, stack.m_41741_(), true).m_41613_() != stack.m_41613_()) continue;
                    firstSlot = new Tuple((Object)stackHandler, (Object)slotContext);
                }
            }
            if (firstSlot != null) {
                IDynamicStackHandler stackHandler = (IDynamicStackHandler)firstSlot.m_14418_();
                SlotContext slotContext = (SlotContext)firstSlot.m_14419_();
                int i = slotContext.index();
                ItemStack present = stackHandler.getStackInSlot(i);
                stackHandler.setStackInSlot(i, stack.m_41777_());
                curio.onEquipFromUse(slotContext);
                player.m_21008_(evt.getHand(), present.m_41777_());
                evt.setCancellationResult(InteractionResult.m_19078_((boolean)player.m_9236_().m_5776_()));
                evt.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent
    public void looting(LootingLevelEvent evt) {
        Entity entity;
        DamageSource source = evt.getDamageSource();
        if (source != null && (entity = source.m_7639_()) instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            evt.setLootingLevel(evt.getLootingLevel() + CuriosApi.getCuriosInventory(living).map(handler -> handler.getLootingLevel(source, evt.getEntity(), evt.getLootingLevel())).orElse(0));
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onBreakBlock(BlockEvent.BreakEvent evt) {
        Player player = evt.getPlayer();
        AtomicInteger fortuneLevel = new AtomicInteger();
        CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    NonNullList<Boolean> renderStates;
                    SlotContext slotContext = new SlotContext(entry.getKey(), (LivingEntity)player, i, false, (renderStates = entry.getValue().getRenders()).size() > i && (Boolean)renderStates.get(i) != false);
                    fortuneLevel.addAndGet(CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> curio.getFortuneLevel(slotContext, null)).orElse(0));
                }
            }
        });
        ItemStack stack = player.m_21205_();
        int bonusLevel = stack.getEnchantmentLevel(Enchantments.f_44987_);
        int silklevel = stack.getEnchantmentLevel(Enchantments.f_44985_);
        LevelAccessor level = evt.getLevel();
        evt.setExpToDrop(evt.getState().getExpDrop((LevelReader)level, level.m_213780_(), evt.getPos(), bonusLevel + fortuneLevel.get(), silklevel));
    }

    @SubscribeEvent
    public void enderManAnger(EnderManAngerEvent evt) {
        Player player = evt.getPlayer();
        CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(handler -> {
            block0: for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    int index = i;
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    boolean hasMask = CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> curio.isEnderMask(new SlotContext((String)entry.getKey(), (LivingEntity)player, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false), evt.getEntity())).orElse(false);
                    if (!hasMask) continue;
                    evt.setCanceled(true);
                    break block0;
                }
            }
        });
    }

    @SubscribeEvent
    public void tick(LivingEvent.LivingTickEvent evt) {
        LivingEntity livingEntity = evt.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            AbstractContainerMenu abstractContainerMenu = player.f_36096_;
            if (abstractContainerMenu instanceof CuriosContainerV2) {
                CuriosContainerV2 curiosContainer = (CuriosContainerV2)abstractContainerMenu;
                curiosContainer.checkQuickMove();
            }
        }
        CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
            Set<ICurioStacksHandler> updates;
            handler.clearCachedSlotModifiers();
            handler.handleInvalidStacks();
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                ICurioStacksHandler stacksHandler = entry.getValue();
                String identifier = entry.getKey();
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                IDynamicStackHandler cosmeticStackHandler = stacksHandler.getCosmeticStacks();
                for (int i = 0; i < stacksHandler.getSlots(); ++i) {
                    ItemStack prevCosmeticStack;
                    ItemStack cosmeticStack;
                    NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                    SlotContext slotContext = new SlotContext(identifier, livingEntity, i, false, renderStates.size() > i && (Boolean)renderStates.get(i) != false);
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    LazyOptional<ICurio> currentCurio = CuriosApi.getCurio(stack);
                    int index = i;
                    if (!stack.m_41619_()) {
                        stack.m_41666_(livingEntity.m_9236_(), (Entity)livingEntity, -1, false);
                        currentCurio.ifPresent(curio -> curio.curioTick(slotContext));
                        if (livingEntity.m_9236_().f_46443_) {
                            currentCurio.ifPresent(curio -> curio.curioAnimate(identifier, index, livingEntity));
                        }
                    }
                    if (livingEntity.m_9236_().f_46443_) continue;
                    ItemStack prevStack = stackHandler.getPreviousStackInSlot(i);
                    if (!ItemStack.m_41728_((ItemStack)stack, (ItemStack)prevStack)) {
                        SlotAttribute wrapper;
                        HashSet<SlotAttribute> toRemove;
                        HashMultimap slots;
                        Multimap<Attribute, AttributeModifier> map;
                        LazyOptional<ICurio> prevCurio = CuriosApi.getCurio(prevStack);
                        CuriosEventHandler.syncCurios(livingEntity, stack, currentCurio, prevCurio, identifier, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false, SPacketSyncStack.HandlerType.EQUIPMENT);
                        MinecraftForge.EVENT_BUS.post((Event)new CurioChangeEvent(livingEntity, identifier, i, prevStack, stack));
                        UUID uuid = CuriosApi.getSlotUuid(slotContext);
                        if (!prevStack.m_41619_()) {
                            map = CuriosApi.getAttributeModifiers(slotContext, uuid, prevStack);
                            slots = HashMultimap.create();
                            toRemove = new HashSet<SlotAttribute>();
                            for (Attribute attribute : map.keySet()) {
                                if (!(attribute instanceof SlotAttribute)) continue;
                                wrapper = (SlotAttribute)attribute;
                                slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                                toRemove.add(wrapper);
                            }
                            for (Attribute attribute : toRemove) {
                                map.removeAll((Object)attribute);
                            }
                            livingEntity.m_21204_().m_22161_(map);
                            handler.removeSlotModifiers((Multimap<String, AttributeModifier>)slots);
                            prevCurio.ifPresent(curio -> curio.onUnequip(slotContext, stack));
                        }
                        if (!stack.m_41619_()) {
                            map = CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
                            slots = HashMultimap.create();
                            toRemove = new HashSet();
                            for (Attribute attribute : map.keySet()) {
                                if (!(attribute instanceof SlotAttribute)) continue;
                                wrapper = (SlotAttribute)attribute;
                                slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                                toRemove.add(wrapper);
                            }
                            for (Attribute attribute : toRemove) {
                                map.removeAll((Object)attribute);
                            }
                            livingEntity.m_21204_().m_22178_(map);
                            handler.addTransientSlotModifiers((Multimap<String, AttributeModifier>)slots);
                            currentCurio.ifPresent(curio -> curio.onEquip(slotContext, prevStack));
                            if (livingEntity instanceof ServerPlayer) {
                                EquipCurioTrigger.INSTANCE.trigger(slotContext, (ServerPlayer)livingEntity, stack, (ServerLevel)livingEntity.m_9236_(), livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_());
                            }
                        }
                        stackHandler.setPreviousStackInSlot(i, stack.m_41777_());
                    }
                    if (ItemStack.m_41728_((ItemStack)(cosmeticStack = cosmeticStackHandler.getStackInSlot(i)), (ItemStack)(prevCosmeticStack = cosmeticStackHandler.getPreviousStackInSlot(i)))) continue;
                    CuriosEventHandler.syncCurios(livingEntity, cosmeticStack, CuriosApi.getCurio(cosmeticStack), CuriosApi.getCurio(prevCosmeticStack), identifier, index, true, true, SPacketSyncStack.HandlerType.COSMETIC);
                    cosmeticStackHandler.setPreviousStackInSlot(index, cosmeticStack.m_41777_());
                }
            }
            if (!livingEntity.m_9236_().m_5776_() && !(updates = handler.getUpdatingInventories()).isEmpty()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), (Object)new SPacketSyncModifiers(livingEntity.m_19879_(), updates));
                updates.clear();
            }
        });
    }

    @SubscribeEvent
    public void livingEquipmentChange(LivingEquipmentChangeEvent evt) {
        CuriosApi.getCuriosInventory(evt.getEntity()).ifPresent(inv -> {
            SlotAttribute wrapper;
            HashMultimap slots;
            Multimap map;
            ItemStack from = evt.getFrom();
            ItemStack to = evt.getTo();
            EquipmentSlot slot = evt.getSlot();
            if (!from.m_41619_()) {
                map = from.m_41638_(slot);
                slots = HashMultimap.create();
                for (Attribute attribute : map.keySet()) {
                    if (!(attribute instanceof SlotAttribute)) continue;
                    wrapper = (SlotAttribute)attribute;
                    slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                }
                inv.removeSlotModifiers((Multimap<String, AttributeModifier>)slots);
            }
            if (!to.m_41619_()) {
                map = to.m_41638_(slot);
                slots = HashMultimap.create();
                for (Attribute attribute : map.keySet()) {
                    if (!(attribute instanceof SlotAttribute)) continue;
                    wrapper = (SlotAttribute)attribute;
                    slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                }
                inv.addTransientSlotModifiers((Multimap<String, AttributeModifier>)slots);
            }
        });
    }

    private static void syncCurios(LivingEntity livingEntity, ItemStack stack, LazyOptional<ICurio> currentCurio, LazyOptional<ICurio> prevCurio, String identifier, int index, boolean cosmetic, boolean visible, SPacketSyncStack.HandlerType type) {
        SlotContext slotContext = new SlotContext(identifier, livingEntity, index, cosmetic, visible);
        boolean syncable = currentCurio.map(curio -> curio.canSync(slotContext)).orElse(false) != false || prevCurio.map(curio -> curio.canSync(slotContext)).orElse(false) != false;
        CompoundTag syncTag = syncable ? currentCurio.map(curio -> {
            CompoundTag tag = curio.writeSyncData(slotContext);
            return tag != null ? tag : new CompoundTag();
        }).orElse(new CompoundTag()) : new CompoundTag();
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), (Object)new SPacketSyncStack(livingEntity.m_19879_(), identifier, index, stack, type, syncTag));
    }
}

