package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Lifesteal extends Enchantment {
	public Lifesteal() {
		super(Rarity.RARE, ModEnchantments.WEAPONS, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setRegistryName("life_steal");
		setName(DragonEnchants.MODID + ":" + "life_steal");
	}

	@SubscribeEvent
	public static void onLifeSteal(LivingHurtEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		if (attacker instanceof EntityLivingBase && !attacker.world.isRemote) {
			ItemStack stack = null;
			int level;
			ItemStack mainStack = ((EntityLivingBase) attacker).getHeldItemMainhand();
			ItemStack offStack = ((EntityLivingBase) attacker).getHeldItemOffhand();
			if (event.getSource().getImmediateSource() instanceof EntityArrow) {
				if (mainStack.isItemEnchanted() && mainStack.getItem() == Items.BOW) {
					stack = mainStack;
				}
				else if (offStack.isItemEnchanted() && offStack.getItem() == Items.BOW) {
					stack = offStack;
				}
			}
			else {
				if (mainStack.isItemEnchanted()) {
					stack = mainStack;
				}
				else if (offStack.isItemEnchanted()) {
					stack = offStack;
				}
			}
			if(stack == null) return;
			level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, stack);
			if (level > 0) {
				((EntityLivingBase) attacker).heal(event.getAmount() / 10 * level * level);
			}
		}
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + 40 * (enchantmentLevel);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 50;
	}
}
