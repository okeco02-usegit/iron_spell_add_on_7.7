/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  javax.annotation.Nonnull
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlot$Type
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.player.StackedContents
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.CraftingContainer
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.RecipeBookType
 *  net.minecraft.world.inventory.ResultContainer
 *  net.minecraft.world.inventory.ResultSlot
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.inventory.TransientCraftingContainer
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.CraftingRecipe
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.common.inventory.container;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.CuriosRegistry;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketScroll;
import top.theillusivec4.curios.common.network.server.SPacketScroll;

public class CuriosContainer
extends InventoryMenu
implements ICuriosMenu {
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{InventoryMenu.f_39696_, InventoryMenu.f_39695_, InventoryMenu.f_39694_, InventoryMenu.f_39693_};
    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public final LazyOptional<ICuriosItemHandler> curiosHandler;
    public final Player player;
    private final boolean isLocalWorld;
    private final CraftingContainer craftMatrix = new TransientCraftingContainer((AbstractContainerMenu)this, 2, 2);
    private final ResultContainer craftResult = new ResultContainer();
    private int lastScrollIndex;
    private boolean cosmeticColumn;
    private boolean skip = false;

    public CuriosContainer(int windowId, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(windowId, playerInventory);
    }

    public CuriosContainer(int windowId, Inventory playerInventory) {
        this(windowId, playerInventory, false);
    }

    public CuriosContainer(int windowId, Inventory playerInventory, boolean skip) {
        super(playerInventory, playerInventory.f_35978_.m_9236_().f_46443_, playerInventory.f_35978_);
        this.f_38843_ = (MenuType)CuriosRegistry.CURIO_MENU.get();
        this.f_38840_ = windowId;
        this.f_150394_.clear();
        this.f_38841_.clear();
        this.f_38839_.clear();
        this.player = playerInventory.f_35978_;
        this.isLocalWorld = this.player.m_9236_().f_46443_;
        this.curiosHandler = CuriosApi.getCuriosInventory((LivingEntity)this.player);
        if (skip) {
            this.skip = true;
            return;
        }
        this.m_38897_((Slot)new ResultSlot(playerInventory.f_35978_, this.craftMatrix, (Container)this.craftResult, 0, 154, 28));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.m_38897_(new Slot((Container)this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.m_38897_(new Slot((Container)playerInventory, 36 + (3 - k), 8, 8 + k * 18){

                public void m_5852_(@Nonnull ItemStack stack) {
                    ItemStack itemstack = this.m_7993_();
                    super.m_5852_(stack);
                    CuriosContainer.this.player.m_238392_(equipmentslottype, itemstack, stack);
                }

                public int m_6641_() {
                    return 1;
                }

                public boolean m_5857_(@Nonnull ItemStack stack) {
                    return stack.canEquip(equipmentslottype, (Entity)CuriosContainer.this.player);
                }

                public boolean m_8010_(@Nonnull Player playerIn) {
                    ItemStack itemstack = this.m_7993_();
                    return (itemstack.m_41619_() || playerIn.m_7500_() || !EnchantmentHelper.m_44920_((ItemStack)itemstack)) && super.m_8010_(playerIn);
                }

                @OnlyIn(value=Dist.CLIENT)
                public Pair<ResourceLocation, ResourceLocation> m_7543_() {
                    return Pair.of((Object)InventoryMenu.f_39692_, (Object)ARMOR_SLOT_TEXTURES[equipmentslottype.m_20749_()]);
                }
            });
        }
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.m_38897_(new Slot((Container)playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.m_38897_(new Slot((Container)playerInventory, i1, 8 + i1 * 18, 142));
        }
        this.m_38897_(new Slot((Container)playerInventory, 40, 77, 62){

            @OnlyIn(value=Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> m_7543_() {
                return Pair.of((Object)InventoryMenu.f_39692_, (Object)InventoryMenu.f_39697_);
            }
        });
        this.curiosHandler.ifPresent(curios -> {
            int i;
            IDynamicStackHandler stackHandler;
            ICurioStacksHandler stacksHandler;
            Map<String, ICurioStacksHandler> curioMap = curios.getCurios();
            int slots = 0;
            int yOffset = 12;
            for (String identifier : curioMap.keySet()) {
                stacksHandler = curioMap.get(identifier);
                stackHandler = stacksHandler.getStacks();
                if (!stacksHandler.isVisible()) continue;
                for (i = 0; i < stackHandler.getSlots() && slots < 8; ++slots, ++i) {
                    this.m_38897_((Slot)new CurioSlot(this.player, stackHandler, i, identifier, -18, yOffset, stacksHandler.getRenders(), stacksHandler.canToggleRendering()));
                    yOffset += 18;
                }
            }
            yOffset = 12;
            slots = 0;
            for (String identifier : curioMap.keySet()) {
                stacksHandler = curioMap.get(identifier);
                stackHandler = stacksHandler.getStacks();
                if (!stacksHandler.isVisible()) continue;
                for (i = 0; i < stackHandler.getSlots() && slots < 8; ++slots, ++i) {
                    if (stacksHandler.hasCosmetic()) {
                        IDynamicStackHandler cosmeticHandler = stacksHandler.getCosmeticStacks();
                        this.cosmeticColumn = true;
                        this.m_38897_((Slot)new CosmeticCurioSlot(this.player, cosmeticHandler, i, identifier, -37, yOffset));
                    }
                    yOffset += 18;
                }
            }
        });
        this.scrollToIndex(0);
    }

    public boolean hasCosmeticColumn() {
        return this.cosmeticColumn;
    }

    @Override
    public void resetSlots() {
        this.scrollToIndex(this.lastScrollIndex);
    }

    public void scrollToIndex(int indexIn) {
        this.curiosHandler.ifPresent(curios -> {
            int i;
            IDynamicStackHandler stackHandler;
            ICurioStacksHandler stacksHandler;
            Map<String, ICurioStacksHandler> curioMap = curios.getCurios();
            int slots = 0;
            int yOffset = 12;
            int index = 0;
            int startingIndex = indexIn;
            this.f_38839_.subList(46, this.f_38839_.size()).clear();
            this.f_38841_.subList(46, this.f_38841_.size()).clear();
            this.f_150394_.subList(46, this.f_150394_.size()).clear();
            for (String identifier : curioMap.keySet()) {
                stacksHandler = curioMap.get(identifier);
                stackHandler = stacksHandler.getStacks();
                if (!stacksHandler.isVisible()) continue;
                for (i = 0; i < stackHandler.getSlots() && slots < 8; ++i) {
                    if (index >= startingIndex) {
                        ++slots;
                    }
                    ++index;
                }
            }
            startingIndex = Math.min(startingIndex, Math.max(0, index - 8));
            index = 0;
            slots = 0;
            for (String identifier : curioMap.keySet()) {
                stacksHandler = curioMap.get(identifier);
                stackHandler = stacksHandler.getStacks();
                if (!stacksHandler.isVisible()) continue;
                for (i = 0; i < stackHandler.getSlots() && slots < 8; ++i) {
                    if (index >= startingIndex) {
                        this.m_38897_((Slot)new CurioSlot(this.player, stackHandler, i, identifier, -18, yOffset, stacksHandler.getRenders(), stacksHandler.canToggleRendering()));
                        yOffset += 18;
                        ++slots;
                    }
                    ++index;
                }
            }
            index = 0;
            slots = 0;
            yOffset = 12;
            for (String identifier : curioMap.keySet()) {
                stacksHandler = curioMap.get(identifier);
                stackHandler = stacksHandler.getStacks();
                if (!stacksHandler.isVisible()) continue;
                for (i = 0; i < stackHandler.getSlots() && slots < 8; ++i) {
                    if (index >= startingIndex) {
                        if (stacksHandler.hasCosmetic()) {
                            IDynamicStackHandler cosmeticHandler = stacksHandler.getCosmeticStacks();
                            this.cosmeticColumn = true;
                            this.m_38897_((Slot)new CosmeticCurioSlot(this.player, cosmeticHandler, i, identifier, -37, yOffset));
                        }
                        yOffset += 18;
                        ++slots;
                    }
                    ++index;
                }
            }
            if (!this.isLocalWorld) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)this.player), (Object)new SPacketScroll(this.f_38840_, indexIn));
            }
            this.lastScrollIndex = indexIn;
        });
    }

    public void scrollTo(float pos) {
        this.curiosHandler.ifPresent(curios -> {
            int k = curios.getVisibleSlots() - 8;
            int j = (int)((double)(pos * (float)k) + 0.5);
            if (j < 0) {
                j = 0;
            }
            if (j == this.lastScrollIndex) {
                return;
            }
            if (this.isLocalWorld) {
                NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketScroll(this.f_38840_, j));
            }
        });
    }

    public void m_6199_(@Nonnull Container inventoryIn) {
        if (!this.player.m_9236_().f_46443_) {
            ServerPlayer playerMP = (ServerPlayer)this.player;
            ItemStack stack = ItemStack.f_41583_;
            MinecraftServer server = this.player.m_9236_().m_7654_();
            if (server == null) {
                return;
            }
            Optional recipe = server.m_129894_().m_44015_(RecipeType.f_44107_, (Container)this.craftMatrix, this.player.m_9236_());
            if (recipe.isPresent()) {
                CraftingRecipe craftingRecipe = (CraftingRecipe)recipe.get();
                if (this.craftResult.m_40135_(this.player.m_9236_(), playerMP, (Recipe)craftingRecipe)) {
                    stack = craftingRecipe.m_5874_((Container)this.craftMatrix, this.player.m_9236_().m_9598_());
                }
            }
            this.craftResult.m_6836_(0, stack);
            this.m_150404_(0, stack);
            playerMP.f_8906_.m_9829_((Packet)new ClientboundContainerSetSlotPacket(this.f_38840_, this.m_182425_(), 0, stack));
        }
    }

    public void m_6877_(@Nonnull Player playerIn) {
        super.m_6877_(playerIn);
        if (this.skip) {
            return;
        }
        this.craftResult.m_6211_();
        if (!playerIn.m_9236_().f_46443_) {
            this.m_150411_(playerIn, (Container)this.craftMatrix);
        }
    }

    public boolean canScroll() {
        return this.curiosHandler.map(curios -> {
            if (curios.getVisibleSlots() > 8) {
                return 1;
            }
            return 0;
        }).orElse(0) == 1;
    }

    public boolean m_6875_(@Nonnull Player playerIn) {
        return true;
    }

    public void m_182406_(int pSlotId, int pStateId, @Nonnull ItemStack pStack) {
        if (this.skip) {
            super.m_182406_(pSlotId, pStateId, pStack);
            return;
        }
        if (this.f_38839_.size() > pSlotId) {
            super.m_182406_(pSlotId, pStateId, pStack);
        }
    }

    @Nonnull
    public ItemStack m_7648_(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.f_41583_;
        Slot slot = (Slot)this.f_38839_.get(index);
        if (slot.m_6657_()) {
            int i;
            ItemStack itemstack1 = slot.m_7993_();
            itemstack = itemstack1.m_41777_();
            EquipmentSlot entityequipmentslot = Mob.m_147233_((ItemStack)itemstack);
            if (index == 0) {
                if (!this.m_38903_(itemstack1, 9, 45, true)) {
                    return ItemStack.f_41583_;
                }
                slot.m_40234_(itemstack1, itemstack);
            } else if (index < 5 ? !this.m_38903_(itemstack1, 9, 45, false) : (index < 9 ? !this.m_38903_(itemstack1, 9, 45, false) : (entityequipmentslot.m_20743_() == EquipmentSlot.Type.ARMOR && !((Slot)this.f_38839_.get(8 - entityequipmentslot.m_20749_())).m_6657_() ? !this.m_38903_(itemstack1, i = 8 - entityequipmentslot.m_20749_(), i + 1, false) : (index < 46 && !CuriosApi.getItemStackSlots(itemstack, playerIn.m_9236_()).isEmpty() ? !this.m_38903_(itemstack1, 46, this.f_38839_.size(), false) : (entityequipmentslot == EquipmentSlot.OFFHAND && !((Slot)this.f_38839_.get(45)).m_6657_() ? !this.m_38903_(itemstack1, 45, 46, false) : (index < 36 ? !this.m_38903_(itemstack1, 36, 45, false) : (index < 45 ? !this.m_38903_(itemstack1, 9, 36, false) : !this.m_38903_(itemstack1, 9, 45, false)))))))) {
                return ItemStack.f_41583_;
            }
            if (itemstack1.m_41619_()) {
                slot.m_5852_(ItemStack.f_41583_);
            } else {
                slot.m_6654_();
            }
            if (itemstack1.m_41613_() == itemstack.m_41613_()) {
                return ItemStack.f_41583_;
            }
            slot.m_142406_(playerIn, itemstack1);
            if (index == 0) {
                playerIn.m_36176_(itemstack1, false);
            }
        }
        return itemstack;
    }

    @Nonnull
    public RecipeBookType m_5867_() {
        return RecipeBookType.CRAFTING;
    }

    public void m_5816_(@Nonnull StackedContents itemHelperIn) {
        this.craftMatrix.m_5809_(itemHelperIn);
    }

    public void m_6650_() {
        this.craftMatrix.m_6211_();
        this.craftResult.m_6211_();
    }

    public boolean m_6032_(Recipe<? super CraftingContainer> recipeIn) {
        return recipeIn.m_5818_((Container)this.craftMatrix, this.player.m_9236_());
    }

    public int m_6635_() {
        return this.craftMatrix.m_39347_();
    }

    public int m_6656_() {
        return this.craftMatrix.m_39346_();
    }
}

