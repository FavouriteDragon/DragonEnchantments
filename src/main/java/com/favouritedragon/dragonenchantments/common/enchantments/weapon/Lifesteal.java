package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Lifesteal extends Enchantment {
	public Lifesteal() {
		super(Rarity.RARE, Objects.requireNonNull(ModEnchantments.WEAPONS), new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setRegistryName("life_steal");
		setName(DragonEnchants.MODID + ":" + "life_steal");
	}

	//Has to be an attack event so if you kill an enemy you still steal life.
	@SubscribeEvent
	public static void onLifeSteal(LivingAttackEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		if (attacker instanceof EntityLivingBase && !attacker.world.isRemote) {
			int level = DragonUtils.getHeldLevelForEnchantment((EntityLivingBase) attacker, ModEnchantments.lifeSteal, event);
			if (level > 0) {
				float amount = event.getAmount() > event.getEntityLiving().getHealth() ? event.getAmount() : event.getEntityLiving().getHealth();
				((EntityLivingBase) attacker).heal(amount / 10 * level);
			}
		}
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + 40 * (enchantmentLevel);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 50;
	}
}
