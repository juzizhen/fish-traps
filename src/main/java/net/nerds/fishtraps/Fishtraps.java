package net.nerds.fishtraps;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.nerds.fishtraps.blocks.FishTrapsManager;
import net.nerds.fishtraps.config.FishTrapsConfig;

public class Fishtraps implements ModInitializer {

    public static final String MODID = "fishtraps";

    public static FishTrapsConfig fishTrapsConfig;
    public static CreativeModeTab FISHTRAPS_GROUP;

    @Override
    public void onInitialize() {
        fishTrapsConfig = new FishTrapsConfig();
        fishTrapsConfig.loadConfig();

        FishTrapsManager.init();

        var tabBuilder = FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(FishTrapsManager.WOODEN_FISH_TRAP.asItem()))
                .title(Component.translatable("itemGroup.fishtraps.fishtraps"))
                .displayItems((_, entries) -> {
                    entries.accept(FishTrapsManager.WOODEN_FISH_TRAP.asItem());
                    entries.accept(FishTrapsManager.IRON_FISH_TRAP.asItem());
                    entries.accept(FishTrapsManager.DIAMOND_FISH_TRAP.asItem());
                    entries.accept(FishTrapsManager.FISH_BAIT);
                });
        FISHTRAPS_GROUP = tabBuilder.build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MODID, "fishtraps"), FISHTRAPS_GROUP);
    }
}