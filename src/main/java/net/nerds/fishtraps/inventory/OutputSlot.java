package net.nerds.fishtraps.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OutputSlot extends Slot {

    public OutputSlot(Container inventory, int invIndex, int x, int y) {
        super(inventory, invIndex, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
}