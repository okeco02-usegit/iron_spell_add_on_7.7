/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package top.theillusivec4.curios.common.inventory.container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

public class CuriosContainerProvider
implements MenuProvider {
    @Nonnull
    public Component m_5446_() {
        return Component.m_237115_((String)"container.crafting");
    }

    @Nullable
    public AbstractContainerMenu m_7208_(int i, @Nonnull Inventory playerInventory, @Nonnull Player playerEntity) {
        if (((Boolean)CuriosConfig.SERVER.enableLegacyMenu.get()).booleanValue()) {
            return new CuriosContainer(i, playerInventory);
        }
        return new CuriosContainerV2(i, playerInventory);
    }
}

