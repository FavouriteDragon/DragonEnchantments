package com.favouritedragon.dragonenchantments.common.enchantments.armour;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SolarPowered extends Enchantment {
	//Restores health and durability
	public SolarPowered() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS,
		EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "solar_powered");
		setRegistryName("solar_powered");
	}

	@SubscribeEvent
	public static void onSun(LivingEvent.LivingUpdateEvent event) {

	}


	@Override
	public int getMaxLevel() {
		return 5;
	}


}
