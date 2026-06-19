package net.nerds.fishtraps.blocks.woodenFishTrap;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.blocks.FishTrapEntityManager;
import net.nerds.fishtraps.blocks.baseTrap.BaseFishTrapBlockEntity;
import net.nerds.fishtraps.config.FishTrapValues;
import net.minecraft.world.level.storage.loot.LootTable;

public class WoodenFishTrapBlockEntity extends BaseFishTrapBlockEntity implements ExtendedMenuProvider<BlockPos> {

    public static final ResourceKey<LootTable> LOOT_TABLE_KEY = ResourceKey.create(
            Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("fishtraps", "gameplay/wooden_fish_trap"));

    public WoodenFishTrapBlockEntity(BlockPos pos, BlockState state) {
        super(FishTrapEntityManager.WOODEN_FISH_TRAP_ENTITY, pos, state,
                Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.WOODEN_TIME),
                Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.WOODEN_LURE),
                Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.WOODEN_LUCK),
                LOOT_TABLE_KEY);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.fishtraps.wooden_fish_trap");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new WoodenFishTrapContainer(containerId, playerInventory, this);
    }
}