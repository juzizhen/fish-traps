package net.nerds.fishtraps.blocks.baseTrap;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public abstract class BaseFishTrapBlock extends BlockWithEntity implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public BaseFishTrapBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        throw new UnsupportedOperationException("Subclasses must override createBlockEntity");
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory) {
            ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, moved);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
                                                BlockPos pos, Direction direction, BlockPos neighborPos,
                                                BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == getBlockEntityType() ? (world1, pos, state1, be) -> {
            if (be instanceof BaseFishTrapBlockEntity fishTrap) {
                fishTrap.tick();
            }
        } : null;
    }

    protected abstract BlockEntityType<?> getBlockEntityType();
}
