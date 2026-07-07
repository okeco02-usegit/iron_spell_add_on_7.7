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
 *  net.minecraft.util.Mth
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
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.common.inventory.container;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.CuriosRegistry;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketPage;
import top.theillusivec4.curios.common.network.server.SPacketQuickMove;

public class CuriosContainerV2
extends CuriosContainer {
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{InventoryMenu.f_39696_, InventoryMenu.f_39695_, InventoryMenu.f_39694_, InventoryMenu.f_39693_};
    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public final ICuriosItemHandler curiosHandler;
    public final Player player;
    private final boolean isLocalWorld;
    private final CraftingContainer craftMatrix = new TransientCraftingContainer((AbstractContainerMenu)this, 2, 2);
    private final ResultContainer craftResult = new ResultContainer();
    public int currentPage;
    public int totalPages;
    public List<Integer> grid = new ArrayList<Integer>();
    private final List<ProxySlot> proxySlots = new ArrayList<ProxySlot>();
    private int moveToPage = -1;
    private int moveFromIndex = -1;
    public boolean hasCosmetics;
    public boolean isViewingCosmetics;
    public int panelWidth;

    public CuriosContainerV2(int windowId, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(windowId, playerInventory);
    }

    public CuriosContainerV2(int windowId, Inventory playerInventory) {
        super(windowId, playerInventory);
        this.f_38843_ = CuriosRegistry.CURIO_MENU_NEW.get();
        this.player = playerInventory.f_35978_;
        this.isLocalWorld = this.player.m_9236_().f_46443_;
        this.curiosHandler = (ICuriosItemHandler)CuriosApi.getCuriosInventory((LivingEntity)this.player).orElse(null);
        this.resetSlots();
    }

    public void setPage(int page) {
        this.f_38839_.clear();
        this.f_38841_.clear();
        this.f_150394_.clear();
        this.panelWidth = 0;
        int visibleSlots = 0;
        int maxSlotsPerPage = (Integer)CuriosConfig.SERVER.maxSlotsPerPage.get();
        int startingIndex = page * maxSlotsPerPage;
        int columns = 0;
        if (this.curiosHandler != null) {
            visibleSlots = this.curiosHandler.getVisibleSlots();
            int slotsOnPage = Math.min(maxSlotsPerPage, visibleSlots - startingIndex);
            int calculatedColumns = (int)Math.ceil((double)slotsOnPage / 8.0);
            int minimumColumns = Math.min(slotsOnPage, (Integer)CuriosConfig.SERVER.minimumColumns.get());
            columns = Mth.m_14045_((int)calculatedColumns, (int)minimumColumns, (int)8);
            this.panelWidth = 14 + 18 * columns;
        }
        this.m_38897_((Slot)new ResultSlot(this.player, this.craftMatrix, (Container)this.craftResult, 0, 154, 28));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.m_38897_(new Slot((Container)this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.m_38897_(new Slot((Container)this.player.m_150109_(), 36 + (3 - k), 8, 8 + k * 18){

                public void m_5852_(@Nonnull ItemStack stack) {
                    ItemStack itemstack = this.m_7993_();
                    super.m_5852_(stack);
                    CuriosContainerV2.this.player.m_238392_(equipmentslottype, itemstack, stack);
                }

                public int m_6641_() {
                    return 1;
                }

                public boolean m_5857_(@Nonnull ItemStack stack) {
                    return stack.canEquip(equipmentslottype, (Entity)CuriosContainerV2.this.player);
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
                this.m_38897_(new Slot((Container)this.player.m_150109_(), j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.m_38897_(new Slot((Container)this.player.m_150109_(), i1, 8 + i1 * 18, 142));
        }
        this.m_38897_(new Slot((Container)this.player.m_150109_(), 40, 77, 62){

            @OnlyIn(value=Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> m_7543_() {
                return Pair.of((Object)InventoryMenu.f_39692_, (Object)InventoryMenu.f_39697_);
            }
        });
        if (this.curiosHandler != null) {
            Map<String, ICurioStacksHandler> curioMap = this.curiosHandler.getCurios();
            this.totalPages = (int)Math.ceil((double)visibleSlots / (double)maxSlotsPerPage);
            int index = 0;
            int yOffset = 8;
            if (this.totalPages > 1) {
                yOffset += 8;
            }
            int currentColumn = 1;
            int currentRow = 1;
            int slots = 0;
            this.grid.clear();
            this.proxySlots.clear();
            int currentPage = 0;
            int endingIndex = startingIndex + maxSlotsPerPage;
            for (String identifier : curioMap.keySet()) {
                ICurioStacksHandler stacksHandler = curioMap.get(identifier);
                boolean isCosmetic = false;
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                if (stacksHandler.hasCosmetic()) {
                    this.hasCosmetics = true;
                    if (this.isViewingCosmetics) {
                        isCosmetic = true;
                        stackHandler = stacksHandler.getCosmeticStacks();
                    }
                }
                if (!stacksHandler.isVisible()) continue;
                for (int i = 0; i < stackHandler.getSlots(); ++i) {
                    if (index >= startingIndex && index < endingIndex) {
                        if (isCosmetic) {
                            this.m_38897_((Slot)new CurioSlot(this.player, stackHandler, i, identifier, (currentColumn - 1) * 18 + 7 - this.panelWidth, yOffset + (currentRow - 1) * 18, stacksHandler.getRenders(), stacksHandler.canToggleRendering(), true, true));
                        } else {
                            this.m_38897_((Slot)new CurioSlot(this.player, stackHandler, i, identifier, (currentColumn - 1) * 18 + 7 - this.panelWidth, yOffset + (currentRow - 1) * 18, stacksHandler.getRenders(), stacksHandler.canToggleRendering(), false, false));
                        }
                        if (this.grid.size() < currentColumn) {
                            this.grid.add(1);
                        } else {
                            this.grid.set(currentColumn - 1, this.grid.get(currentColumn - 1) + 1);
                        }
                        if (currentColumn == columns) {
                            currentColumn = 1;
                            ++currentRow;
                        } else {
                            ++currentColumn;
                        }
                    } else if (isCosmetic) {
                        this.proxySlots.add(new ProxySlot(currentPage, (Slot)new CurioSlot(this.player, stackHandler, i, identifier, (currentColumn - 1) * 18 + 7 - this.panelWidth, yOffset + (currentRow - 1) * 18, stacksHandler.getRenders(), stacksHandler.canToggleRendering(), true, true)));
                    } else {
                        this.proxySlots.add(new ProxySlot(currentPage, (Slot)new CurioSlot(this.player, stackHandler, i, identifier, (currentColumn - 1) * 18 + 7 - this.panelWidth, yOffset + (currentRow - 1) * 18, stacksHandler.getRenders(), stacksHandler.canToggleRendering(), false, false)));
                    }
                    if (++slots >= maxSlotsPerPage) {
                        slots = 0;
                        ++currentPage;
                    }
                    ++index;
                }
            }
            if (!this.isLocalWorld) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)this.player), (Object)new SPacketPage(this.f_38840_, page));
            }
        }
        this.currentPage = page;
    }

    @Override
    public void resetSlots() {
        this.setPage(this.currentPage);
    }

    public void toggleCosmetics() {
        this.isViewingCosmetics = !this.isViewingCosmetics;
        this.resetSlots();
    }

    @Override
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

    @Override
    public void m_6877_(@Nonnull Player playerIn) {
        super.m_6877_(playerIn);
        this.craftResult.m_6211_();
        if (!playerIn.m_9236_().f_46443_) {
            this.m_150411_(playerIn, (Container)this.craftMatrix);
        }
    }

    @Override
    public void m_182406_(int pSlotId, int pStateId, @Nonnull ItemStack pStack) {
        if (this.f_38839_.size() > pSlotId) {
            super.m_182406_(pSlotId, pStateId, pStack);
        }
    }

    @Override
    public boolean m_6875_(@Nonnull Player player) {
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    @Nonnull
    public ItemStack m_7648_(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.f_41583_;
        Slot slot = (Slot)this.f_38839_.get(index);
        if (!slot.m_6657_()) return itemstack;
        ItemStack itemstack1 = slot.m_7993_();
        itemstack = itemstack1.m_41777_();
        EquipmentSlot entityequipmentslot = Mob.m_147233_((ItemStack)itemstack);
        if (index == 0) {
            if (!this.m_38903_(itemstack1, 9, 45, true)) {
                return ItemStack.f_41583_;
            }
            slot.m_40234_(itemstack1, itemstack);
        } else if (index < 5) {
            if (!this.m_38903_(itemstack1, 9, 45, false)) {
                return ItemStack.f_41583_;
            }
        } else if (index < 9) {
            if (!this.m_38903_(itemstack1, 9, 45, false)) {
                return ItemStack.f_41583_;
            }
        } else if (entityequipmentslot.m_20743_() == EquipmentSlot.Type.ARMOR && !((Slot)this.f_38839_.get(8 - entityequipmentslot.m_20749_())).m_6657_()) {
            int i = 8 - entityequipmentslot.m_20749_();
            if (!this.m_38903_(itemstack1, i, i + 1, false)) {
                return ItemStack.f_41583_;
            }
        } else if (index < 46 && !CuriosApi.getItemStackSlots(itemstack, playerIn.m_9236_()).isEmpty()) {
            if (!this.m_38903_(itemstack1, 46, this.f_38839_.size(), false)) {
                int page = this.findAvailableSlot(itemstack1);
                if (page == -1) return ItemStack.f_41583_;
                this.moveToPage = page;
                this.moveFromIndex = index;
            }
        } else if (entityequipmentslot == EquipmentSlot.OFFHAND && !((Slot)this.f_38839_.get(45)).m_6657_() ? !this.m_38903_(itemstack1, 45, 46, false) : (index < 36 ? !this.m_38903_(itemstack1, 36, 45, false) : (index < 45 ? !this.m_38903_(itemstack1, 9, 36, false) : !this.m_38903_(itemstack1, 9, 45, false)))) {
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
        if (index != 0) return itemstack;
        playerIn.m_36176_(itemstack1, false);
        return itemstack;
    }

    protected int findAvailableSlot(ItemStack stack) {
        int result = -1;
        if (stack.m_41753_()) {
            for (ProxySlot proxySlot : this.proxySlots) {
                int maxSize;
                int j;
                Slot slot = proxySlot.slot();
                ItemStack itemstack = slot.m_7993_();
                if (itemstack.m_41619_() || !ItemStack.m_150942_((ItemStack)stack, (ItemStack)itemstack) || (j = itemstack.m_41613_() + stack.m_41613_()) > (maxSize = Math.min(slot.m_6641_(), stack.m_41741_())) && itemstack.m_41613_() >= maxSize) continue;
                result = proxySlot.page();
                break;
            }
        }
        if (!stack.m_41619_() && result == -1) {
            for (ProxySlot proxySlot : this.proxySlots) {
                Slot slot1 = proxySlot.slot();
                ItemStack itemstack1 = slot1.m_7993_();
                if (!itemstack1.m_41619_() || !slot1.m_5857_(stack)) continue;
                result = proxySlot.page();
                break;
            }
        }
        return result;
    }

    @Override
    @Nonnull
    public RecipeBookType m_5867_() {
        return RecipeBookType.CRAFTING;
    }

    public boolean m_142157_(int index) {
        return index != this.m_6636_();
    }

    @Override
    public void m_5816_(@Nonnull StackedContents itemHelperIn) {
        this.craftMatrix.m_5809_(itemHelperIn);
    }

    @Override
    public void m_6650_() {
        this.craftMatrix.m_6211_();
        this.craftResult.m_6211_();
    }

    @Override
    public boolean m_6032_(Recipe<? super CraftingContainer> recipeHolder) {
        return recipeHolder.m_5818_((Container)this.craftMatrix, this.player.m_9236_());
    }

    public int m_6636_() {
        return 0;
    }

    @Override
    public int m_6635_() {
        return this.craftMatrix.m_39347_();
    }

    @Override
    public int m_6656_() {
        return this.craftMatrix.m_39346_();
    }

    public int m_6653_() {
        return 5;
    }

    public void nextPage() {
        this.setPage(Math.min(this.currentPage + 1, this.totalPages - 1));
    }

    public void prevPage() {
        this.setPage(Math.max(this.currentPage - 1, 0));
    }

    public void checkQuickMove() {
        if (this.moveToPage != -1) {
            this.setPage(this.moveToPage);
            this.m_7648_(this.player, this.moveFromIndex);
            this.moveToPage = -1;
            if (!this.isLocalWorld) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)this.player), (Object)new SPacketQuickMove(this.f_38840_, this.moveFromIndex));
            }
        }
    }

    private record ProxySlot(int page, Slot slot) {
    }
}

