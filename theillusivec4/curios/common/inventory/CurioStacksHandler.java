/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  org.apache.commons.lang3.EnumUtils
 */
package top.theillusivec4.curios.common.inventory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.EnumUtils;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.inventory.DynamicStackHandler;

public class CurioStacksHandler
implements ICurioStacksHandler {
    private static final UUID LEGACY_UUID = UUID.fromString("0b0eabbd-4220-4e9f-bafb-34100da2bd7e");
    private final ICuriosItemHandler itemHandler;
    private final String identifier;
    private final Map<UUID, AttributeModifier> modifiers = new HashMap<UUID, AttributeModifier>();
    private final Set<AttributeModifier> persistentModifiers = new HashSet<AttributeModifier>();
    private final Set<AttributeModifier> cachedModifiers = new HashSet<AttributeModifier>();
    private final Multimap<AttributeModifier.Operation, AttributeModifier> modifiersByOperation = HashMultimap.create();
    private int baseSize;
    private IDynamicStackHandler stackHandler;
    private IDynamicStackHandler cosmeticStackHandler;
    private boolean visible;
    private boolean cosmetic;
    private boolean canToggleRender;
    private ICurio.DropRule dropRule;
    private boolean update;
    private NonNullList<Boolean> renderHandler;

    public CurioStacksHandler(ICuriosItemHandler itemHandler, String identifier) {
        this(itemHandler, identifier, 1, true, false, true, ICurio.DropRule.DEFAULT);
    }

    public CurioStacksHandler(ICuriosItemHandler itemHandler, String identifier, int size, boolean visible, boolean cosmetic, boolean canToggleRender, ICurio.DropRule dropRule) {
        this.baseSize = size;
        this.visible = visible;
        this.cosmetic = cosmetic;
        this.itemHandler = itemHandler;
        this.identifier = identifier;
        this.canToggleRender = canToggleRender;
        this.dropRule = dropRule;
        this.renderHandler = NonNullList.m_122780_((int)size, (Object)true);
        this.stackHandler = new DynamicStackHandler(size, index -> new SlotContext(identifier, itemHandler.getWearer(), (int)index, false, (Boolean)this.getRenders().get(index.intValue())));
        this.cosmeticStackHandler = new DynamicStackHandler(size, index -> new SlotContext(identifier, itemHandler.getWearer(), (int)index, true, (Boolean)this.getRenders().get(index.intValue())));
    }

    @Override
    public IDynamicStackHandler getStacks() {
        this.update();
        return this.stackHandler;
    }

    @Override
    public IDynamicStackHandler getCosmeticStacks() {
        this.update();
        return this.cosmeticStackHandler;
    }

    @Override
    public NonNullList<Boolean> getRenders() {
        this.update();
        return this.renderHandler;
    }

    @Override
    public boolean canToggleRendering() {
        return this.canToggleRender;
    }

    @Override
    public ICurio.DropRule getDropRule() {
        return this.dropRule;
    }

    @Override
    public int getSlots() {
        this.update();
        return this.stackHandler.getSlots();
    }

    @Override
    public int getSizeShift() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public boolean hasCosmetic() {
        return this.cosmetic;
    }

    @Override
    public void grow(int amount) {
        if ((amount = Math.max(0, amount)) > 0) {
            this.addLegacyChange(amount);
        }
    }

    @Override
    public void shrink(int amount) {
        if ((amount = Math.max(0, amount)) > 0) {
            this.addLegacyChange(Math.min(this.getSlots(), amount) * -1);
        }
    }

    private void addLegacyChange(int shift) {
        AttributeModifier mod = this.getModifiers().get(LEGACY_UUID);
        int current = mod != null ? (int)mod.m_22218_() : 0;
        AttributeModifier newModifier = new AttributeModifier(LEGACY_UUID, "legacy", (double)(current += shift), AttributeModifier.Operation.ADDITION);
        this.modifiers.put(newModifier.m_22209_(), newModifier);
        Collection<AttributeModifier> modifiers = this.getModifiersByOperation(newModifier.m_22217_());
        modifiers.remove(newModifier);
        modifiers.add(newModifier);
        this.persistentModifiers.remove(newModifier);
        this.persistentModifiers.add(newModifier);
        this.flagUpdate();
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag list;
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.m_128405_("SavedBaseSize", this.baseSize);
        compoundNBT.m_128365_("Stacks", (Tag)this.stackHandler.serializeNBT());
        compoundNBT.m_128365_("Cosmetics", (Tag)this.cosmeticStackHandler.serializeNBT());
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.renderHandler.size(); ++i) {
            CompoundTag tag = new CompoundTag();
            tag.m_128405_("Slot", i);
            tag.m_128379_("Render", ((Boolean)this.renderHandler.get(i)).booleanValue());
            nbtTagList.add((Object)tag);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.m_128365_("Renders", (Tag)nbtTagList);
        nbt.m_128405_("Size", this.renderHandler.size());
        compoundNBT.m_128365_("Renders", (Tag)nbt);
        compoundNBT.m_128379_("HasCosmetic", this.cosmetic);
        compoundNBT.m_128379_("Visible", this.visible);
        compoundNBT.m_128379_("RenderToggle", this.canToggleRender);
        compoundNBT.m_128359_("DropRule", this.dropRule.toString());
        if (!this.persistentModifiers.isEmpty()) {
            list = new ListTag();
            for (AttributeModifier attributeModifier : this.persistentModifiers) {
                list.add((Object)attributeModifier.m_22219_());
            }
            compoundNBT.m_128365_("PersistentModifiers", (Tag)list);
        }
        if (!this.modifiers.isEmpty()) {
            list = new ListTag();
            this.modifiers.forEach((uuid, modifier) -> {
                if (!this.persistentModifiers.contains(modifier)) {
                    list.add((Object)modifier.m_22219_());
                }
            });
            compoundNBT.m_128365_("CachedModifiers", (Tag)list);
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag list;
        int sizeShift;
        if (nbt.m_128441_("SavedBaseSize")) {
            this.baseSize = nbt.m_128451_("SavedBaseSize");
        }
        if (nbt.m_128441_("Stacks")) {
            this.stackHandler.deserializeNBT(nbt.m_128469_("Stacks"));
        }
        if (nbt.m_128441_("Cosmetics")) {
            this.cosmeticStackHandler.deserializeNBT(nbt.m_128469_("Cosmetics"));
        }
        if (nbt.m_128441_("Renders")) {
            CompoundTag tag = nbt.m_128469_("Renders");
            this.renderHandler = NonNullList.m_122780_((int)(nbt.m_128425_("Size", 3) ? nbt.m_128451_("Size") : this.stackHandler.getSlots()), (Object)true);
            ListTag tagList = tag.m_128437_("Renders", 10);
            for (int i = 0; i < tagList.size(); ++i) {
                CompoundTag tags = tagList.m_128728_(i);
                int slot = tags.m_128451_("Slot");
                if (slot < 0 || slot >= this.renderHandler.size()) continue;
                this.renderHandler.set(slot, (Object)tags.m_128471_("Render"));
            }
        }
        if (nbt.m_128441_("SizeShift") && (sizeShift = nbt.m_128451_("SizeShift")) != 0) {
            this.addLegacyChange(sizeShift);
        }
        this.cosmetic = nbt.m_128441_("HasCosmetic") ? nbt.m_128471_("HasCosmetic") : this.cosmetic;
        this.visible = nbt.m_128441_("Visible") ? nbt.m_128471_("Visible") : this.visible;
        boolean bl = this.canToggleRender = nbt.m_128441_("RenderToggle") ? nbt.m_128471_("RenderToggle") : this.canToggleRender;
        if (nbt.m_128441_("DropRule")) {
            this.dropRule = (ICurio.DropRule)EnumUtils.getEnum(ICurio.DropRule.class, (String)nbt.m_128461_("DropRule"), (Enum)this.dropRule);
        }
        if (nbt.m_128425_("PersistentModifiers", 9)) {
            list = nbt.m_128437_("PersistentModifiers", 10);
            for (int i = 0; i < list.size(); ++i) {
                AttributeModifier attributeModifier = AttributeModifier.m_22212_((CompoundTag)list.m_128728_(i));
                if (attributeModifier == null) continue;
                this.addPermanentModifier(attributeModifier);
            }
        }
        if (nbt.m_128425_("CachedModifiers", 9)) {
            list = nbt.m_128437_("CachedModifiers", 10);
            for (int i = 0; i < list.size(); ++i) {
                AttributeModifier attributeModifier = AttributeModifier.m_22212_((CompoundTag)list.m_128728_(i));
                if (attributeModifier == null) continue;
                this.cachedModifiers.add(attributeModifier);
                this.addTransientModifier(attributeModifier);
            }
        }
        this.update();
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public CompoundTag getSyncTag() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.m_128365_("Stacks", (Tag)this.stackHandler.serializeNBT());
        compoundNBT.m_128365_("Cosmetics", (Tag)this.cosmeticStackHandler.serializeNBT());
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.renderHandler.size(); ++i) {
            CompoundTag tag = new CompoundTag();
            tag.m_128405_("Slot", i);
            tag.m_128379_("Render", ((Boolean)this.renderHandler.get(i)).booleanValue());
            nbtTagList.add((Object)tag);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.m_128365_("Renders", (Tag)nbtTagList);
        nbt.m_128405_("Size", this.renderHandler.size());
        compoundNBT.m_128365_("Renders", (Tag)nbt);
        compoundNBT.m_128379_("HasCosmetic", this.cosmetic);
        compoundNBT.m_128379_("Visible", this.visible);
        compoundNBT.m_128379_("RenderToggle", this.canToggleRender);
        compoundNBT.m_128359_("DropRule", this.dropRule.toString());
        compoundNBT.m_128405_("BaseSize", this.baseSize);
        if (!this.modifiers.isEmpty()) {
            ListTag list = new ListTag();
            for (Map.Entry<UUID, AttributeModifier> modifier : this.modifiers.entrySet()) {
                list.add((Object)modifier.getValue().m_22219_());
            }
            compoundNBT.m_128365_("Modifiers", (Tag)list);
        }
        return compoundNBT;
    }

    @Override
    public void applySyncTag(CompoundTag tag) {
        int sizeShift;
        if (tag.m_128441_("BaseSize")) {
            this.baseSize = tag.m_128451_("BaseSize");
        }
        if (tag.m_128441_("Stacks")) {
            this.stackHandler.deserializeNBT(tag.m_128469_("Stacks"));
        }
        if (tag.m_128441_("Cosmetics")) {
            this.cosmeticStackHandler.deserializeNBT(tag.m_128469_("Cosmetics"));
        }
        if (tag.m_128441_("Renders")) {
            CompoundTag compoundNBT = tag.m_128469_("Renders");
            this.renderHandler = NonNullList.m_122780_((int)(compoundNBT.m_128425_("Size", 3) ? compoundNBT.m_128451_("Size") : this.stackHandler.getSlots()), (Object)true);
            ListTag tagList = compoundNBT.m_128437_("Renders", 10);
            for (int i = 0; i < tagList.size(); ++i) {
                CompoundTag tags = tagList.m_128728_(i);
                int slot = tags.m_128451_("Slot");
                if (slot < 0 || slot >= this.renderHandler.size()) continue;
                this.renderHandler.set(slot, (Object)tags.m_128471_("Render"));
            }
        }
        if (tag.m_128441_("SizeShift") && (sizeShift = tag.m_128451_("SizeShift")) != 0) {
            this.addLegacyChange(sizeShift);
        }
        this.cosmetic = tag.m_128441_("HasCosmetic") ? tag.m_128471_("HasCosmetic") : this.cosmetic;
        this.visible = tag.m_128441_("Visible") ? tag.m_128471_("Visible") : this.visible;
        boolean bl = this.canToggleRender = tag.m_128441_("RenderToggle") ? tag.m_128471_("RenderToggle") : this.canToggleRender;
        if (tag.m_128441_("DropRule")) {
            this.dropRule = (ICurio.DropRule)EnumUtils.getEnum(ICurio.DropRule.class, (String)tag.m_128461_("DropRule"), (Enum)this.dropRule);
        }
        this.modifiers.clear();
        this.persistentModifiers.clear();
        this.modifiersByOperation.clear();
        if (tag.m_128425_("Modifiers", 9)) {
            ListTag list = tag.m_128437_("Modifiers", 10);
            for (int i = 0; i < list.size(); ++i) {
                AttributeModifier attributeModifier = AttributeModifier.m_22212_((CompoundTag)list.m_128728_(i));
                if (attributeModifier == null) continue;
                this.addTransientModifier(attributeModifier);
            }
        }
        this.flagUpdate();
        this.update();
    }

    @Override
    public void copyModifiers(ICurioStacksHandler other) {
        this.modifiers.clear();
        this.cachedModifiers.clear();
        this.modifiersByOperation.clear();
        this.persistentModifiers.clear();
        other.getModifiers().forEach((uuid, modifier) -> this.addTransientModifier((AttributeModifier)modifier));
        this.cachedModifiers.addAll(other.getCachedModifiers());
        for (AttributeModifier persistentModifier : other.getPermanentModifiers()) {
            this.addPermanentModifier(persistentModifier);
        }
        this.update();
    }

    @Override
    public Map<UUID, AttributeModifier> getModifiers() {
        return this.modifiers;
    }

    @Override
    public Set<AttributeModifier> getPermanentModifiers() {
        return this.persistentModifiers;
    }

    @Override
    public Set<AttributeModifier> getCachedModifiers() {
        return this.cachedModifiers;
    }

    @Override
    public Collection<AttributeModifier> getModifiersByOperation(AttributeModifier.Operation operation) {
        return this.modifiersByOperation.get((Object)operation);
    }

    @Override
    public void addTransientModifier(AttributeModifier modifier) {
        this.modifiers.put(modifier.m_22209_(), modifier);
        this.getModifiersByOperation(modifier.m_22217_()).add(modifier);
        this.flagUpdate();
    }

    @Override
    public void addPermanentModifier(AttributeModifier modifier) {
        this.addTransientModifier(modifier);
        this.persistentModifiers.add(modifier);
    }

    @Override
    public void removeModifier(UUID uuid) {
        AttributeModifier modifier = this.modifiers.remove(uuid);
        if (modifier != null) {
            this.persistentModifiers.remove(modifier);
            this.getModifiersByOperation(modifier.m_22217_()).remove(modifier);
            this.flagUpdate();
        }
    }

    private void flagUpdate() {
        this.update = true;
        if (this.itemHandler != null) {
            this.itemHandler.getUpdatingInventories().remove(this);
            this.itemHandler.getUpdatingInventories().add(this);
        }
    }

    @Override
    public void clearModifiers() {
        HashSet<UUID> ids = new HashSet<UUID>(this.modifiers.keySet());
        for (UUID id : ids) {
            this.removeModifier(id);
        }
    }

    @Override
    public void clearCachedModifiers() {
        for (AttributeModifier cachedModifier : this.cachedModifiers) {
            this.removeModifier(cachedModifier.m_22209_());
        }
        this.cachedModifiers.clear();
        this.flagUpdate();
    }

    @Override
    public void update() {
        if (this.update) {
            this.update = false;
            double baseSize = this.baseSize;
            for (AttributeModifier mod : this.getModifiersByOperation(AttributeModifier.Operation.ADDITION)) {
                baseSize += mod.m_22218_();
            }
            double size = baseSize;
            for (AttributeModifier mod : this.getModifiersByOperation(AttributeModifier.Operation.MULTIPLY_BASE)) {
                size += (double)this.baseSize * mod.m_22218_();
            }
            for (AttributeModifier mod : this.getModifiersByOperation(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
                size *= mod.m_22218_();
            }
            if ((size = Math.max(0.0, size)) != (double)this.getSlots()) {
                this.resize((int)size);
                if (this.itemHandler != null && this.itemHandler.getWearer() != null) {
                    MinecraftForge.EVENT_BUS.post((Event)new SlotModifiersUpdatedEvent(this.itemHandler.getWearer(), Set.of(this.identifier)));
                    LivingEntity livingEntity = this.itemHandler.getWearer();
                    if (livingEntity instanceof Player) {
                        Player player = (Player)livingEntity;
                        if (player.f_36096_ instanceof ICuriosMenu) {
                            ((ICuriosMenu)player.f_36096_).resetSlots();
                        }
                    }
                }
            }
        }
    }

    private void resize(int newSize) {
        int currentSize = this.getSlots();
        if (currentSize != newSize) {
            int change = newSize - currentSize;
            if (currentSize > newSize) {
                this.loseStacks(this.stackHandler, this.identifier, change *= -1);
                this.stackHandler.shrink(change);
                this.cosmeticStackHandler.shrink(change);
                NonNullList newList = NonNullList.m_122780_((int)Math.max(0, newSize), (Object)true);
                for (int i = 0; i < newList.size() && i < this.renderHandler.size(); ++i) {
                    newList.set(i, (Object)((Boolean)this.renderHandler.get(i)));
                }
                this.renderHandler = newList;
            } else {
                this.stackHandler.grow(change);
                this.cosmeticStackHandler.grow(change);
                NonNullList newList = NonNullList.m_122780_((int)Math.max(0, newSize), (Object)true);
                for (int i = 0; i < newList.size() && i < this.renderHandler.size(); ++i) {
                    newList.set(i, (Object)((Boolean)this.renderHandler.get(i)));
                }
                this.renderHandler = newList;
            }
        }
    }

    private void loseStacks(IDynamicStackHandler stackHandler, String identifier, int amount) {
        if (this.itemHandler == null) {
            return;
        }
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        for (int i = Math.max(0, stackHandler.getSlots() - amount); i >= 0 && i < stackHandler.getSlots(); ++i) {
            ItemStack stack = stackHandler.getStackInSlot(i);
            drops.add(stackHandler.getStackInSlot(i));
            LivingEntity entity = this.itemHandler.getWearer();
            SlotContext slotContext = new SlotContext(identifier, entity, i, false, this.visible);
            if (!stack.m_41619_()) {
                UUID uuid = CuriosApi.getSlotUuid(slotContext);
                Multimap<Attribute, AttributeModifier> map = CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
                HashMultimap slots = HashMultimap.create();
                HashSet<SlotAttribute> toRemove = new HashSet<SlotAttribute>();
                for (Attribute attribute : map.keySet()) {
                    if (!(attribute instanceof SlotAttribute)) continue;
                    SlotAttribute wrapper = (SlotAttribute)attribute;
                    slots.putAll((Object)wrapper.getIdentifier(), (Iterable)map.get((Object)attribute));
                    toRemove.add(wrapper);
                }
                for (Attribute attribute : toRemove) {
                    map.removeAll((Object)attribute);
                }
                this.itemHandler.getWearer().m_21204_().m_22161_(map);
                this.itemHandler.removeSlotModifiers((Multimap<String, AttributeModifier>)slots);
                CuriosApi.getCurio(stack).ifPresent(curio -> curio.onUnequip(slotContext, ItemStack.f_41583_));
            }
            stackHandler.setStackInSlot(i, ItemStack.f_41583_);
        }
        drops.forEach(this.itemHandler::loseInvalidStack);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CurioStacksHandler that = (CurioStacksHandler)o;
        return this.identifier.equals(that.identifier);
    }

    public int hashCode() {
        return Objects.hash(this.identifier);
    }
}

