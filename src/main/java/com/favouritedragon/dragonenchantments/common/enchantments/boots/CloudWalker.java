package com.favouritedragon.dragonenchantments.common.enchantments.boots;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CloudWalker extends Enchantment {

	protected CloudWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
	}

	@SubscribeEvent
	public void onFallEvent(LivingEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			if (entity.motionY < 0) {
				entity.motionY += 0.08;
			}
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 100 * enchantmentLevel;
	}
}
