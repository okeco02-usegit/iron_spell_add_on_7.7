/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.systems.RenderSystem
 *  javax.annotation.Nonnull
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.ImageButton
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
 *  net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Tuple
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ClickType
 *  net.minecraft.world.inventory.RecipeBookMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.ICuriosScreen;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.client.KeyRegistry;
import top.theillusivec4.curios.client.gui.CosmeticButton;
import top.theillusivec4.curios.client.gui.CuriosButton;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.PageButton;
import top.theillusivec4.curios.client.gui.RenderButton;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketPage;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

public class CuriosScreenV2
extends EffectRenderingInventoryScreen<CuriosContainerV2>
implements RecipeUpdateListener,
ICuriosScreen {
    static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory_revamp.png");
    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();
    public boolean widthTooNarrow;
    private ImageButton recipeBookButton;
    private CuriosButton buttonCurios;
    private CosmeticButton cosmeticButton;
    private PageButton nextPage;
    private PageButton prevPage;
    private boolean buttonClicked;
    private boolean isRenderButtonHovered;
    public int panelWidth = 0;
    private static int scrollCooldown = 0;

    public CuriosScreenV2(CuriosContainerV2 curiosContainer, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)curiosContainer, playerInventory, title);
    }

    public static Tuple<Integer, Integer> getButtonOffset(boolean isCreative) {
        CuriosClientConfig.Client client = CuriosClientConfig.CLIENT;
        CuriosClientConfig.Client.ButtonCorner corner = (CuriosClientConfig.Client.ButtonCorner)((Object)client.buttonCorner.get());
        int x = 0;
        int y = 0;
        if (isCreative) {
            x += corner.getCreativeXoffset() + (Integer)client.creativeButtonXOffset.get();
            y += corner.getCreativeYoffset() + (Integer)client.creativeButtonYOffset.get();
        } else {
            x += corner.getXoffset() + (Integer)client.buttonXOffset.get();
            y += corner.getYoffset() + (Integer)client.buttonYOffset.get();
        }
        return new Tuple((Object)x, (Object)y);
    }

    public void m_7856_() {
        if (this.f_96541_ != null) {
            this.panelWidth = ((CuriosContainerV2)this.f_97732_).panelWidth;
            this.f_97735_ = (this.f_96543_ - this.f_97726_) / 2;
            this.f_97736_ = (this.f_96544_ - this.f_97727_) / 2;
            this.widthTooNarrow = true;
            this.recipeBookGui.m_100309_(this.f_96543_, this.f_96544_, this.f_96541_, true, (RecipeBookMenu)this.f_97732_);
            this.m_7787_((GuiEventListener)this.recipeBookGui);
            this.m_264313_((GuiEventListener)this.recipeBookGui);
            if (this.getMinecraft().f_91074_ != null && this.getMinecraft().f_91074_.m_7500_() && this.recipeBookGui.m_100385_()) {
                this.recipeBookGui.m_100384_();
            }
            Tuple<Integer, Integer> offsets = CuriosScreenV2.getButtonOffset(false);
            this.buttonCurios = new CuriosButton((AbstractContainerScreen<?>)this, this.getGuiLeft() + (Integer)offsets.m_14418_(), this.f_96544_ / 2 + (Integer)offsets.m_14419_(), 14, 14, 50, 0, 14, CuriosScreen.CURIO_INVENTORY);
            if (((Boolean)CuriosClientConfig.CLIENT.enableButton.get()).booleanValue()) {
                this.m_142416_((GuiEventListener)this.buttonCurios);
            }
            if (!((CuriosContainerV2)this.f_97732_).player.m_7500_()) {
                this.recipeBookButton = new ImageButton(this.f_97735_ + 104, this.f_96544_ / 2 - 22, 20, 18, 0, 0, 19, CuriosScreen.RECIPE_BUTTON_TEXTURE, button -> {
                    this.recipeBookGui.m_100384_();
                    button.m_264152_(this.f_97735_ + 104, this.f_96544_ / 2 - 22);
                    this.buttonCurios.m_264152_(this.f_97735_ + (Integer)offsets.m_14418_(), this.f_96544_ / 2 + (Integer)offsets.m_14419_());
                });
                this.m_142416_((GuiEventListener)this.recipeBookButton);
            }
            this.updateRenderButtons();
        }
    }

    public void updateRenderButtons() {
        this.f_169368_.removeIf(widget -> widget instanceof RenderButton || widget instanceof CosmeticButton || widget instanceof PageButton);
        this.f_96540_.removeIf(widget -> widget instanceof RenderButton || widget instanceof CosmeticButton || widget instanceof PageButton);
        this.f_169369_.removeIf(widget -> widget instanceof RenderButton || widget instanceof CosmeticButton || widget instanceof PageButton);
        this.panelWidth = ((CuriosContainerV2)this.f_97732_).panelWidth;
        if (((CuriosContainerV2)this.f_97732_).hasCosmetics) {
            this.cosmeticButton = new CosmeticButton(this, this.getGuiLeft() + 17, this.getGuiTop() - 18, 20, 17);
            this.m_142416_((GuiEventListener)this.cosmeticButton);
        }
        if (((CuriosContainerV2)this.f_97732_).totalPages > 1) {
            this.nextPage = new PageButton(this, this.getGuiLeft() + 17, this.getGuiTop() + 2, 11, 12, PageButton.Type.NEXT);
            this.m_142416_((GuiEventListener)this.nextPage);
            this.prevPage = new PageButton(this, this.getGuiLeft() + 17, this.getGuiTop() + 2, 11, 12, PageButton.Type.PREVIOUS);
            this.m_142416_((GuiEventListener)this.prevPage);
        }
        for (Slot inventorySlot : ((CuriosContainerV2)this.f_97732_).f_38839_) {
            if (!(inventorySlot instanceof CurioSlot)) continue;
            CurioSlot curioSlot = (CurioSlot)inventorySlot;
            if (inventorySlot instanceof CosmeticCurioSlot || !curioSlot.canToggleRender()) continue;
            this.m_142416_((GuiEventListener)new RenderButton(curioSlot, this.f_97735_ + inventorySlot.f_40220_ + 11, this.f_97736_ + inventorySlot.f_40221_ - 3, 8, 8, 75, 0, 8, CURIO_INVENTORY, button -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketToggleRender(curioSlot.getIdentifier(), inventorySlot.getSlotIndex()))));
        }
    }

    public void m_181908_() {
        super.m_181908_();
        this.recipeBookGui.m_100386_();
    }

    public void m_88315_(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        if (this.recipeBookGui.m_100385_() && this.widthTooNarrow) {
            this.m_7286_(guiGraphics, partialTicks, mouseX, mouseY);
            this.recipeBookGui.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        } else {
            Slot slot;
            this.recipeBookGui.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
            super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
            this.recipeBookGui.m_280128_(guiGraphics, this.f_97735_, this.f_97736_, true, partialTicks);
            boolean isButtonHovered = false;
            for (Renderable button : this.f_169369_) {
                if (!(button instanceof RenderButton)) continue;
                ((RenderButton)button).renderButtonOverlay(guiGraphics, mouseX, mouseY, partialTicks);
                if (!((RenderButton)button).m_274382_()) continue;
                isButtonHovered = true;
            }
            this.isRenderButtonHovered = isButtonHovered;
            LocalPlayer clientPlayer = Minecraft.m_91087_().f_91074_;
            if (!this.isRenderButtonHovered && clientPlayer != null && clientPlayer.f_36095_.m_142621_().m_41619_() && this.getSlotUnderMouse() != null && (slot = this.getSlotUnderMouse()) instanceof CurioSlot) {
                CurioSlot slotCurio = (CurioSlot)slot;
                if (!slot.m_6657_()) {
                    guiGraphics.m_280557_(this.f_96547_, (Component)Component.m_237113_((String)slotCurio.getSlotName()), mouseX, mouseY);
                }
            }
        }
        this.m_280072_(guiGraphics, mouseX, mouseY);
    }

    protected void m_280072_(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        LocalPlayer clientPlayer;
        Minecraft mc = this.f_96541_;
        if (mc != null && (clientPlayer = mc.f_91074_) != null && clientPlayer.f_36095_.m_142621_().m_41619_()) {
            if (this.isRenderButtonHovered) {
                guiGraphics.m_280557_(this.f_96547_, (Component)Component.m_237115_((String)"gui.curios.toggle"), mouseX, mouseY);
            } else if (this.f_97734_ != null && this.f_97734_.m_6657_()) {
                guiGraphics.m_280153_(this.f_96547_, this.f_97734_.m_7993_(), mouseX, mouseY);
            }
        }
    }

    public boolean m_7933_(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (this.recipeBookGui.m_100385_() && this.widthTooNarrow) {
            this.recipeBookGui.m_100384_();
            return true;
        }
        if (KeyRegistry.openCurios.isActiveAndMatches(InputConstants.m_84827_((int)p_keyPressed_1_, (int)p_keyPressed_2_))) {
            LocalPlayer playerEntity = this.getMinecraft().f_91074_;
            if (playerEntity != null) {
                playerEntity.m_6915_();
            }
            return true;
        }
        return super.m_7933_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    protected void m_280003_(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.f_96541_ != null && this.f_96541_.f_91074_ != null) {
            guiGraphics.m_280614_(this.f_96547_, this.f_96539_, 97, 6, 0x404040, false);
        }
    }

    protected void m_7286_(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        if (this.f_96541_ != null && this.f_96541_.f_91074_ != null) {
            if (scrollCooldown > 0 && this.f_96541_.f_91074_.f_19797_ % 5 == 0) {
                --scrollCooldown;
            }
            this.panelWidth = ((CuriosContainerV2)this.f_97732_).panelWidth;
            int i = this.f_97735_;
            int j = this.f_97736_;
            guiGraphics.m_280218_(f_97725_, i, j, 0, 0, 176, this.f_97727_);
            InventoryScreen.m_274545_((GuiGraphics)guiGraphics, (int)(i + 51), (int)(j + 75), (int)30, (float)((float)(i + 51) - (float)mouseX), (float)((float)(j + 75 - 50) - (float)mouseY), (LivingEntity)this.f_96541_.f_91074_);
            CuriosApi.getCuriosInventory((LivingEntity)this.f_96541_.f_91074_).ifPresent(handler -> {
                int upperHeight;
                int rows;
                boolean pageOffset;
                int xOffset = -33;
                int yOffset = j;
                boolean bl = pageOffset = ((CuriosContainerV2)this.f_97732_).totalPages > 1;
                if (((CuriosContainerV2)this.f_97732_).hasCosmetics) {
                    guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset + 2, yOffset - 23, 32, 0, 28, 24);
                }
                List<Integer> grid = ((CuriosContainerV2)this.f_97732_).grid;
                xOffset -= (grid.size() - 1) * 18;
                for (int r = 0; r < grid.size(); ++r) {
                    rows = grid.get(0);
                    upperHeight = 7 + rows * 18;
                    int xTexOffset = 91;
                    if (pageOffset) {
                        upperHeight += 8;
                    }
                    if (r != 0) {
                        xTexOffset += 7;
                    }
                    guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset, yOffset, xTexOffset, 0, 25, upperHeight);
                    guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset, yOffset + upperHeight, xTexOffset, 159, 25, 7);
                    if (grid.size() == 1) {
                        guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset + 7, yOffset, xTexOffset += 7, 0, 25, upperHeight);
                        guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset + 7, yOffset + upperHeight, xTexOffset, 159, 25, 7);
                    }
                    if (r == 0) {
                        xOffset += 25;
                        continue;
                    }
                    xOffset += 18;
                }
                xOffset -= grid.size() * 18;
                if (pageOffset) {
                    yOffset += 8;
                }
                Iterator iterator = grid.iterator();
                while (iterator.hasNext()) {
                    rows = iterator.next();
                    upperHeight = rows * 18;
                    guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset, yOffset + 7, 7, 7, 18, upperHeight);
                    xOffset += 18;
                }
                RenderSystem.enableBlend();
                for (Slot slot : ((CuriosContainerV2)this.f_97732_).f_38839_) {
                    CurioSlot curioSlot;
                    if (!(slot instanceof CurioSlot) || !(curioSlot = (CurioSlot)slot).isCosmetic()) continue;
                    guiGraphics.m_280218_(CURIO_INVENTORY, slot.f_40220_ + this.getGuiLeft() - 1, slot.f_40221_ + this.getGuiTop() - 1, 32, 50, 18, 18);
                }
                RenderSystem.disableBlend();
            });
        }
    }

    protected boolean m_6774_(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
        if (this.isRenderButtonHovered) {
            return false;
        }
        return (!this.widthTooNarrow || !this.recipeBookGui.m_100385_()) && super.m_6774_(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        if (this.recipeBookGui.m_6375_(mouseX, mouseY, mouseButton)) {
            return true;
        }
        return this.widthTooNarrow && this.recipeBookGui.m_100385_() || super.m_6375_(mouseX, mouseY, mouseButton);
    }

    public boolean m_6348_(double mouseReleased1, double mouseReleased3, int mouseReleased5) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        }
        return super.m_6348_(mouseReleased1, mouseReleased3, mouseReleased5);
    }

    public boolean m_6050_(double pMouseScrolled1, double pMouseScrolled3, double pMouseScrolled5) {
        if (((CuriosContainerV2)this.f_97732_).totalPages > 1 && pMouseScrolled1 < (double)this.getGuiLeft() && pMouseScrolled1 > (double)(this.getGuiLeft() - this.panelWidth) && pMouseScrolled3 > (double)this.getGuiTop() && pMouseScrolled3 < (double)(this.getGuiTop() + this.f_97727_) && scrollCooldown <= 0) {
            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketPage(((CuriosContainerV2)this.m_6262_()).f_38840_, pMouseScrolled5 < 0.0));
            scrollCooldown = 2;
        }
        return super.m_6050_(pMouseScrolled1, pMouseScrolled3, pMouseScrolled5);
    }

    protected boolean m_7467_(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.f_97726_) || mouseY >= (double)(guiTopIn + this.f_97727_);
        return this.recipeBookGui.m_100297_(mouseX, mouseY, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, mouseButton) && flag;
    }

    protected void m_6597_(@Nonnull Slot slotIn, int slotId, int mouseButton, @Nonnull ClickType type) {
        super.m_6597_(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.m_6904_(slotIn);
    }

    public void m_6916_() {
        this.recipeBookGui.m_100387_();
    }

    @Nonnull
    public RecipeBookComponent m_5564_() {
        return this.recipeBookGui;
    }
}

