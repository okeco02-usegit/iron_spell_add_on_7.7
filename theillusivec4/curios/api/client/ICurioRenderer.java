/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexMultiConsumer
 *  com.mojang.math.Axis
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package top.theillusivec4.curios.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurioRenderer {
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack var1, SlotContext var2, PoseStack var3, RenderLayerParent<T, M> var4, MultiBufferSource var5, int var6, float var7, float var8, float var9, float var10, float var11, float var12);

    public static void translateIfSneaking(PoseStack matrixStack, LivingEntity livingEntity) {
        if (livingEntity.m_6047_()) {
            matrixStack.m_252880_(0.0f, 0.1875f, 0.0f);
        }
    }

    public static void rotateIfSneaking(PoseStack matrixStack, LivingEntity livingEntity) {
        LivingEntityRenderer livingRenderer;
        EntityModel model;
        EntityRenderer render;
        if (livingEntity.m_6047_() && (render = Minecraft.m_91087_().m_91290_().m_114382_((Entity)livingEntity)) instanceof LivingEntityRenderer && (model = (livingRenderer = (LivingEntityRenderer)render).m_7200_()) instanceof HumanoidModel) {
            matrixStack.m_252781_(Axis.f_252529_.m_252961_(((HumanoidModel)model).f_102810_.f_104203_));
        }
    }

    public static void followHeadRotations(LivingEntity livingEntity, ModelPart ... renderers) {
        LivingEntityRenderer livingRenderer;
        EntityModel model;
        EntityRenderer render = Minecraft.m_91087_().m_91290_().m_114382_((Entity)livingEntity);
        if (render instanceof LivingEntityRenderer && (model = (livingRenderer = (LivingEntityRenderer)render).m_7200_()) instanceof HumanoidModel) {
            for (ModelPart renderer : renderers) {
                renderer.m_104315_(((HumanoidModel)model).f_102808_);
            }
        }
    }

    @SafeVarargs
    public static void followBodyRotations(LivingEntity livingEntity, HumanoidModel<LivingEntity> ... models) {
        LivingEntityRenderer livingRenderer;
        EntityModel entityModel;
        EntityRenderer render = Minecraft.m_91087_().m_91290_().m_114382_((Entity)livingEntity);
        if (render instanceof LivingEntityRenderer && (entityModel = (livingRenderer = (LivingEntityRenderer)render).m_7200_()) instanceof HumanoidModel) {
            for (HumanoidModel<LivingEntity> model : models) {
                HumanoidModel bipedModel = (HumanoidModel)entityModel;
                bipedModel.m_102872_(model);
            }
        }
    }

    public static void renderModel(Model model, ResourceLocation textureLocation, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light, @Nullable RenderType glintRender) {
        RenderType renderType = model.m_103119_(textureLocation);
        VertexConsumer vertexConsumer = glintRender != null ? VertexMultiConsumer.m_86168_((VertexConsumer)renderTypeBuffer.m_6299_(glintRender), (VertexConsumer)renderTypeBuffer.m_6299_(renderType)) : renderTypeBuffer.m_6299_(renderType);
        model.m_7695_(poseStack, vertexConsumer, light, OverlayTexture.f_118083_, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static interface HumanoidRender
    extends ModelRender<HumanoidModel<LivingEntity>> {
        @Override
        default public void prepareModel(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            HumanoidModel model = (HumanoidModel)this.getModel(stack, slotContext);
            LivingEntity livingEntity = slotContext.entity();
            EntityModel parentModel = renderLayerParent.m_7200_();
            if (parentModel instanceof HumanoidModel) {
                HumanoidModel humanoidModel = (HumanoidModel)parentModel;
                humanoidModel.m_102872_(model);
            } else {
                parentModel.m_102624_((EntityModel)model);
            }
            model.m_6839_(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            model.m_6973_(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }

        @Override
        default public void renderModel(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent, MultiBufferSource renderTypeBuffer, int light) {
            ICurioRenderer.renderModel(this.getModel(stack, slotContext), this.getModelTexture(stack, slotContext), poseStack, renderTypeBuffer, light, (RenderType)(stack.m_41790_() ? RenderType.m_110484_() : null));
        }
    }

    public static interface ModelRender<L extends Model>
    extends ICurioRenderer {
        public L getModel(ItemStack var1, SlotContext var2);

        public ResourceLocation getModelTexture(ItemStack var1, SlotContext var2);

        default public void renderModel(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent, MultiBufferSource renderTypeBuffer, int light) {
            ICurioRenderer.renderModel(this.getModel(stack, slotContext), this.getModelTexture(stack, slotContext), poseStack, renderTypeBuffer, light, (RenderType)(stack.m_41790_() ? RenderType.m_110496_() : null));
        }

        default public void prepareModel(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        }

        @Override
        default public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            this.prepareModel(stack, slotContext, poseStack, renderLayerParent, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            this.renderModel(stack, slotContext, poseStack, renderLayerParent, renderTypeBuffer, light);
        }
    }
}

