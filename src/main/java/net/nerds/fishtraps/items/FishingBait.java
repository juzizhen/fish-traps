package net.nerds.fishtraps.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.config.FishTrapValues;

public class FishingBait extends Item {
    public FishingBait() {
        super(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM,
                        Identifier.of(Fishtraps.MODID, "fish_trap_bait")))
                .maxDamage(Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.FISH_BAIT_DURABILITY)));
    }
}
