package com.favouritedragon.dragonenchants.enchantments;

import com.favouritedragon.dragonenchants.DragonEnchants;
import com.favouritedragon.dragonenchants.enchantments.all.weapon.melee.ThunderAspect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantments {

    public static final EnchantmentType WEAPON = EnchantmentType.create("weapon", (item) ->
            item instanceof SwordItem || item instanceof TridentItem || item instanceof AxeItem
            || item instanceof BowItem || item instanceof CrossbowItem);
    public static final EnchantmentType MELEE_WEAPON = EnchantmentType.create("melee_weapon", item ->
            item instanceof SwordItem || item instanceof AxeItem || item instanceof TridentItem);
    public static final EnchantmentType RANGED_WEAPON = EnchantmentType.create("ranged_weapon", item ->
            item instanceof BowItem || item instanceof CrossbowItem);


    public static final Enchantment
        THUNDER_ASPECT = new ThunderAspect();


    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        IForgeRegistry<Enchantment> registry = event.getRegistry();

        registry.register(THUNDER_ASPECT);
    }
}
