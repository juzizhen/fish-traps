package net.nerds.fishtraps.blocks.baseTrap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.nerds.fishtraps.blocks.FishTrapsManager;

@Environment(EnvType.CLIENT)
public class FishTrapBaitRenderer implements BlockEntityRenderer<BaseFishTrapBlockEntity, FishTrapBaitRenderer.FishTrapRenderState> {

    private static final float BAIT_SCALE = 0.83125F;

    private final BlockEntityRendererProvider.Context context;
    private ItemStack fishBait;

    public FishTrapBaitRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    private ItemStack getFishBait() {
        if (fishBait == null) {
            fishBait = new ItemStack(FishTrapsManager.FISH_BAIT);
        }
        return fishBait;
    }

    @Override
    public FishTrapRenderState createRenderState() {
        return new FishTrapRenderState();
    }

    @Override
    public void extractRenderState(BaseFishTrapBlockEntity blockEntity, FishTrapRenderState state, float tickDelta,
                                   Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumbling);
        if (blockEntity.showFishBait()) {
            state.showBait = true;
            context.itemModelResolver().updateForTopItem(state.baitRenderState, getFishBait(),
                    ItemDisplayContext.NONE, blockEntity.getLevel(), null, 0);
        } else {
            state.showBait = false;
        }
    }

    @Override
    public void submit(FishTrapRenderState state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState cameraState) {
        if (state.showBait) {
            matrices.pushPose();
            matrices.translate(0.5D, 0.3D, 0.5D);
            matrices.translate(0.0D, 0.4000000059604645D, 0.0D);
            matrices.rotateDegrees(Axis.YP, 9.0F);
            matrices.translate(0.0D, -0.20000000298023224D, 0.0D);
            matrices.rotateDegrees(Axis.XN, 30.0F);
            matrices.scale(BAIT_SCALE, BAIT_SCALE, BAIT_SCALE);
            state.baitRenderState.submit(matrices, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    public static class FishTrapRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState baitRenderState = new ItemStackRenderState();
        public boolean showBait = false;
    }
}
