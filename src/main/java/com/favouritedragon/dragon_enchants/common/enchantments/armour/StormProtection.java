package com.favouritedragon.dragon_enchants.common.enchantments.armour;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class StormProtection extends Enchantment {

	public StormProtection() {
		super(Rarity.RARE, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
				EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "storm_protection");
		setRegistryName("storm_protection");
	}

	@SubscribeEvent
	public static void onLightingHurt(LivingHurtEvent event) {
		if (event.getSource() == DamageSource.LIGHTNING_BOLT) {
			if (event.getEntityLiving() != null) {
				EntityLivingBase entity = event.getEntityLiving();
				int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.stormProtection, entity);
				if (level > 0) {
					float modifier = 1 - level / 10F;
					Iterable<ItemStack> armour = entity.getEquipmentAndArmor();
					for (ItemStack stack : armour) {
						int armourLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.stormProtection, stack);
						modifier -= armourLevel / 40F;
					}
					event.setAmount(event.getAmount() * modifier);
				}
			}
		}
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}
