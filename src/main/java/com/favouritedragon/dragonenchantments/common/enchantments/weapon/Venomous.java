package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;

import java.util.Objects;

public class Venomous extends Enchantment {

	public Venomous() {
		super(Rarity.RARE, Objects.requireNonNull(ModEnchantments.WEAPONS), new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,
				EntityEquipmentSlot.OFFHAND});
		setName(DragonEnchants.MODID + ":" + "venomous");
		setRegistryName("venomous");
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 20 + 20 * (enchantmentLevel - 1);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 60;
	}

	@Override
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		super.onEntityDamaged(user, target, level);
		if (target instanceof EntityLivingBase) {
			((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.POISON, 40 * level, level - 1));
		}
	}
}
