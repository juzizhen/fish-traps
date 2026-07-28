package net.nerds.fishtraps.blocks.diamondFishTrap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.nerds.fishtraps.blocks.FishTrapsManager;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DiamondFishTrapRenderer implements BlockEntityRenderer<DiamondFishTrapBlockEntity, DiamondFishTrapRenderer.FishTrapRenderState> {

    private final ItemModelManager itemModelManager;
    private final ItemStack fishBait;

    public DiamondFishTrapRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
        this.fishBait = new ItemStack(FishTrapsManager.FISH_BAIT);
    }

    @Override
    public FishTrapRenderState createRenderState() {
        return new FishTrapRenderState();
    }

    @Override
    public void updateRenderState(DiamondFishTrapBlockEntity blockEntity, FishTrapRenderState state, float tickDelta, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickDelta, cameraPos, crumblingOverlay);
        state.showFishBait = blockEntity.showFishBait();
        if (state.showFishBait) {
            this.itemModelManager.update(state.itemRenderState, fishBait, ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 0);
        }
    }

    @Override
    public void render(FishTrapRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.showFishBait) {
            matrices.push();
            matrices.translate(0.5D, 0.3D, 0.5D);
            float scale = 0.83125F;
            matrices.translate(0.0D, 0.4000000059604645D, 0.0D);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(9.0f));
            matrices.translate(0.0D, -0.20000000298023224D, 0.0D);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0F));
            matrices.scale(scale, scale, scale);
            state.itemRenderState.render(matrices, queue, state.lightmapCoordinates, 0, 0);
            matrices.pop();
        }
    }

    public static class FishTrapRenderState extends BlockEntityRenderState {
        public boolean showFishBait = false;
        public final ItemRenderState itemRenderState = new ItemRenderState();
    }
}
