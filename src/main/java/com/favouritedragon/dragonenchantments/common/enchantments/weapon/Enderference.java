package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import java.util.List;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Enderference extends Enchantment {
	public Enderference() {
		super(Rarity.RARE, ModEnchantments.WEAPONS,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setRegistryName("enderference");
		setName(DragonEnchants.MODID + ":" + "enderference");
	}

	@SubscribeEvent
	public static void onEndermanTeleport(EnderTeleportEvent event) {
		if (event.getEntityLiving() instanceof EntityEnderman) {
			List<EntityLivingBase> entities = event.getEntityLiving().getEntityWorld().getEntities(
					EntityLivingBase.class,
					e -> DragonUtils.getHeldLevelForEnchantment(e, ModEnchantments.enderference) > 0);
			for (EntityLivingBase entity : entities) {
				int level = DragonUtils.getHeldLevelForEnchantment(entity, ModEnchantments.enderference);
				if (entity.getDistance(event.getEntityLiving()) < 5 + level * 2) {
					event.setCanceled(true);
					return;
				}
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
