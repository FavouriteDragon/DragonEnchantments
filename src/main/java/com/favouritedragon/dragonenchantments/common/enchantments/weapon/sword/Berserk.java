package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Berserk extends Enchantment {

	public Berserk() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		setName(DragonEnchants.MODID + ":" + "berserk");
		setRegistryName("berserk");
	}

	@SubscribeEvent
	public static void onAttackEvent(LivingAttackEvent event) {

	}
	@Override
	public int getMaxLevel() {
		return 4;
	}

}
