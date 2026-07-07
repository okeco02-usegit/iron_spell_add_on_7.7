/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
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
 *  net.minecraft.util.Mth
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
import net.minecraft.util.Mth;
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
import top.theillusivec4.curios.client.gui.CuriosButton;
import top.theillusivec4.curios.client.gui.RenderButton;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

public class CuriosScreen
extends EffectRenderingInventoryScreen<CuriosContainer>
implements RecipeUpdateListener,
ICuriosScreen {
    static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory.png");
    static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("minecraft:textures/gui/recipe_button.png");
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static float currentScroll;
    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();
    public boolean hasScrollBar;
    public boolean widthTooNarrow;
    private CuriosButton buttonCurios;
    private boolean isScrolling;
    private boolean buttonClicked;
    private boolean isRenderButtonHovered;

    public CuriosScreen(CuriosContainer curiosContainer, Inventory playerInventory, Component title) {
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
        super.m_7856_();
        if (this.f_96541_ != null) {
            if (this.f_96541_.f_91074_ != null) {
                this.hasScrollBar = CuriosApi.getCuriosInventory((LivingEntity)this.f_96541_.f_91074_).map(handler -> handler.getVisibleSlots() > 8).orElse(false);
                if (this.hasScrollBar) {
                    ((CuriosContainer)this.f_97732_).scrollTo(currentScroll);
                }
            }
            int neededWidth = 431;
            if (this.hasScrollBar) {
                neededWidth += 30;
            }
            if (((CuriosContainer)this.f_97732_).hasCosmeticColumn()) {
                neededWidth += 40;
            }
            this.widthTooNarrow = this.f_96543_ < neededWidth;
            this.recipeBookGui.m_100309_(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu)this.f_97732_);
            this.updateScreenPosition();
            this.m_7787_((GuiEventListener)this.recipeBookGui);
            this.m_264313_((GuiEventListener)this.recipeBookGui);
            if (this.getMinecraft().f_91074_ != null && this.getMinecraft().f_91074_.m_7500_() && this.recipeBookGui.m_100385_()) {
                this.recipeBookGui.m_100384_();
                this.updateScreenPosition();
            }
            Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(false);
            this.buttonCurios = new CuriosButton((AbstractContainerScreen<?>)this, this.getGuiLeft() + (Integer)offsets.m_14418_(), this.f_96544_ / 2 + (Integer)offsets.m_14419_(), 14, 14, 50, 0, 14, CURIO_INVENTORY);
            if (((Boolean)CuriosClientConfig.CLIENT.enableButton.get()).booleanValue()) {
                this.m_142416_((GuiEventListener)this.buttonCurios);
            }
            if (!((CuriosContainer)this.f_97732_).player.m_7500_()) {
                this.m_142416_((GuiEventListener)new ImageButton(this.f_97735_ + 104, this.f_96544_ / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, button -> {
                    this.recipeBookGui.m_100384_();
                    this.updateScreenPosition();
                    button.m_264152_(this.f_97735_ + 104, this.f_96544_ / 2 - 22);
                    this.buttonCurios.m_264152_(this.f_97735_ + (Integer)offsets.m_14418_(), this.f_96544_ / 2 + (Integer)offsets.m_14419_());
                }));
            }
            this.updateRenderButtons();
        }
    }

    public void updateRenderButtons() {
        this.f_169368_.removeIf(widget -> widget instanceof RenderButton);
        this.f_96540_.removeIf(widget -> widget instanceof RenderButton);
        this.f_169369_.removeIf(widget -> widget instanceof RenderButton);
        for (Slot inventorySlot : ((CuriosContainer)this.f_97732_).f_38839_) {
            if (!(inventorySlot instanceof CurioSlot)) continue;
            CurioSlot curioSlot = (CurioSlot)inventorySlot;
            if (inventorySlot instanceof CosmeticCurioSlot || !curioSlot.canToggleRender()) continue;
            this.m_142416_((GuiEventListener)new RenderButton(curioSlot, this.f_97735_ + inventorySlot.f_40220_ + 11, this.f_97736_ + inventorySlot.f_40221_ - 3, 8, 8, 75, 0, 8, CURIO_INVENTORY, button -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), (Object)new CPacketToggleRender(curioSlot.getIdentifier(), inventorySlot.getSlotIndex()))));
        }
    }

    private void updateScreenPosition() {
        int i;
        if (this.recipeBookGui.m_100385_() && !this.widthTooNarrow) {
            int offset = 148;
            if (this.hasScrollBar) {
                offset -= 30;
            }
            if (((CuriosContainer)this.f_97732_).hasCosmeticColumn()) {
                offset -= 40;
            }
            i = 177 + (this.f_96543_ - this.f_97726_ - offset) / 2;
        } else {
            i = (this.f_96543_ - this.f_97726_) / 2;
        }
        this.f_97735_ = i;
        this.updateRenderButtons();
    }

    public void m_181908_() {
        super.m_181908_();
        this.recipeBookGui.m_100386_();
    }

    private boolean inScrollBar(double mouseX, double mouseY) {
        int i = this.f_97735_;
        int j = this.f_97736_;
        int k = i - 34;
        int l = j + 12;
        int i1 = k + 14;
        int j1 = l + 139;
        if (((CuriosContainer)this.f_97732_).hasCosmeticColumn()) {
            i1 -= 19;
            k -= 19;
        }
        return mouseX >= (double)k && mouseY >= (double)l && mouseX < (double)i1 && mouseY < (double)j1;
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
            this.updateScreenPosition();
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
            int i = this.f_97735_;
            int j = this.f_97736_;
            guiGraphics.m_280218_(f_97725_, i, j, 0, 0, this.f_97726_, this.f_97727_);
            InventoryScreen.m_274545_((GuiGraphics)guiGraphics, (int)(i + 51), (int)(j + 75), (int)30, (float)((float)(i + 51) - (float)mouseX), (float)((float)(j + 75 - 50) - (float)mouseY), (LivingEntity)this.f_96541_.f_91074_);
            CuriosApi.getCuriosInventory((LivingEntity)this.f_96541_.f_91074_).ifPresent(handler -> {
                int slotCount = handler.getVisibleSlots();
                if (slotCount > 0) {
                    int upperHeight = 7 + Math.min(slotCount, 9) * 18;
                    int xTexOffset = 0;
                    int width = 27;
                    int xOffset = -26;
                    if (((CuriosContainer)this.f_97732_).hasCosmeticColumn()) {
                        xTexOffset = 92;
                        width = 46;
                        xOffset -= 19;
                    }
                    guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset, j + 4, xTexOffset, 0, width, upperHeight);
                    if (slotCount <= 8) {
                        guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset, j + 4 + upperHeight, xTexOffset, 151, width, 7);
                    } else {
                        guiGraphics.m_280218_(CURIO_INVENTORY, i + xOffset - 16, j + 4, 27, 0, 23, 158);
                        guiGraphics.m_280218_(CREATIVE_INVENTORY_TABS, i + xOffset - 8, j + 12 + (int)(127.0f * currentScroll), 232, 0, 12, 15);
                    }
                    for (Slot slot : ((CuriosContainer)this.f_97732_).f_38839_) {
                        if (!(slot instanceof CosmeticCurioSlot)) continue;
                        int x = this.f_97735_ + slot.f_40220_ - 1;
                        int y = this.f_97736_ + slot.f_40221_ - 1;
                        guiGraphics.m_280218_(CURIO_INVENTORY, x, y, 138, 0, 18, 18);
                    }
                }
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
        if (this.inScrollBar(mouseX, mouseY)) {
            this.isScrolling = this.needsScrollBars();
            return true;
        }
        return this.widthTooNarrow && this.recipeBookGui.m_100385_() || super.m_6375_(mouseX, mouseY, mouseButton);
    }

    public boolean m_6348_(double mouseReleased1, double mouseReleased3, int mouseReleased5) {
        if (mouseReleased5 == 0) {
            this.isScrolling = false;
        }
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        }
        return super.m_6348_(mouseReleased1, mouseReleased3, mouseReleased5);
    }

    public boolean m_7979_(double pMouseDragged1, double pMouseDragged3, int pMouseDragged5, double pMouseDragged6, double pMouseDragged8) {
        if (this.isScrolling) {
            int i = this.f_97736_ + 8;
            int j = i + 148;
            currentScroll = ((float)pMouseDragged3 - (float)i - 7.5f) / ((float)(j - i) - 15.0f);
            currentScroll = Mth.m_14036_((float)currentScroll, (float)0.0f, (float)1.0f);
            ((CuriosContainer)this.f_97732_).scrollTo(currentScroll);
            return true;
        }
        return super.m_7979_(pMouseDragged1, pMouseDragged3, pMouseDragged5, pMouseDragged6, pMouseDragged8);
    }

    public boolean m_6050_(double pMouseScrolled1, double pMouseScrolled3, double pMouseScrolled5) {
        if (!this.needsScrollBars()) {
            return false;
        }
        int i = ((CuriosContainer)this.f_97732_).curiosHandler.map(inv -> (int)Math.floor((double)inv.getVisibleSlots() / 8.0)).orElse(1);
        currentScroll = (float)((double)currentScroll - pMouseScrolled5 / (double)i);
        currentScroll = Mth.m_14036_((float)currentScroll, (float)0.0f, (float)1.0f);
        ((CuriosContainer)this.f_97732_).scrollTo(currentScroll);
        return true;
    }

    private boolean needsScrollBars() {
        return ((CuriosContainer)this.f_97732_).canScroll();
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

