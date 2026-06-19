package net.nerds.fishtraps.blocks.baseTrap;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.nerds.fishtraps.inventory.BaitSlot;
import net.nerds.fishtraps.inventory.OutputSlot;
import net.nerds.fishtraps.items.FishingBait;

public abstract class BaseFishTrapContainer extends AbstractContainerMenu {
    public final Container inventory;
    public final Inventory playerInventory;
    public final Level level;

    protected BaseFishTrapContainer(MenuType<?> type, int containerId, Inventory playerInventory, Container inventory) {
        super(type, containerId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.level = playerInventory.player.level();

        int invCountNum = 0;
        this.addSlot(new BaitSlot(inventory, invCountNum++, 8, 118));

        for (int outInvIndex = 0; outInvIndex < 3; ++outInvIndex) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new OutputSlot(inventory, invCountNum++, (8 + column * 18), 18 + (outInvIndex * 18)));
            }
        }
        for (int outInvIndex = 3; outInvIndex < 5; ++outInvIndex) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new OutputSlot(inventory, invCountNum++, (8 + column * 18), 18 + (outInvIndex * 18) - 1));
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
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot initSlot = this.slots.get(slotIndex);
        if (!initSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack originalItem = initSlot.getItem().copy();

        if (initSlot.container == player.getInventory()) {
            ItemStack baitItem = initSlot.getItem();
            Slot baitSlot = this.slots.getFirst();
            if (isFishBait(baitItem) && !baitSlot.hasItem()) {
                ItemStack singleBait = baitItem.copy();
                singleBait.setCount(1);
                baitSlot.set(singleBait);
                baitItem.shrink(1);
                initSlot.set(baitItem.isEmpty() ? ItemStack.EMPTY : baitItem);

                return singleBait.copy();
            }
            return ItemStack.EMPTY;
        } else {
            ItemStack singleItem = originalItem.copy();
            singleItem.setCount(1);

            if (player.getInventory().add(originalItem)) {
                initSlot.set(ItemStack.EMPTY);
                return originalItem;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    private boolean isFishBait(ItemStack itemStack) {
        return itemStack.getItem() instanceof FishingBait;
    }
}