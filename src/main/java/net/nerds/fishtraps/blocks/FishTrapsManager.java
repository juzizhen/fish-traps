package net.nerds.fishtraps.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapBlock;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapBlock;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapBlock;
import net.nerds.fishtraps.items.FishingBait;

import java.util.function.Function;

public class FishTrapsManager {

    public static WoodenFishTrapBlock WOODEN_FISH_TRAP;
    public static IronFishTrapBlock IRON_FISH_TRAP;
    public static DiamondFishTrapBlock DIAMOND_FISH_TRAP;
    public static Item FISH_BAIT;

    public static void init() {
        blockInit();
        itemInit();
        FishTrapEntityManager.init();
        FishTrapEntityManager.initGui();
    }

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("fishtraps", name));
        T item = factory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    private static <B extends Block> B registerBlock(ResourceKey<Block> key, B block) {
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, key.identifier());
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
        return block;
    }

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Fishtraps.MODID, name));
    }

    private static void blockInit() {
        WOODEN_FISH_TRAP = registerBlock(blockKey("wooden_fish_trap"),
                new WoodenFishTrapBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                        .strength(3.5f).noOcclusion()
                        .setId(blockKey("wooden_fish_trap"))));

        IRON_FISH_TRAP = registerBlock(blockKey("iron_fish_trap"),
                new IronFishTrapBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                        .strength(5.0f).noOcclusion().requiresCorrectToolForDrops()
                        .setId(blockKey("iron_fish_trap"))));

        DIAMOND_FISH_TRAP = registerBlock(blockKey("diamond_fish_trap"),
                new DiamondFishTrapBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)
                        .strength(5.0f).noOcclusion().requiresCorrectToolForDrops()
                        .setId(blockKey("diamond_fish_trap"))));
    }

    private static void itemInit() {
        FISH_BAIT = registerItem(
                "fish_trap_bait",
                FishingBait::new,
                new Item.Properties()
        );
    }
}