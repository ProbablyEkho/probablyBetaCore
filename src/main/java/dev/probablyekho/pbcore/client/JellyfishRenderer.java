package dev.probablyekho.pbcore.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.probablyekho.pbcore.entity.Jellyfish;
import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class JellyfishRenderer extends MobRenderer<Jellyfish, SquidModel<Jellyfish>> {
    public JellyfishRenderer(EntityRendererProvider.Context context) {
        super(context, new SquidModel<>(context.bakeLayer(ModelLayers.SQUID)), 0.7F);
    }
    @Override
    protected void setupRotations(Jellyfish entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        float f = Mth.lerp(partialTick, entity.xBodyRotO, entity.xBodyRot);
        float f1 = Mth.lerp(partialTick, entity.zBodyRotO, entity.zBodyRot);
        poseStack.translate(0.0F, 0.5F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(f));
        poseStack.mulPose(Axis.YP.rotationDegrees(f1));
        poseStack.translate(0.0F, -1.2F, 0.0F);
    }
    @Override
    protected float getBob(Jellyfish livingBase, float partialTicks) {
        return Mth.lerp(partialTicks, livingBase.oldTentacleAngle, livingBase.tentacleAngle);
    }
    @Override
    protected int getBlockLightLevel(Jellyfish entity, BlockPos pos) {
        return 15;
    }
    @Override
    public ResourceLocation getTextureLocation(Jellyfish entity) {
        return ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, "textures/entity/jellyfish.png");
    }
}
