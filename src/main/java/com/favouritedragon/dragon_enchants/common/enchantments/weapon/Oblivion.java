package com.favouritedragon.dragon_enchants.common.enchantments.weapon;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import com.favouritedragon.dragon_enchants.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;

public class Oblivion extends Enchantment {

	//Low chance for instantaneous death
	public Oblivion() {
		super(Rarity.VERY_RARE, ModEnchantments.WEAPONS, new EntityEquipmentSlot[] {EntityEquipmentSlot.OFFHAND, EntityEquipmentSlot.MAINHAND});
		setRegistryName(DragonEnchants.MODID + ":" + "oblivion");
		setName("oblivion");
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 75;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 200;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		super.onEntityDamaged(user, target, level);
		if (DragonUtils.getRandomNumberInRange(1, 100) <= 5 && target instanceof EntityLivingBase) {
			target.playSound(SoundEvents.ENTITY_GHAST_DEATH, 1.5F, 0.8F + user.world.rand.nextFloat() / 5);
			((EntityLivingBase) target).setHealth(0);
			((EntityLivingBase) target).onDeath(DamageSource.OUT_OF_WORLD);
		}
	}
}
