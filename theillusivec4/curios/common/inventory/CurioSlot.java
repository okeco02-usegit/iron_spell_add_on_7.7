/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.items.IItemHandler
 *  net.minecraftforge.items.SlotItemHandler
 */
package top.theillusivec4.curios.common.inventory;

import javax.annotation.Nonnull;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.mixin.core.AccessorEntity;

public class CurioSlot
extends SlotItemHandler {
    private final String identifier;
    private final Player player;
    private final SlotContext slotContext;
    private NonNullList<Boolean> renderStatuses;
    private boolean canToggleRender;
    private boolean showCosmeticToggle;
    private boolean isCosmetic;

    public CurioSlot(Player player, IDynamicStackHandler handler, int index, String identifier, int xPosition, int yPosition, NonNullList<Boolean> renders, boolean canToggleRender, boolean showCosmeticToggle, boolean isCosmetic) {
        this(player, handler, index, identifier, xPosition, yPosition, renders, canToggleRender);
        this.showCosmeticToggle = showCosmeticToggle;
        this.isCosmetic = isCosmetic;
    }

    public CurioSlot(Player player, IDynamicStackHandler handler, int index, String identifier, int xPosition, int yPosition, NonNullList<Boolean> renders, boolean canToggleRender) {
        super((IItemHandler)handler, index, xPosition, yPosition);
        this.identifier = identifier;
        this.renderStatuses = renders;
        this.player = player;
        this.canToggleRender = canToggleRender;
        this.slotContext = new SlotContext(identifier, (LivingEntity)player, index, this instanceof CosmeticCurioSlot, this instanceof CosmeticCurioSlot || (Boolean)renders.get(index) != false);
        CuriosApi.getSlot(identifier, player.m_9236_()).ifPresent(slotType -> this.setBackground(InventoryMenu.f_39692_, slotType.getIcon()));
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean canToggleRender() {
        return this.canToggleRender;
    }

    public boolean isCosmetic() {
        return this.isCosmetic;
    }

    public boolean showCosmeticToggle() {
        return this.showCosmeticToggle;
    }

    public boolean getRenderStatus() {
        if (!this.canToggleRender) {
            return true;
        }
        return this.renderStatuses.size() > this.getSlotIndex() && (Boolean)this.renderStatuses.get(this.getSlotIndex()) != false;
    }

    @OnlyIn(value=Dist.CLIENT)
    public String getSlotName() {
        String key;
        StringBuilder builder = new StringBuilder();
        if (this.isCosmetic) {
            builder.append(I18n.m_118938_((String)"curios.cosmetic", (Object[])new Object[0])).append(" ");
        }
        if (I18n.m_118936_((String)(key = "curios.identifier." + this.identifier))) {
            builder.append(I18n.m_118938_((String)key, (Object[])new Object[0]));
            return builder.toString();
        }
        builder.append(Character.toUpperCase(this.identifier.charAt(0))).append(this.identifier.substring(1).toLowerCase());
        return builder.toString();
    }

    public void m_5852_(@Nonnull ItemStack stack) {
        ItemStack current = this.m_7993_();
        boolean flag = current.m_41619_() && stack.m_41619_();
        super.m_5852_(stack);
        if (!(flag || ItemStack.m_41728_((ItemStack)current, (ItemStack)stack) || ((AccessorEntity)this.player).getFirstTick())) {
            CuriosApi.getCurio(stack).ifPresent(curio -> curio.onEquipFromUse(this.slotContext));
        }
    }

    public boolean m_150651_(@Nonnull Player pPlayer) {
        return true;
    }
}

