package net.nerds.fishtraps.blocks.ironFishTrap;

import net.minecraft.world.entity.player.Inventory;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapContainer;

public class IronFishTrapContainer extends BaseFishTrapContainer {

    public IronFishTrapContainer(int containerId, Inventory playerInventory, BaseFishTrapBlockEntity blockEntity) {
        super(FishTrapEntityManager.IRON_FISH_TRAP_SCREEN_HANDLER, containerId, playerInventory, blockEntity);
    }
}