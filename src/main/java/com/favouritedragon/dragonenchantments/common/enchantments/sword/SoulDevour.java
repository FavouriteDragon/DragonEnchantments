package com.favouritedragon.dragonenchantments.common.enchantments.sword;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class SoulDevour extends Enchantment {

	private List<Entity> killed;

	public SoulDevour() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {

	}

	private List getEnemiesKilled() {
		return this.killed;
	}

	private void addEnemiesKilled(Entity killed) {
		this.killed.add(killed);
	}

	private void clearEnemiesKilled() {
		this.killed.clear();
	}
}
