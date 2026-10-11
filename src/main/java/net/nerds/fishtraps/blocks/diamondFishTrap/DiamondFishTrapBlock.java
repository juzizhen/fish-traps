package net.nerds.fishtraps.blocks.diamondFishTrap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlock;

public class DiamondFishTrapBlock extends BaseFishTrapBlock {

    public DiamondFishTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DiamondFishTrapBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return FishTrapEntityManager.DIAMOND_FISH_TRAP_ENTITY;
    }
}