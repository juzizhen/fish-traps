package net.nerds.fishtraps.blocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
		Registry.register(Registries.BLOCK, new Identifier(Fishtraps.MODID, "wooden_fish_trap"), WOODEN_FISH_TRAP);
		Registry.register(Registries.ITEM, new Identifier(Fishtraps.MODID, "wooden_fish_trap"),
				new BlockItem(WOODEN_FISH_TRAP, new Item.Settings()));

		Registry.register(Registries.BLOCK, new Identifier(Fishtraps.MODID, "iron_fish_trap"), IRON_FISH_TRAP);
		Registry.register(Registries.ITEM, new Identifier(Fishtraps.MODID, "iron_fish_trap"),
				new BlockItem(IRON_FISH_TRAP, new Item.Settings()));

		Registry.register(Registries.BLOCK, new Identifier(Fishtraps.MODID, "diamond_fish_trap"), DIAMOND_FISH_TRAP);
		Registry.register(Registries.ITEM, new Identifier(Fishtraps.MODID, "diamond_fish_trap"),
				new BlockItem(DIAMOND_FISH_TRAP, new Item.Settings()));
	}

	private static void itemInit() {
		Registry.register(Registries.ITEM, new Identifier(Fishtraps.MODID, "fish_trap_bait"), FISH_BAIT);
	}
}