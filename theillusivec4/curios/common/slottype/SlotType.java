/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  org.apache.commons.lang3.EnumUtils
 */
package top.theillusivec4.curios.common.slottype;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.EnumUtils;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;

public final class SlotType
implements ISlotType {
    private final String identifier;
    private final int order;
    private final int size;
    private final boolean useNativeGui;
    private final boolean hasCosmetic;
    private final ResourceLocation icon;
    private final ICurio.DropRule dropRule;
    private final boolean renderToggle;
    private final Set<ResourceLocation> validators;

    public static ISlotType from(CompoundTag tag) {
        Builder builder = new Builder(tag.m_128461_("Identifier"));
        builder.icon(new ResourceLocation(tag.m_128461_("Icon")));
        builder.order(tag.m_128451_("Order"));
        builder.size(tag.m_128451_("Size"));
        builder.useNativeGui(tag.m_128471_("UseNativeGui"));
        builder.hasCosmetic(tag.m_128471_("HasCosmetic"));
        builder.renderToggle(tag.m_128471_("ToggleRender"));
        builder.dropRule(ICurio.DropRule.values()[tag.m_128451_("DropRule")]);
        ListTag list = tag.m_128437_("Validators", 8);
        for (Tag tag1 : list) {
            if (!(tag1 instanceof StringTag)) continue;
            StringTag stringTag = (StringTag)tag1;
            builder.validator(new ResourceLocation(stringTag.m_7916_()));
        }
        return builder.build();
    }

    private SlotType(Builder builder) {
        this.identifier = builder.identifier;
        this.order = builder.order;
        this.size = builder.size;
        this.useNativeGui = builder.useNativeGui;
        this.hasCosmetic = builder.hasCosmetic;
        this.icon = builder.icon;
        this.dropRule = builder.dropRule;
        this.renderToggle = builder.renderToggle;
        this.validators = builder.validators;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean useNativeGui() {
        return this.useNativeGui;
    }

    @Override
    public boolean hasCosmetic() {
        return this.hasCosmetic;
    }

    @Override
    public boolean canToggleRendering() {
        return this.renderToggle;
    }

    @Override
    public ICurio.DropRule getDropRule() {
        return this.dropRule;
    }

    @Override
    public Set<ResourceLocation> getValidators() {
        return this.validators;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SlotType that = (SlotType)o;
        return this.identifier.equals(that.identifier);
    }

    public int hashCode() {
        return Objects.hash(this.identifier);
    }

    @Override
    public int compareTo(ISlotType otherType) {
        if (this.order == otherType.getOrder()) {
            return this.identifier.compareTo(otherType.getIdentifier());
        }
        if (this.order > otherType.getOrder()) {
            return 1;
        }
        return -1;
    }

    @Override
    public CompoundTag writeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.m_128359_("Identifier", this.identifier);
        tag.m_128359_("Icon", this.icon.toString());
        tag.m_128405_("Order", this.order);
        tag.m_128405_("Size", this.size);
        tag.m_128379_("UseNativeGui", this.useNativeGui);
        tag.m_128379_("HasCosmetic", this.hasCosmetic);
        tag.m_128379_("ToggleRender", this.renderToggle);
        tag.m_128405_("DropRule", this.dropRule.ordinal());
        ListTag list = new ListTag();
        for (ResourceLocation slotResultPredicate : this.validators) {
            list.add((Object)StringTag.m_129297_((String)slotResultPredicate.toString()));
        }
        tag.m_128365_("Validators", (Tag)list);
        return tag;
    }

    public static class Builder {
        private final String identifier;
        private Integer order = null;
        private Integer size = null;
        private int sizeMod = 0;
        private Boolean useNativeGui = null;
        private Boolean hasCosmetic = null;
        private Boolean renderToggle = null;
        private ResourceLocation icon = new ResourceLocation("curios", "slot/empty_curio_slot");
        private ICurio.DropRule dropRule = ICurio.DropRule.DEFAULT;
        private Set<ResourceLocation> validators = null;

        public Builder(String identifier) {
            this.identifier = identifier;
        }

        public void apply(Builder builder) {
            if (!builder.identifier.equals(this.identifier)) {
                Curios.LOGGER.error("Mismatched slot builders {} and {}", (Object)builder.identifier, (Object)this.identifier);
                return;
            }
            if (builder.order != null) {
                this.order(builder.order);
            }
            if (builder.size != null) {
                this.size(builder.size);
            }
            if (builder.useNativeGui != null) {
                this.useNativeGui(builder.useNativeGui);
            }
            if (builder.hasCosmetic != null) {
                this.hasCosmetic(builder.hasCosmetic);
            }
            if (builder.renderToggle != null) {
                this.renderToggle(builder.renderToggle);
            }
            if (builder.icon != null) {
                this.icon(builder.icon);
            }
            if (builder.dropRule != null) {
                this.dropRule(builder.dropRule);
            }
            if (builder.validators != null) {
                this.validators = Set.copyOf(builder.validators);
            }
        }

        public Builder icon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        public Builder order(int order) {
            return this.order(order, false);
        }

        public Builder order(int order, boolean replace) {
            this.order = replace || this.order == null ? order : Math.min(this.order, order);
            return this;
        }

        public Builder size(int size) {
            return this.size(size, false);
        }

        public Builder size(int size, String operation) {
            return this.size(size, operation, false);
        }

        public Builder size(int size, boolean replace) {
            return this.size(size, "SET", replace);
        }

        public Builder size(int size, String operation, boolean replace) {
            switch (operation) {
                case "SET": {
                    this.size = replace || this.size == null ? size : Math.max(this.size, size);
                    if (!replace) break;
                    this.sizeMod = 0;
                    break;
                }
                case "ADD": {
                    this.sizeMod = replace ? size : this.sizeMod + size;
                    break;
                }
                case "REMOVE": {
                    this.sizeMod = replace ? -size : this.sizeMod - size;
                }
            }
            return this;
        }

        public Builder useNativeGui(boolean useNativeGui) {
            return this.useNativeGui(useNativeGui, false);
        }

        public Builder useNativeGui(boolean useNativeGui, boolean replace) {
            this.useNativeGui = replace || this.useNativeGui == null ? useNativeGui : this.useNativeGui != false && useNativeGui;
            return this;
        }

        public Builder renderToggle(boolean renderToggle) {
            return this.renderToggle(renderToggle, false);
        }

        public Builder renderToggle(boolean renderToggle, boolean replace) {
            this.renderToggle = replace || this.renderToggle == null ? renderToggle : this.renderToggle != false && renderToggle;
            return this;
        }

        public Builder hasCosmetic(boolean hasCosmetic) {
            return this.hasCosmetic(hasCosmetic, false);
        }

        public Builder hasCosmetic(boolean hasCosmetic, boolean replace) {
            this.hasCosmetic = replace || this.hasCosmetic == null ? hasCosmetic : this.hasCosmetic != false || hasCosmetic;
            return this;
        }

        public Builder dropRule(ICurio.DropRule dropRule) {
            this.dropRule = dropRule;
            return this;
        }

        public Builder dropRule(String dropRule) {
            ICurio.DropRule newRule = (ICurio.DropRule)EnumUtils.getEnum(ICurio.DropRule.class, (String)dropRule);
            if (newRule == null) {
                Curios.LOGGER.error(dropRule + " is not a valid drop rule!");
            } else {
                this.dropRule = newRule;
            }
            return this;
        }

        public Builder validator(ResourceLocation slotResultPredicate) {
            if (this.validators == null) {
                this.validators = new HashSet<ResourceLocation>();
            }
            this.validators.add(slotResultPredicate);
            return this;
        }

        public SlotType build() {
            if (this.order == null) {
                this.order = 1000;
            }
            if (this.size == null) {
                this.size = 1;
            }
            this.size = this.size + this.sizeMod;
            this.size = Math.max(this.size, 0);
            if (this.useNativeGui == null) {
                this.useNativeGui = true;
            }
            if (this.hasCosmetic == null) {
                this.hasCosmetic = false;
            }
            if (this.renderToggle == null) {
                this.renderToggle = true;
            }
            if (this.validators == null) {
                this.validators = Set.of(new ResourceLocation("curios", "tag"));
            }
            return new SlotType(this);
        }
    }
}

