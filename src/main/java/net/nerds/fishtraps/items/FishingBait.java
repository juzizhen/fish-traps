package net.nerds.fishtraps.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.config.FishTrapValues;

public class FishingBait extends Item {
    private static final String USES_KEY = "RemainingUses";

    public FishingBait(Properties properties) {
        super(properties.stacksTo(64));
    }

    public static int getMaxUses() {
        return Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.FISH_BAIT_DURABILITY);
    }

    public static int getRemainingUses(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null || data.isEmpty()) return getMaxUses();
        CompoundTag tag = data.copyTag();
        return tag.getInt(USES_KEY).orElse(getMaxUses());
    }

    public static void setRemainingUses(ItemStack stack, int uses) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(USES_KEY, uses));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        int remaining = getRemainingUses(stack);
        return remaining < getMaxUses();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int remaining = getRemainingUses(stack);
        return Math.round(13.0f * remaining / getMaxUses());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int remaining = getRemainingUses(stack);
        float ratio = (float) remaining / getMaxUses();
        return java.awt.Color.HSBtoRGB(ratio / 3.0f, 1.0f, 1.0f) & 0x00FFFFFF;
    }
}