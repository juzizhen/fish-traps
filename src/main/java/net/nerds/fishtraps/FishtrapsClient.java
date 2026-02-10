package net.nerds.fishtraps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.FishTrapsManager;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapGui;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapGui;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapGui;

public class FishtrapsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FishTrapEntityManager.registerEntityRenderers();
        this.registerClientGuis();

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                FishTrapsManager.WOODEN_FISH_TRAP,
                FishTrapsManager.IRON_FISH_TRAP,
                FishTrapsManager.DIAMOND_FISH_TRAP);
    }

    private void registerClientGuis() {
        HandledScreens.register(FishTrapEntityManager.WOODEN_FISH_TRAP_SCREEN_HANDLER,
                WoodenFishTrapGui::new);

        HandledScreens.register(FishTrapEntityManager.IRON_FISH_TRAP_SCREEN_HANDLER,
                IronFishTrapGui::new);

        HandledScreens.register(FishTrapEntityManager.DIAMOND_FISH_TRAP_SCREEN_HANDLER,
                DiamondFishTrapGui::new);
    }
}