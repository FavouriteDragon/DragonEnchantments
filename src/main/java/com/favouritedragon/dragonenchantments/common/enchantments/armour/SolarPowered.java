package com.favouritedragon.dragonenchantments.common.enchantments.armour;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

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
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			if (entity.world.isDaytime()) {
				if(entity.world.canSeeSky(entity.getPosition()) && !entity.world.isRaining() && !entity.world.isThundering()) {
					int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.solarPowered, entity);
					if (level > 0) {
						if (!entity.isPotionActive(MobEffects.REGENERATION)) {
							entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, level - 1, false, false));
						}
						Iterable<ItemStack> armour = entity.getEquipmentAndArmor();
						for (ItemStack stack : armour) {
							int armourLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.solarPowered, stack);
							if (entity.ticksExisted % 400 - level * 20 == 0) {
								stack.damageItem(-armourLevel, entity);
							}

						}
					}
				}
			}
		}
	}


	@Override
	public int getMaxLevel() {
		return 5;
	}


}
