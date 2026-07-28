package net.nerds.fishtraps.blocks.ironFishTrap;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlock;

public class IronFishTrapBlock extends BaseFishTrapBlock {

    public IronFishTrapBlock() {
        super(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK,
                        Identifier.of(Fishtraps.MODID, "iron_fish_trap")))
                .hardness(5.0f)
                .nonOpaque()
                .requiresTool());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IronFishTrapBlockEntity(pos, state);
    }

    @Override
    protected net.minecraft.block.entity.BlockEntityType<?> getBlockEntityType() {
        return FishTrapEntityManager.IRON_FISH_TRAP_ENTITY;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            NamedScreenHandlerFactory screenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(settings -> new IronFishTrapBlock());
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
    }
}
