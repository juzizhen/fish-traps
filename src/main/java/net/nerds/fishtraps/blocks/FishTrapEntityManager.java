package net.nerds.fishtraps.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapContainer;
import net.nerds.fishtraps.blocks.diamondFishTrap.DiamondFishTrapRenderer;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapContainer;
import net.nerds.fishtraps.blocks.ironFishTrap.IronFishTrapRenderer;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapBlockEntity;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapContainer;
import net.nerds.fishtraps.blocks.woodenFishTrap.WoodenFishTrapRenderer;

public class FishTrapEntityManager {

    public static final Identifier WOODEN_FISH_TRAP_CONTAINER = Identifier.fromNamespaceAndPath(Fishtraps.MODID, "wooden_fish_trap_container");
    public static final Identifier IRON_FISH_TRAP_CONTAINER = Identifier.fromNamespaceAndPath(Fishtraps.MODID, "iron_fish_trap_container");
    public static final Identifier DIAMOND_FISH_TRAP_CONTAINER = Identifier.fromNamespaceAndPath(Fishtraps.MODID, "diamond_fish_trap_container");
    public static BlockEntityType<WoodenFishTrapBlockEntity> WOODEN_FISH_TRAP_ENTITY;
    public static MenuType<WoodenFishTrapContainer> WOODEN_FISH_TRAP_SCREEN_HANDLER;
    public static BlockEntityType<IronFishTrapBlockEntity> IRON_FISH_TRAP_ENTITY;
    public static MenuType<IronFishTrapContainer> IRON_FISH_TRAP_SCREEN_HANDLER;
    public static BlockEntityType<DiamondFishTrapBlockEntity> DIAMOND_FISH_TRAP_ENTITY;
    public static MenuType<DiamondFishTrapContainer> DIAMOND_FISH_TRAP_SCREEN_HANDLER;

    public static void init() {
        WOODEN_FISH_TRAP_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(Fishtraps.MODID, "wooden_fish_trap_entity"),
                new BlockEntityType<>(
                        WoodenFishTrapBlockEntity::new,
                        java.util.Set.of(FishTrapsManager.WOODEN_FISH_TRAP)
                )
        );

        IRON_FISH_TRAP_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(Fishtraps.MODID, "iron_fish_trap_entity"),
                new BlockEntityType<>(
                        IronFishTrapBlockEntity::new,
                        java.util.Set.of(FishTrapsManager.IRON_FISH_TRAP)
                )
        );

        DIAMOND_FISH_TRAP_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(Fishtraps.MODID, "diamond_fish_trap_entity"),
                new BlockEntityType<>(
                        DiamondFishTrapBlockEntity::new,
                        java.util.Set.of(FishTrapsManager.DIAMOND_FISH_TRAP)
                )
        );
    }

    public static void initGui() {
        ExtendedMenuType<WoodenFishTrapContainer, BlockPos> woodenType =
                new ExtendedMenuType<>(
                        (int containerId, Inventory inventory, BlockPos pos) -> {
                            WoodenFishTrapBlockEntity blockEntity =
                                    (WoodenFishTrapBlockEntity) inventory.player.level().getBlockEntity(pos);
                            return new WoodenFishTrapContainer(containerId, inventory, blockEntity);
                        },
                        BlockPos.STREAM_CODEC
                );
        WOODEN_FISH_TRAP_SCREEN_HANDLER = Registry.register(
                BuiltInRegistries.MENU,
                WOODEN_FISH_TRAP_CONTAINER,
                woodenType
        );

        ExtendedMenuType<IronFishTrapContainer, BlockPos> ironType =
                new ExtendedMenuType<>(
                        (int containerId, Inventory inventory, BlockPos pos) -> {
                            IronFishTrapBlockEntity blockEntity =
                                    (IronFishTrapBlockEntity) inventory.player.level().getBlockEntity(pos);
                            return new IronFishTrapContainer(containerId, inventory, blockEntity);
                        },
                        BlockPos.STREAM_CODEC
                );
        IRON_FISH_TRAP_SCREEN_HANDLER = Registry.register(
                BuiltInRegistries.MENU,
                IRON_FISH_TRAP_CONTAINER,
                ironType
        );

        ExtendedMenuType<DiamondFishTrapContainer, BlockPos> diamondType =
                new ExtendedMenuType<>(
                        (int containerId, Inventory inventory, BlockPos pos) -> {
                            DiamondFishTrapBlockEntity blockEntity =
                                    (DiamondFishTrapBlockEntity) inventory.player.level().getBlockEntity(pos);
                            return new DiamondFishTrapContainer(containerId, inventory, blockEntity);
                        },
                        BlockPos.STREAM_CODEC
                );
        DIAMOND_FISH_TRAP_SCREEN_HANDLER = Registry.register(
                BuiltInRegistries.MENU,
                DIAMOND_FISH_TRAP_CONTAINER,
                diamondType
        );
    }

    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers() {
        BlockEntityRenderers.register(WOODEN_FISH_TRAP_ENTITY, WoodenFishTrapRenderer::new);
        BlockEntityRenderers.register(IRON_FISH_TRAP_ENTITY, IronFishTrapRenderer::new);
        BlockEntityRenderers.register(DIAMOND_FISH_TRAP_ENTITY, DiamondFishTrapRenderer::new);
    }
}