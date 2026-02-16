package net.nerds.fishtraps.blocks.diamondFishTrap;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlockEntity;
import net.nerds.fishtraps.config.FishTrapValues;

public class DiamondFishTrapBlockEntity extends BaseFishTrapBlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {

    public DiamondFishTrapBlockEntity(BlockPos pos, BlockState state) {
        super(FishTrapEntityManager.DIAMOND_FISH_TRAP_ENTITY, pos, state,
                Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.DIAMOND_TIME),
                Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.DIAMOND_LURE),
                Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.DIAMOND_LUCK));
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.fishtraps.diamond_fish_trap");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new DiamondFishTrapContainer(syncId, playerInventory, this);
    }
}