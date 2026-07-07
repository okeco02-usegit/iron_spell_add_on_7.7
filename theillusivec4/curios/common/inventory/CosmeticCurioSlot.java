/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.core.NonNullList
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package top.theillusivec4.curios.common.inventory;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.inventory.CurioSlot;

public class CosmeticCurioSlot
extends CurioSlot {
    public CosmeticCurioSlot(Player player, IDynamicStackHandler handler, int index, String identifier, int xPosition, int yPosition) {
        super(player, handler, index, identifier, xPosition, yPosition, (NonNullList<Boolean>)NonNullList.m_122779_(), true);
        this.setBackground(InventoryMenu.f_39692_, new ResourceLocation("curios", "slot/empty_cosmetic_slot"));
    }

    @Override
    public boolean getRenderStatus() {
        return true;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public String getSlotName() {
        return I18n.m_118938_((String)"curios.cosmetic", (Object[])new Object[0]) + " " + super.getSlotName();
    }
}

