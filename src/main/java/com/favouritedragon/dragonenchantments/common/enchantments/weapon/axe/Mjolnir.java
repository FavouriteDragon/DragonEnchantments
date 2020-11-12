package com.favouritedragon.dragonenchantments.common.enchantments.weapon.axe;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

//Thunder aspect but big boom
public class Mjolnir extends Enchantment {

    public Mjolnir(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(Rarity.VERY_RARE, ModEnchantments.AXES, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setName(DragonEnchants.MODID + ":" + "mjolnir");
        setRegistryName("mjolnir");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
