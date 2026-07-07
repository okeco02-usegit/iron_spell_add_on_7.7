/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  javax.annotation.Nonnull
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.CachedOutput
 *  net.minecraft.data.DataProvider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.PackOutput$PathProvider
 *  net.minecraft.data.PackOutput$Target
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.common.data.ExistingFileHelper
 */
package top.theillusivec4.curios.api;

import com.google.gson.JsonElement;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.data.IEntitiesData;
import top.theillusivec4.curios.api.type.data.ISlotData;

public abstract class CuriosDataProvider
implements DataProvider {
    private final PackOutput.PathProvider entitiesPathProvider;
    private final PackOutput.PathProvider slotsPathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final String modId;
    private final Map<String, ISlotData> slotBuilders = new HashMap<String, ISlotData>();
    private final Map<String, IEntitiesData> entitiesBuilders = new HashMap<String, IEntitiesData>();
    private final ExistingFileHelper fileHelper;

    public CuriosDataProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        this.modId = modId;
        this.fileHelper = fileHelper;
        this.entitiesPathProvider = output.m_245269_(PackOutput.Target.DATA_PACK, "curios/entities");
        this.slotsPathProvider = output.m_245269_(PackOutput.Target.DATA_PACK, "curios/slots");
        this.registries = registries;
    }

    public abstract void generate(HolderLookup.Provider var1, ExistingFileHelper var2);

    @Nonnull
    public CompletableFuture<?> m_213708_(@Nonnull CachedOutput pOutput) {
        return this.registries.thenCompose(p_255484_ -> {
            ArrayList list = new ArrayList();
            this.generate((HolderLookup.Provider)p_255484_, this.fileHelper);
            this.slotBuilders.forEach((slot, slotBuilder) -> {
                Path path = this.slotsPathProvider.m_245731_(new ResourceLocation(this.modId, slot));
                list.add(DataProvider.m_253162_((CachedOutput)pOutput, (JsonElement)slotBuilder.serialize(), (Path)path));
            });
            this.entitiesBuilders.forEach((entities, entitiesBuilder) -> {
                Path path = this.entitiesPathProvider.m_245731_(new ResourceLocation(this.modId, entities));
                list.add(DataProvider.m_253162_((CachedOutput)pOutput, (JsonElement)entitiesBuilder.serialize(), (Path)path));
            });
            return CompletableFuture.allOf((CompletableFuture[])list.toArray(CompletableFuture[]::new));
        });
    }

    public final ISlotData createSlot(String id) {
        return this.slotBuilders.computeIfAbsent(id, k -> CuriosDataProvider.createSlotData());
    }

    public final ISlotData copySlot(String id, String copyId) {
        if (id.equals(copyId)) {
            return this.createSlot(id);
        }
        return this.slotBuilders.computeIfAbsent(id, k -> this.slotBuilders.getOrDefault(copyId, CuriosDataProvider.createSlotData()));
    }

    public final IEntitiesData createEntities(String id) {
        return this.entitiesBuilders.computeIfAbsent(id, k -> CuriosDataProvider.createEntitiesData());
    }

    public final IEntitiesData copyEntities(String id, String copyId) {
        if (id.equals(copyId)) {
            return this.createEntities(id);
        }
        return this.entitiesBuilders.computeIfAbsent(id, k -> this.entitiesBuilders.getOrDefault(copyId, CuriosDataProvider.createEntitiesData()));
    }

    @Nonnull
    public final String m_6055_() {
        return "Curios for " + this.modId;
    }

    private static ISlotData createSlotData() {
        CuriosApi.apiError();
        return null;
    }

    private static IEntitiesData createEntitiesData() {
        CuriosApi.apiError();
        return null;
    }
}

