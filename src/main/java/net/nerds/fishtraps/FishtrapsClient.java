package net.nerds.fishtraps;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapGui;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapGui;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapGui;

public class FishtrapsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FishTrapEntityManager.registerEntityRenderers();
        this.registerClientGuis();
    }

    private void registerClientGuis() {
        MenuScreens.register(FishTrapEntityManager.WOODEN_FISH_TRAP_SCREEN_HANDLER,
                WoodenFishTrapGui::new);

        MenuScreens.register(FishTrapEntityManager.IRON_FISH_TRAP_SCREEN_HANDLER,
                IronFishTrapGui::new);

        MenuScreens.register(FishTrapEntityManager.DIAMOND_FISH_TRAP_SCREEN_HANDLER,
                DiamondFishTrapGui::new);
    }
}