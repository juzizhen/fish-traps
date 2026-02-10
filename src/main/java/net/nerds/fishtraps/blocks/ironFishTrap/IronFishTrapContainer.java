package net.nerds.fishtraps.blocks.ironFishTrap;

import net.minecraft.entity.player.PlayerInventory;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapContainer;

public class IronFishTrapContainer extends BaseFishTrapContainer {

    public IronFishTrapContainer(int syncId, PlayerInventory playerInventory, BaseFishTrapBlockEntity blockEntity) {
        super(FishTrapEntityManager.IRON_FISH_TRAP_SCREEN_HANDLER, syncId, playerInventory, blockEntity);
    }
}