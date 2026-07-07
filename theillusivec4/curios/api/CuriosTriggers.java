/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.advancements.CriterionTriggerInstance
 *  net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance
 *  net.minecraft.advancements.critereon.ContextAwarePredicate
 *  net.minecraft.advancements.critereon.ItemPredicate$Builder
 *  net.minecraft.advancements.critereon.LocationPredicate$Builder
 *  net.minecraft.resources.ResourceLocation
 */
package top.theillusivec4.curios.api;

import javax.annotation.Nonnull;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotPredicate;

public final class CuriosTriggers {
    @Nonnull
    public static EquipBuilder equip() {
        return new EquipBuilder();
    }

    @Deprecated
    @Nonnull
    public static CriterionTriggerInstance equip(ItemPredicate.Builder itemPredicate) {
        CuriosApi.apiError();
        return new EmptyInstance();
    }

    @Deprecated
    @Nonnull
    public static CriterionTriggerInstance equipAtLocation(ItemPredicate.Builder itemPredicate, LocationPredicate.Builder locationPredicate) {
        CuriosApi.apiError();
        return new EmptyInstance();
    }

    public static final class EquipBuilder {
        private ItemPredicate.Builder itemPredicate;
        private LocationPredicate.Builder locationPredicate;
        private SlotPredicate.Builder slotPredicate;

        private EquipBuilder() {
        }

        public EquipBuilder withItem(ItemPredicate.Builder builder) {
            this.itemPredicate = builder;
            return this;
        }

        public EquipBuilder withLocation(LocationPredicate.Builder builder) {
            this.locationPredicate = builder;
            return this;
        }

        public EquipBuilder withSlot(SlotPredicate.Builder builder) {
            this.slotPredicate = builder;
            return this;
        }

        public CriterionTriggerInstance build() {
            return new EmptyInstance();
        }
    }

    private static final class EmptyInstance
    extends AbstractCriterionTriggerInstance {
        public EmptyInstance() {
            super(new ResourceLocation("curios", "empty"), ContextAwarePredicate.f_285567_);
        }
    }
}

