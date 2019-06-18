package com.favouritedragon.dragonenchantments.common.enchantments.boots;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EndWalker extends Enchantment {

	public EndWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
