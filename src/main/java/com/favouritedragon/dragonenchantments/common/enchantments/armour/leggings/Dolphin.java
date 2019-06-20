package com.favouritedragon.dragonenchantments.common.enchantments.armour.leggings;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class Dolphin extends Enchantment {

	public Dolphin() {
		super(Rarity.RARE, EnumEnchantmentType.ARMOR_LEGS, new EntityEquipmentSlot[]{EntityEquipmentSlot.LEGS});
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
