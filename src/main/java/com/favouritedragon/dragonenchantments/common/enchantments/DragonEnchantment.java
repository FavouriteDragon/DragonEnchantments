package com.favouritedragon.dragonenchantments.common.enchantments;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public abstract class DragonEnchantment extends Enchantment {

	public DragonEnchantment(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@SubscribeEvent
	public static void attackEvents(LivingAttackEvent event) {
		onAttackEvent(event);
	}

	@SubscribeEvent
	public static void hurtEvents(LivingHurtEvent event) {
		onHurtEvent(event);
	}

	@SubscribeEvent
	public static void hurtEvents(LivingEvent.LivingUpdateEvent event) {
		onUpdateEvent(event);
	}


	public static void onAttackEvent(LivingAttackEvent event) {

	}

	public static void onHurtEvent(LivingHurtEvent event) {

	}

	public static void onUpdateEvent(LivingEvent.LivingUpdateEvent event) {

	}

}
