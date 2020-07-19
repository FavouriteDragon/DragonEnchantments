package com.favouritedragon.dragon_enchants.common.enchantments.weapon.sword;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ArrowSlicer extends Enchantment {

	public ArrowSlicer() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		setName(DragonEnchants.MODID + ":" + "arrow_slicer");
		setRegistryName("arrow_slicer");
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
