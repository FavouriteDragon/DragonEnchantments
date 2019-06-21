package com.favouritedragon.dragonenchantments.common.enchantments.armour.leggings;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sun.awt.ModalExclude;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Dolphin extends Enchantment {

	public Dolphin() {
		super(Rarity.RARE, EnumEnchantmentType.ARMOR_LEGS, new EntityEquipmentSlot[]{EntityEquipmentSlot.LEGS});
		setName(DragonEnchants.MODID + ":" + "dolphin");
		setRegistryName("dolphin");
	}

	@SubscribeEvent
	public static void onSwimEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase entity = event.getEntityLiving();
			if (entity.isInWater()) {
				int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.dolphin, entity);
				if (level > 0) {
					entity.motionX *= level * 1.25F;
					entity.motionY *= 0.85F - level / 20F;
					entity.motionZ *= level * 1.25F;
					entity.velocityChanged = true;
				}
			}
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
