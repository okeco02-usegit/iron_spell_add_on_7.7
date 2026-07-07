/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraftforge.common.crafting.conditions.ICondition
 */
package top.theillusivec4.curios.api.type.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.crafting.conditions.ICondition;
import top.theillusivec4.curios.api.type.capability.ICurio;

public interface ISlotData {
    public ISlotData replace(boolean var1);

    public ISlotData order(int var1);

    public ISlotData size(int var1);

    default public ISlotData operation(String operation) {
        return this.operation(AttributeModifier.Operation.ADDITION);
    }

    public ISlotData useNativeGui(boolean var1);

    public ISlotData addCosmetic(boolean var1);

    public ISlotData renderToggle(boolean var1);

    public ISlotData icon(ResourceLocation var1);

    public ISlotData dropRule(ICurio.DropRule var1);

    public ISlotData addCondition(ICondition var1);

    public ISlotData addValidator(ResourceLocation var1);

    public JsonObject serialize();

    @Deprecated(forRemoval=true)
    public ISlotData operation(AttributeModifier.Operation var1);
}

