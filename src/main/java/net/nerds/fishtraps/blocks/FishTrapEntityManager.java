package net.nerds.fishtraps.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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

    public static final Identifier WOODEN_FISH_TRAP_CONTAINER = Identifier.of(Fishtraps.MODID, "wooden_fish_trap_container");
    public static final Identifier IRON_FISH_TRAP_CONTAINER = Identifier.of(Fishtraps.MODID, "iron_fish_trap_container");
    public static final Identifier DIAMOND_FISH_TRAP_CONTAINER = Identifier.of(Fishtraps.MODID, "diamond_fish_trap_container");
    public static BlockEntityType<WoodenFishTrapBlockEntity> WOODEN_FISH_TRAP_ENTITY;
    public static ScreenHandlerType<WoodenFishTrapContainer> WOODEN_FISH_TRAP_SCREEN_HANDLER;
    public static BlockEntityType<IronFishTrapBlockEntity> IRON_FISH_TRAP_ENTITY;
    public static ScreenHandlerType<IronFishTrapContainer> IRON_FISH_TRAP_SCREEN_HANDLER;
    public static BlockEntityType<DiamondFishTrapBlockEntity> DIAMOND_FISH_TRAP_ENTITY;
    public static ScreenHandlerType<DiamondFishTrapContainer> DIAMOND_FISH_TRAP_SCREEN_HANDLER;

    public static void init() {
        WOODEN_FISH_TRAP_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(Fishtraps.MODID, "wooden_fish_trap_entity"),
                FabricBlockEntityTypeBuilder.create(
                        WoodenFishTrapBlockEntity::new,
                        FishTrapsManager.WOODEN_FISH_TRAP
                ).build()
        );

        IRON_FISH_TRAP_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(Fishtraps.MODID, "iron_fish_trap_entity"),
                FabricBlockEntityTypeBuilder.create(
                        IronFishTrapBlockEntity::new,
                        FishTrapsManager.IRON_FISH_TRAP
                ).build()
        );

        DIAMOND_FISH_TRAP_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(Fishtraps.MODID, "diamond_fish_trap_entity"),
                FabricBlockEntityTypeBuilder.create(
                        DiamondFishTrapBlockEntity::new,
                        FishTrapsManager.DIAMOND_FISH_TRAP
                ).build()
        );
    }

    public static void initGui() {
        ExtendedScreenHandlerType<WoodenFishTrapContainer, BlockPos> woodenType =
                new ExtendedScreenHandlerType<>(
                        (int syncId, PlayerInventory inventory, BlockPos pos) -> {
                            WoodenFishTrapBlockEntity blockEntity =
                                    (WoodenFishTrapBlockEntity) inventory.player.getWorld().getBlockEntity(pos);
                            return new WoodenFishTrapContainer(syncId, inventory, blockEntity);
                        },
                        BlockPos.PACKET_CODEC
                );
        WOODEN_FISH_TRAP_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                WOODEN_FISH_TRAP_CONTAINER,
                woodenType
        );

        ExtendedScreenHandlerType<IronFishTrapContainer, BlockPos> ironType =
                new ExtendedScreenHandlerType<>(
                        (int syncId, PlayerInventory inventory, BlockPos pos) -> {
                            IronFishTrapBlockEntity blockEntity =
                                    (IronFishTrapBlockEntity) inventory.player.getWorld().getBlockEntity(pos);
                            return new IronFishTrapContainer(syncId, inventory, blockEntity);
                        },
                        BlockPos.PACKET_CODEC
                );
        IRON_FISH_TRAP_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                IRON_FISH_TRAP_CONTAINER,
                ironType
        );

        ExtendedScreenHandlerType<DiamondFishTrapContainer, BlockPos> diamondType =
                new ExtendedScreenHandlerType<>(
                        (int syncId, PlayerInventory inventory, BlockPos pos) -> {
                            DiamondFishTrapBlockEntity blockEntity =
                                    (DiamondFishTrapBlockEntity) inventory.player.getWorld().getBlockEntity(pos);
                            return new DiamondFishTrapContainer(syncId, inventory, blockEntity);
                        },
                        BlockPos.PACKET_CODEC
                );
        DIAMOND_FISH_TRAP_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                DIAMOND_FISH_TRAP_CONTAINER,
                diamondType
        );
    }

    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers() {
        BlockEntityRendererFactories.register(WOODEN_FISH_TRAP_ENTITY, WoodenFishTrapRenderer::new);
        BlockEntityRendererFactories.register(IRON_FISH_TRAP_ENTITY, IronFishTrapRenderer::new);
        BlockEntityRendererFactories.register(DIAMOND_FISH_TRAP_ENTITY, DiamondFishTrapRenderer::new);
    }
}