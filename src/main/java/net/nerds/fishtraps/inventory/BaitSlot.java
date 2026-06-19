package net.nerds.fishtraps.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.nerds.fishtraps.items.FishingBait;

public class BaitSlot extends Slot {

    public BaitSlot(Container inventory, int invIndex, int x, int y) {
        super(inventory, invIndex, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getItem() instanceof FishingBait;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}