package net.nerds.fishtraps.blocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapBlock;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapBlock;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapBlock;
import net.nerds.fishtraps.items.FishingBait;

public class FishTrapsManager {

    public static final WoodenFishTrapBlock WOODEN_FISH_TRAP = new WoodenFishTrapBlock();
    public static final IronFishTrapBlock IRON_FISH_TRAP = new IronFishTrapBlock();
    public static final DiamondFishTrapBlock DIAMOND_FISH_TRAP = new DiamondFishTrapBlock();
    public static final Item FISH_BAIT = new FishingBait();

    public static void init() {
        FishTrapEntityManager.init();
        FishTrapEntityManager.initGui();
        blockInit();
        itemInit();
    }

    private static void blockInit() {
        Identifier woodenId = Identifier.of(Fishtraps.MODID, "wooden_fish_trap");
        Registry.register(Registries.BLOCK, woodenId, WOODEN_FISH_TRAP);
        Registry.register(Registries.ITEM, woodenId,
                new BlockItem(WOODEN_FISH_TRAP,
                        new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, woodenId))));

        Identifier ironId = Identifier.of(Fishtraps.MODID, "iron_fish_trap");
        Registry.register(Registries.BLOCK, ironId, IRON_FISH_TRAP);
        Registry.register(Registries.ITEM, ironId,
                new BlockItem(IRON_FISH_TRAP,
                        new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, ironId))));

        Identifier diamondId = Identifier.of(Fishtraps.MODID, "diamond_fish_trap");
        Registry.register(Registries.BLOCK, diamondId, DIAMOND_FISH_TRAP);
        Registry.register(Registries.ITEM, diamondId,
                new BlockItem(DIAMOND_FISH_TRAP,
                        new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, diamondId))));
    }

    private static void itemInit() {
        Registry.register(Registries.ITEM, Identifier.of(Fishtraps.MODID, "fish_trap_bait"), FISH_BAIT);
    }
}