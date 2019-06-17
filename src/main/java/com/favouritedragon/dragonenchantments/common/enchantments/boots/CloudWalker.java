package com.favouritedragon.dragonenchantments.common.enchantments.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class CloudWalker extends Enchantment {

	private static HashMap<String, Integer> timesJumped;

	public CloudWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "cloud_walker");
		setRegistryName("cloud_walker");
	}

	@SubscribeEvent
	public static void onFallEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, entity);
			if (level > 0) {
				if (entity.motionY <= 0) {
					entity.motionY += 0.06 * level;
				}
			}
		}
	}

	@SubscribeEvent
	public static void jumpEvent(LivingEvent.LivingJumpEvent event) {

	}

	@SubscribeEvent
	public void clearDoubleJump(LivingFallEvent event) {

	}
	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 100 * enchantmentLevel;
	}

	private static void addTimesJumped(String UUID, int jumped) {
		timesJumped.put(UUID, jumped);
	}

	private static HashMap<String, Integer> getTimesJumped() {
		return timesJumped;
	}
}
