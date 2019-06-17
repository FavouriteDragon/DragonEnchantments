package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DragonSlayer extends Enchantment {
	public DragonSlayer() {
		super(Rarity.RARE, Objects.requireNonNull(ModEnchantments.WEAPONS), new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setRegistryName("dragon_slayer");
		setName(DragonEnchants.MODID + ":" + "dragon_slayer");
	}

	@SubscribeEvent
	public static void onDragonHurt(LivingHurtEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		if (attacker instanceof EntityLivingBase && !attacker.world.isRemote) {
			int level = DragonUtils.getHeldLevelForEnchantment((EntityLivingBase) attacker, ModEnchantments.dragonSlayer, event);
			if (level > 0) {
				float amount = event.getAmount() > event.getEntityLiving().getHealth() ? event.getAmount() : event.getEntityLiving().getHealth();
				if (event.getEntityLiving() instanceof EntityDragon) {
					event.setAmount(event.getAmount() * (1 + 0.1F * level));
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
		return 20 + 80 * enchantmentLevel;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 200;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
}
