package com.favouritedragon.dragonenchantments.common.enchantments.armour.leggings;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sun.awt.ModalExclude;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Dolphin extends Enchantment {

	private static final UUID MOVEMENT_MODIFIER_ID = UUID.fromString("bf6f2d30-9426-452a-8510-9537adb0237c");

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
					float multiplier = 2F + level / 5F;
					applyMovementModifier(entity, multiplier);
				}
			}
			else if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(MOVEMENT_MODIFIER_ID) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MOVEMENT_MODIFIER_ID);
			}
		}
	}

	private static void applyMovementModifier(EntityLivingBase entity, float multiplier) {

		IAttributeInstance moveSpeed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		moveSpeed.removeModifier(MOVEMENT_MODIFIER_ID);

		moveSpeed.applyModifier(new AttributeModifier(MOVEMENT_MODIFIER_ID, "Dolphin Modifier", multiplier, 1));

	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
