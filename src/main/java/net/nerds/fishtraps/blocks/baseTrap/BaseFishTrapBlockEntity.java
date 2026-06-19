package net.nerds.fishtraps.blocks.baseTrap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceKey;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.config.FishTrapValues;
import net.nerds.fishtraps.items.FishingBait;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFishTrapBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {

    private static final int[] AVAILABLE_SLOTS;
    static {
        AVAILABLE_SLOTS = new int[46];
        for (int i = 0; i < 46; i++) {
            AVAILABLE_SLOTS[i] = i;
        }
    }

    private final long tickValidator;
    private final long tickValidatorPenalty;
    private final int lureLevel;
    private final int luckOfTheSeaLevel;
    private final boolean shouldPenalty;
    private final int maxStorage = 46;
    public NonNullList<ItemStack> inventory;
    private long tickCounter = 0;
    private boolean showFishBait = false;
    private final boolean workingInLava;
    private final ResourceKey<LootTable> lootTableKey;

    public BaseFishTrapBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int fishDelay, int lureLevel, int luckOfTheSeaLevel, ResourceKey<LootTable> lootTableKey) {
        super(type, pos, state);
        inventory = NonNullList.withSize(maxStorage, ItemStack.EMPTY);
        this.tickValidator = fishDelay;
        this.tickValidatorPenalty = this.tickValidator * Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.PENALTY_MULTIPLIER_AMOUNT);
        this.lureLevel = lureLevel;
        this.luckOfTheSeaLevel = luckOfTheSeaLevel;
        this.shouldPenalty = Fishtraps.fishTrapsConfig.getBooleanProperty(FishTrapValues.SHOULD_PENALTY_MULTIPLIER);
        this.workingInLava = Fishtraps.fishTrapsConfig.getBooleanProperty(FishTrapValues.WORKING_IN_LAVA);
        this.lootTableKey = lootTableKey;
    }

    public static boolean accelerationEnabled = false;

    public void tick() {
        showFishBait = !this.inventory.getFirst().isEmpty();
        if (tickCounter >= getValidationNumber()) {
            tickCounter = 0;
            validateLiquidAndFish();
        } else {
            tickCounter += accelerationEnabled ? 64 : 1;
        }
    }

    private long getValidationNumber() {
        if (!showFishBait && this.shouldPenalty) {
            return this.tickValidatorPenalty;
        }
        return this.tickValidator;
    }

    private void validateLiquidAndFish() {
        if (level == null || level.isClientSide()) return;

        boolean isSurroundedByLiquid = true;
        Iterable<BlockPos> waterCheckIterator = BlockPos.betweenClosed(
                new BlockPos(worldPosition.getX() - 1, worldPosition.getY(), worldPosition.getZ() - 1),
                new BlockPos(worldPosition.getX() + 1, worldPosition.getY(), worldPosition.getZ() + 1));

        for (BlockPos blockPos : waterCheckIterator) {
            Block block = level.getBlockState(blockPos).getBlock();

            boolean validLiquid = block == Blocks.WATER;
            if (workingInLava) {
                validLiquid = validLiquid || block == Blocks.LAVA;
            }

            if (!(validLiquid || block instanceof BaseFishTrapBlock)) {
                isSurroundedByLiquid = false;
                break;
            }
        }

        if (isSurroundedByLiquid) {
            fish();
        }
    }

    private void fish() {
        ItemStack itemStack = new ItemStack(Items.FISHING_ROD);

        if (this.level != null && !this.level.isClientSide()) {
            net.minecraft.core.Registry<Enchantment> enchantmentRegistry = this.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

            Holder<Enchantment> lureEntry = enchantmentRegistry.getOrThrow(Enchantments.LURE);
            Holder<Enchantment> luckEntry = enchantmentRegistry.getOrThrow(Enchantments.LUCK_OF_THE_SEA);

            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            builder.set(lureEntry, this.lureLevel);
            builder.set(luckEntry, this.luckOfTheSeaLevel);
            ItemEnchantments enchantments = builder.toImmutable();

            itemStack.set(DataComponents.ENCHANTMENTS, enchantments);
        }

        if (this.level == null) return;
        ServerLevel serverLevel = (ServerLevel) this.level;
        LootParams.Builder lootParamsBuilder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition))
                .withParameter(LootContextParams.TOOL, itemStack)
                .withLuck((float) this.luckOfTheSeaLevel);

        LootTable lootTable = serverLevel.getServer()
                .reloadableRegistries().getLootTable(this.lootTableKey);
        if (lootTable == LootTable.EMPTY) {
            lootTable = serverLevel.getServer()
                    .reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
        }

        List<ItemStack> list = new ArrayList<>();
        lootTable.getRandomItems(lootParamsBuilder.create(LootContextParamSets.FISHING), list::add);

        addItemsToInventory(list);

        if (showFishBait) {
            ItemStack fishBait = inventory.getFirst();
            if (fishBait.getItem() instanceof FishingBait) {
                int remainingUses = FishingBait.getRemainingUses(fishBait) - 1;
                if (remainingUses <= 0) {
                    fishBait.shrink(1);
                } else {
                    FishingBait.setRemainingUses(fishBait, remainingUses);
                }
                setChanged();
            }
        }
    }

    private void addItemsToInventory(List<ItemStack> itemStackList) {
        boolean changed = false;
        for (ItemStack itemStack : itemStackList) {
            for (int i = 1; i < inventory.size(); i++) {
                if (inventory.get(i).isEmpty()) {
                    inventory.set(i, itemStack.copy());
                    changed = true;
                    break;
                } else if (ItemStack.isSameItem(inventory.get(i), itemStack) &&
                        (inventory.get(i).getCount() + itemStack.getCount() <= itemStack.getMaxStackSize()) &&
                        itemStack.isStackable()) {
                    ItemStack newStack = itemStack.copy();
                    newStack.setCount(inventory.get(i).getCount() + itemStack.getCount());
                    inventory.set(i, newStack);
                    changed = true;
                    break;
                }
            }
        }
        if (changed) {
            setChanged();
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        inventory = NonNullList.withSize(maxStorage, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.inventory);
        showFishBait = this.inventory.getFirst().getCount() > 0;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.inventory);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(inventory, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ContainerHelper.takeItem(inventory, slot);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                (double) this.worldPosition.getY() + 0.5D,
                (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    public boolean showFishBait() {
        return showFishBait;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return AVAILABLE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot == 0 && stack.getItem() instanceof FishingBait;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot > 0;
    }

    @Override
    public abstract Component getDisplayName();

    @Override
    public abstract AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player);
}