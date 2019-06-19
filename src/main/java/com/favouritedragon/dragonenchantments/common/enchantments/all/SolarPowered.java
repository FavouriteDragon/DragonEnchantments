package com.favouritedragon.dragonenchantments.common.enchantments.all;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class SolarPowered extends Enchantment {
	//Restores health and durability
	public SolarPowered() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ALL, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS,
				EntityEquipmentSlot.FEET, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setName(DragonEnchants.MODID + ":" + "solar_powered");
		setRegistryName("solar_powered");
	}

	@SubscribeEvent
	public static void onSun(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			if (entity.world.getSunBrightness(1.0F) >= 0.8F) {
				if (entity.world.canSeeSky(entity.getPosition()) && !entity.world.isRaining() && !entity.world.isThundering()) {
					int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.solarPowered, entity);
					if (level > 0) {
						if (!entity.isPotionActive(MobEffects.REGENERATION)) {
							entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20 + level * 5, level - 1, false, false));
						}
						Iterable<ItemStack> armour = entity.getEquipmentAndArmor();
						for (ItemStack stack : armour) {
							int armourLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.solarPowered, stack);
							if (entity.ticksExisted % 120 - level * 2 == 0 && DragonUtils.getRandomNumberInRange(1, 10) <= level) {
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


	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 20 + enchantmentLevel * 80;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 200;
	}
}
