package net.nerds.fishtraps.inventory;

import net.minecraft.screen.slot.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * Output-only inventory slot — prevents item insertion.
 */
public class OutputSlot extends Slot {

    public OutputSlot(Inventory inventory, int invIndex, int x, int y) {
        super(inventory, invIndex, x, y);
    }

    @Override
    public boolean canInsert(ItemStack itemStack) {
        return false;
    }
}
