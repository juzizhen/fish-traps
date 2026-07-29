package net.nerds.fishtraps.items;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.nerds.fishtraps.Fishtraps;
import net.nerds.fishtraps.config.FishTrapValues;

public class FishingBait extends Item {

    public static final ComponentType<Integer> BAIT_DAMAGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Fishtraps.MODID, "bait_damage"),
            ComponentType.<Integer>builder()
                    .codec(Codec.INT)
                    .packetCodec(PacketCodecs.VAR_INT)
                    .build()
    );

    public FishingBait() {
        super(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM,
                        Identifier.of(Fishtraps.MODID, "fish_trap_bait")))
                .maxCount(64));
    }

    public static int getMaxBaitDamage() {
        return Fishtraps.fishTrapsConfig.getProperty(FishTrapValues.FISH_BAIT_DURABILITY);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getOrDefault(BAIT_DAMAGE, 0) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int damage = stack.getOrDefault(BAIT_DAMAGE, 0);
        int maxDamage = getMaxBaitDamage();
        return Math.round(13.0f - (float) damage * 13.0f / (float) maxDamage);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int damage = stack.getOrDefault(BAIT_DAMAGE, 0);
        int maxDamage = getMaxBaitDamage();
        float f = Math.max(0.0f, (float) (maxDamage - damage) / (float) maxDamage);
        return MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }
}
