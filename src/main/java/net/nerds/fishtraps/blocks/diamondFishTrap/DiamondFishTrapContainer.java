package net.nerds.fishtraps.blocks.diamondFishTrap;

import net.minecraft.entity.player.PlayerInventory;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapContainer;

public class DiamondFishTrapContainer extends BaseFishTrapContainer {

    public DiamondFishTrapContainer(int syncId, PlayerInventory playerInventory, BaseFishTrapBlockEntity blockEntity) {
        super(FishTrapEntityManager.DIAMOND_FISH_TRAP_SCREEN_HANDLER, syncId, playerInventory, blockEntity);
    }
}