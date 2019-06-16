package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Lifesteal extends Enchantment {
	public Lifesteal() {
		super(Rarity.RARE, ModEnchantments.WEAPONS, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setRegistryName(DragonEnchants.MODID, "life_steal");
		setName("life_steal");
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@SubscribeEvent
	public static void onLifeStea(LivingHurtEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		if (attacker != null && !attacker.world.isRemote) {
			ItemStack stack = null;
			if (attacker instanceof EntityLivingBase && ((EntityLivingBase) attacker).getHeldItemMainhand() != ItemStack.EMPTY) {
				stack = ((EntityLivingBase) attacker).getHeldItemMainhand();
			} else if (attacker instanceof EntityLivingBase && ((EntityLivingBase) attacker).getHeldItemOffhand() != ItemStack.EMPTY) {
				stack = ((EntityLivingBase) attacker).getHeldItemOffhand();
			}
			assert stack != null;
			int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, stack);
			if (level > 0) {
				((EntityLivingBase) attacker).heal(event.getAmount() / 10 * level * level);
			}
		}
	}
}
