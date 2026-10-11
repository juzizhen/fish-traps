package net.nerds.fishtraps.blocks.woodenFishTrap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlock;

public class WoodenFishTrapBlock extends BaseFishTrapBlock {

    public WoodenFishTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenFishTrapBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return FishTrapEntityManager.WOODEN_FISH_TRAP_ENTITY;
    }
}