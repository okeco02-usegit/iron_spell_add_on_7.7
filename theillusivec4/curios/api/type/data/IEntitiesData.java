/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.world.entity.EntityType
 *  net.minecraftforge.common.crafting.conditions.ICondition
 */
package top.theillusivec4.curios.api.type.data;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.conditions.ICondition;

public interface IEntitiesData {
    public IEntitiesData replace(boolean var1);

    public IEntitiesData addPlayer();

    public IEntitiesData addEntities(EntityType<?> ... var1);

    public IEntitiesData addSlots(String ... var1);

    public IEntitiesData addCondition(ICondition var1);

    public JsonObject serialize();
}

