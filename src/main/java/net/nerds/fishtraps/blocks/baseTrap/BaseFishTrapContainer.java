package net.nerds.fishtraps.blocks.baseTrap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.nerds.fishtraps.inventory.BaitSlot;
import net.nerds.fishtraps.inventory.OutputSlot;
import net.nerds.fishtraps.items.FishingBait;

public abstract class BaseFishTrapContainer extends ScreenHandler {
    public final Inventory inventory;
    public final PlayerInventory playerInventory;
    public final World world;

    protected BaseFishTrapContainer(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getEntityWorld();

        int invCountNum = 0;
        this.addSlot(new BaitSlot(inventory, invCountNum++, 8, 118));

        for (int outInvIndex = 0; outInvIndex < 5; ++outInvIndex) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new OutputSlot(inventory, invCountNum++, (8 + column * 18), 18 + (outInvIndex * 18)));
            }
        }

        int playerInvIndex;
        for (playerInvIndex = 0; playerInvIndex < 3; ++playerInvIndex) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlot(new Slot(playerInventory, var4 + playerInvIndex * 9 + 9, 8 + var4 * 18, 142 + playerInvIndex * 18));
            }
        }
        for (playerInvIndex = 0; playerInvIndex < 9; ++playerInvIndex) {
            this.addSlot(new Slot(playerInventory, playerInvIndex, 8 + playerInvIndex * 18, 200));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            int baitSlotIndex = 0;
            int playerInvStart = 1;
            int playerInvEnd = this.slots.size();

            if (slotIndex != baitSlotIndex) {
                if (originalStack.getItem() instanceof FishingBait) {
                    ItemStack singleItem = originalStack.copyWithCount(1);

                    if (this.insertItem(singleItem, baitSlotIndex, baitSlotIndex + 1, false)) {
                        originalStack.decrement(1);
                        if (originalStack.isEmpty()) {
                            slot.setStack(ItemStack.EMPTY);
                        } else {
                            slot.markDirty();
                        }
                    } else {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (!this.insertItem(originalStack, playerInvStart, playerInvEnd, true)) {
                    return ItemStack.EMPTY;
                }

                if (originalStack.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
            }
            slot.onTakeItem(player, originalStack);
        }
        return newStack;
    }
}