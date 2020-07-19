package com.favouritedragon.dragon_enchants.common.enchantments.armour.chest;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class Kineticist extends Enchantment {

	//Absorbs damage, releases an explosion. Waiting on the rendering li
	protected Kineticist(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}
}
