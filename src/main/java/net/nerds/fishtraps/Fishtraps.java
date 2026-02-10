package net.nerds.fishtraps;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nerds.fishtraps.blocks.FishTrapsManager;
import net.nerds.fishtraps.config.FishTrapsConfig;

public class Fishtraps implements ModInitializer {

    public static final String MODID = "fishtraps";
    public static final ItemGroup FISHTRAPS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(FishTrapsManager.WOODEN_FISH_TRAP))  // 使用鱼笼图标而不是钓鱼竿
            .displayName(Text.translatable("itemGroup.fishtraps.fishtraps"))
            .entries((context, entries) -> {
                entries.add(FishTrapsManager.WOODEN_FISH_TRAP);
                entries.add(FishTrapsManager.IRON_FISH_TRAP);
                entries.add(FishTrapsManager.DIAMOND_FISH_TRAP);
                entries.add(FishTrapsManager.FISH_BAIT);
            })
            .build();

    public static FishTrapsConfig fishTrapsConfig;

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, "fishtraps"), FISHTRAPS_GROUP);

        fishTrapsConfig = new FishTrapsConfig();
        fishTrapsConfig.loadConfig();
        FishTrapsManager.init();
    }
}