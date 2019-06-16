package com.favouritedragon.dragonenchantments.common.enchantments.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ThunderAspect extends Enchantment {
	public ThunderAspect() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
	}

	@SubscribeEvent
	public static void onSwingEvent(LivingAttackEvent event) {
		Entity attacker = event.getSource().getTrueSource();
			if (attacker instanceof EntityLivingBase) {
				if (((EntityLivingBase) attacker).getHeldItemMainhand().isItemEnchanted()) {

				}
			}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + enchantmentLevel * 50;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 60;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
}
