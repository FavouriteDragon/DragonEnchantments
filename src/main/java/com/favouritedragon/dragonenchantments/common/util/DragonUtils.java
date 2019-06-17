package com.favouritedragon.dragonenchantments.common.util;

import com.favouritedragon.dragonenchantments.DragonEnchants;

import akka.japi.Pair;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DragonUtils {
	private static final DataParameter<Boolean> POWERED;

	static {
		POWERED = ReflectionHelper.getPrivateValue(EntityCreeper.class, null, "POWERED", "field_184714_b");
	}

	public static void chargeCreeper(EntityCreeper creeper) {
		creeper.getDataManager().set(POWERED, true);
	}

	@SubscribeEvent
	public static void onLightningHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityCreeper && event.getSource() == DamageSource.LIGHTNING_BOLT) {
			chargeCreeper((EntityCreeper) event.getEntityLiving());
		}
	}

	public static int getHeldLevelForEnchantment(EntityLivingBase entity, Enchantment enchantment) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		if (mainStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0) {
			stack = mainStack;
		} else if (offStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0) {
			stack = offStack;
		}
		return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
	}

	public static Pair<Integer,ItemStack> getHeldLevelForEnchantmentAndHeldItem(EntityLivingBase entity, Enchantment enchantment) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		if (mainStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0) {
			stack = mainStack;
		} else if (offStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0) {
			stack = offStack;
		}
		return new Pair<Integer,ItemStack>(stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0, stack);
	}

	public static int getHeldLevelForEnchantment(EntityLivingBase entity, Enchantment enchantment, LivingHurtEvent event) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		boolean mainLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0;
		boolean offLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0;
		if (event.getSource().getImmediateSource() instanceof EntityArrow) {
			if (mainStack.isItemEnchanted() && mainStack.getItem() == Items.BOW && mainLevel) {
				stack = mainStack;
			}
			else if (offStack.isItemEnchanted() && offStack.getItem() == Items.BOW && offLevel) {
				stack = offStack;
			}
		}
		else {
			if (mainStack.isItemEnchanted() && mainLevel) {
				stack = mainStack;
			} else if (offStack.isItemEnchanted() && offLevel) {
				stack = offStack;
			}
		}
		return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
	}

	public static int getHeldLevelForEnchantment(EntityLivingBase entity, Enchantment enchantment, LivingAttackEvent event) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		boolean mainLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0;
		boolean offLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0;
		if (event.getSource().getImmediateSource() instanceof EntityArrow) {
			if (mainStack.isItemEnchanted() && mainStack.getItem() == Items.BOW && mainLevel) {
				stack = mainStack;
			}
			else if (offStack.isItemEnchanted() && offStack.getItem() == Items.BOW && offLevel) {
				stack = offStack;
			}
		}
		else {
			if (mainStack.isItemEnchanted() && mainLevel) {
				stack = mainStack;
			} else if (offStack.isItemEnchanted() && offLevel) {
				stack = offStack;
			}
		}
		return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
	}
}
