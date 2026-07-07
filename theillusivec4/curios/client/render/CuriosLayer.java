/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nonnull
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package top.theillusivec4.curios.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CuriosLayer<T extends LivingEntity, M extends EntityModel<T>>
extends RenderLayer<T, M> {
    private final RenderLayerParent<T, M> renderLayerParent;

    public CuriosLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
        this.renderLayerParent = renderer;
    }

    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource renderTypeBuffer, int light, @Nonnull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.m_85836_();
        CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> handler.getCurios().forEach((id, stacksHandler) -> {
            IDynamicStackHandler stackHandler = stacksHandler.getStacks();
            IDynamicStackHandler cosmeticStacksHandler = stacksHandler.getCosmeticStacks();
            for (int i = 0; i < stackHandler.getSlots(); ++i) {
                boolean renderable;
                ItemStack stack = cosmeticStacksHandler.getStackInSlot(i);
                boolean cosmetic = true;
                NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                boolean bl = renderable = renderStates.size() > i && (Boolean)renderStates.get(i) != false;
                if (stack.m_41619_() && renderable) {
                    stack = stackHandler.getStackInSlot(i);
                    cosmetic = false;
                }
                if (stack.m_41619_()) continue;
                SlotContext slotContext = new SlotContext((String)id, (LivingEntity)livingEntity, i, cosmetic, renderable);
                ItemStack finalStack = stack;
                CuriosRendererRegistry.getRenderer(stack.m_41720_()).ifPresent(renderer -> renderer.render(finalStack, slotContext, matrixStack, this.renderLayerParent, renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch));
            }
        }));
        matrixStack.m_85849_();
    }
}

