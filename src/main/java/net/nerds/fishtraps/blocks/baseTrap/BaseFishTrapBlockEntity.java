package net.nerds.fishtraps.blocks.baseTrap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.config.FishTrapValues;
import net.nerds.fishtraps.items.FishingBait;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class BaseFishTrapBlockEntity extends BlockEntity implements SidedInventory, NamedScreenHandlerFactory {

    private long tickCounter = 0;
    private final long tickValidator;
    private final long tickValidatorPenalty;
    private final long tickCounterChecker;
    private final int lureLevel;
    private final int luckOfTheSeaLevel;
    private final boolean shouldPenalty;
    private final int maxStorage = 46;
    private boolean showFishBait = false;
    public DefaultedList<ItemStack> inventory;

    public BaseFishTrapBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int fishDelay, int lureLevel, int luckOfTheSeaLevel) {
        super(type, pos, state);
        inventory = DefaultedList.ofSize(maxStorage, ItemStack.EMPTY);
        this.tickValidator = fishDelay;
        this.tickValidatorPenalty = this.tickValidator * Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.PENALTY_MULTIPLIER_AMOUNT);
        this.lureLevel = lureLevel;
        this.luckOfTheSeaLevel = luckOfTheSeaLevel;
        this.shouldPenalty = Fishtraps.fishTrapsConfig.getBooleanProperty(FishTrapValues.SHOULD_PENALTY_MULTIPLIER);
        this.tickCounterChecker = this.shouldPenalty ? this.tickValidatorPenalty : this.tickValidator;
    }

    public void tick() {
        if (tickCounter >= getValidationNumber()) {
            tickCounter = 0;
            validateWaterAndFish();
        } else {
            tickCounter++;
        }
    }

    private long getValidationNumber() {
        showFishBait = this.inventory.get(0).getCount() > 0;
        if (!showFishBait && this.shouldPenalty) {
            return this.tickValidatorPenalty;
        } else {
            return this.tickValidator;
        }
    }

    private void validateWaterAndFish() {
        if (world != null) {
            if (!world.isClient) {
              boolean isSurroundedByWater = true;
               Iterable<BlockPos> waterCheckIterator = BlockPos.iterate(
                      new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1),
                       new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 1));

               for (BlockPos blockPos : waterCheckIterator) {
                   Block block = world.getBlockState(blockPos).getBlock();
                   if (world.getBlockEntity(pos) != null && (block != Blocks.WATER && !(block instanceof BaseFishTrapBlock))) {
                       isSurroundedByWater = false;
                       break;
                  }
              }

              if (isSurroundedByWater) {
                  fish();
              }
            }
        }
    }

    private void fish() {
        ItemStack itemStack = new ItemStack(Items.FISHING_ROD);

        Map<Enchantment, Integer> enchantments = Map.of(
                Enchantments.LURE, this.lureLevel,
                Enchantments.LUCK_OF_THE_SEA, this.luckOfTheSeaLevel
        );
        EnchantmentHelper.set(enchantments, itemStack);

        LootContextParameterSet.Builder lootContextBuilder = new LootContextParameterSet.Builder((ServerWorld) this.world)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                .add(LootContextParameters.TOOL, itemStack)
                .luck((float) this.luckOfTheSeaLevel);

        LootTable lootTable = Objects.requireNonNull(Objects.requireNonNull(this.world).getServer()).getLootManager()
                .getLootTable(LootTables.FISHING_GAMEPLAY);
        List<ItemStack> list = lootTable.generateLoot(lootContextBuilder.build(LootContextTypes.FISHING));

        addItemsToInventory(list);

        if (showFishBait) {
            ItemStack fishBait = inventory.get(0);
            if (fishBait.getItem() instanceof FishingBait) {
                if (fishBait.damage(1, world.random, null)) {
                    inventory.set(0, ItemStack.EMPTY);
                    markDirty();
                }
            }
        }
    }

    private void addItemsToInventory(List<ItemStack> itemStackList) {
        for (ItemStack itemStack : itemStackList) {
            for (int i = 1; i < inventory.size(); i++) {
                if (inventory.get(i).isEmpty()) {
                    inventory.set(i, itemStack.copy());
                    markDirty();
                    break;
                } else if (ItemStack.areItemsEqual(inventory.get(i), itemStack) &&
                        (inventory.get(i).getCount() + itemStack.getCount() <= itemStack.getMaxCount()) &&
                        itemStack.isStackable()) {
                    ItemStack newStack = itemStack.copy();
                    newStack.setCount(inventory.get(i).getCount() + itemStack.getCount());
                    inventory.set(i, newStack);
                    markDirty();
                    break;
                }
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory = DefaultedList.ofSize(maxStorage, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        showFishBait = this.inventory.get(0).getCount() > 0;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public int size() {
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
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(inventory, slot);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (Objects.requireNonNull(this.world).getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double) this.pos.getX() + 0.5D,
                    (double) this.pos.getY() + 0.5D,
                    (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        inventory.clear();
        markDirty();
    }

    public boolean showFishBait() {
        return showFishBait;
    }

    // SidedInventory implementation
    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] arr = new int[46];
        for (int i = 0; i < 46; i++) {
            arr[i] = i;
        }
        return arr;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return slot == 0 && stack.getItem() instanceof FishingBait;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot > 0;
    }

    @Override
    public abstract Text getDisplayName();

    @Override
    public abstract ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player);
}