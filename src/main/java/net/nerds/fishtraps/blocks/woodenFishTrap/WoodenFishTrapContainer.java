package net.nerds.fishtraps.blocks.woodenFishTrap;

import net.minecraft.world.entity.player.Inventory;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapContainer;

public class WoodenFishTrapContainer extends BaseFishTrapContainer {

    public WoodenFishTrapContainer(int containerId, Inventory playerInventory, BaseFishTrapBlockEntity blockEntity) {
        super(FishTrapEntityManager.WOODEN_FISH_TRAP_SCREEN_HANDLER, containerId, playerInventory, blockEntity);
    }
}