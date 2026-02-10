package net.nerds.fishtraps.blocks.diamondFishTrap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.nerds.fishtraps.blocks.FishTrapsManager;

@Environment(EnvType.CLIENT)
public class DiamondFishTrapRenderer implements BlockEntityRenderer<DiamondFishTrapBlockEntity> {

    private final ItemStack fishBait;
    private final ItemRenderer itemRenderer;

    public DiamondFishTrapRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        fishBait = new ItemStack(FishTrapsManager.FISH_BAIT);
    }

    @Override
    public void render(DiamondFishTrapBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (blockEntity.showFishBait()) {
            matrices.push();
            matrices.translate(0.5D, 0.3D, 0.5D);
            float scale = 0.83125F;
            matrices.translate(0.0D, 0.4000000059604645D, 0.0D);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(9.0f));
            matrices.translate(0.0D, -0.20000000298023224D, 0.0D);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0F));
            matrices.scale(scale, scale, scale);
            itemRenderer.renderItem(fishBait, ModelTransformationMode.NONE, light, overlay, matrices, vertexConsumers, blockEntity.getWorld(), 0);
            matrices.pop();
        }
    }
}