/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraftforge.items.IItemHandlerModifiable
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.tags.ITagManager
 */
package top.theillusivec4.curios.mixin;

import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CuriosUtilMixinHooks {
    private static final ITagManager<Item> ITEM_TAGS = ForgeRegistries.ITEMS.tags();

    public static boolean canNeutralizePiglins(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity).map(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    int index = i;
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    boolean canNeutralize = CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> curio.makesPiglinsNeutral(new SlotContext((String)entry.getKey(), livingEntity, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false))).orElse(false);
                    if (!canNeutralize) continue;
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static boolean canWalkOnPowderSnow(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity).map(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); ++i) {
                    int index = i;
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    boolean canWalk = CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> curio.canWalkOnPowderedSnow(new SlotContext((String)entry.getKey(), livingEntity, index, false, renderStates.size() > index && (Boolean)renderStates.get(index) != false))).orElse(false);
                    if (!canWalk) continue;
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static int getFortuneLevel(Player player) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).map(handler -> handler.getFortuneLevel(null)).orElse(0);
    }

    public static int getFortuneLevel(LootContext lootContext) {
        Entity entity = (Entity)lootContext.m_78953_(LootContextParams.f_81455_);
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            return CuriosApi.getCuriosInventory(livingEntity).map(handler -> handler.getFortuneLevel(lootContext)).orElse(0);
        }
        return 0;
    }

    public static boolean isFreezeImmune(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity).map(curios -> {
            IItemHandlerModifiable handler = curios.getEquippedCurios();
            for (int i = 0; i < handler.getSlots(); ++i) {
                ItemStack stack = handler.getStackInSlot(i);
                if (ITEM_TAGS == null || !ITEM_TAGS.getTag(ItemTags.f_144320_).contains((Object)stack.m_41720_())) continue;
                return true;
            }
            return false;
        }).orElse(false);
    }

    public static CompoundTag mergeCuriosInventory(CompoundTag compoundTag, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            ListTag list = compoundTag.m_128437_("Inventory", 10);
            return CuriosApi.getCuriosInventory(livingEntity).map(inv -> {
                IItemHandlerModifiable handler = inv.getEquippedCurios();
                for (int i = 0; i < handler.getSlots(); ++i) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (stack.m_41619_()) continue;
                    CompoundTag tag = new CompoundTag();
                    tag.m_128344_("Slot", (byte)(4444 + i));
                    stack.m_41739_(tag);
                    list.add((Object)tag);
                }
                return compoundTag;
            }).orElse(compoundTag);
        }
        return compoundTag;
    }

    public static boolean containsStack(Player player, ItemStack stack) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).map(inv -> inv.findFirstCurio(stack2 -> !stack2.m_41619_() && ItemStack.m_150942_((ItemStack)stack, (ItemStack)stack2)).isPresent()).orElse(false);
    }

    public static boolean containsTag(Player player, TagKey<Item> tagKey) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).map(inv -> inv.findFirstCurio(stack2 -> !stack2.m_41619_() && stack2.m_204117_(tagKey)).isPresent()).orElse(false);
    }

    public static boolean hasAnyMatching(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).map(inv -> inv.findFirstCurio(predicate).isPresent()).orElse(false);
    }
}

