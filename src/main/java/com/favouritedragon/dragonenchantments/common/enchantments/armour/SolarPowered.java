package com.favouritedragon.dragonenchantments.common.enchantments.armour;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class SolarPowered extends Enchantment {
	//Restores health and durability
	public SolarPowered() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS,
		EntityEquipmentSlot.FEET});
	}
}
