package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DragonSlayer extends Enchantment {
	public DragonSlayer() {
		super(Rarity.RARE, ModEnchantments.WEAPONS, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setRegistryName("dragon_slayer");
		setName(DragonEnchants.MODID + ":" + "dragon_slayer");
	}

	@SubscribeEvent
	public static void onDragonHurt(LivingHurtEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		if (attacker != null && !attacker.world.isRemote) {
			ItemStack stack = null;
			if (attacker instanceof EntityLivingBase && ((EntityLivingBase) attacker).getHeldItemMainhand() != ItemStack.EMPTY) {
				stack = ((EntityLivingBase) attacker).getHeldItemMainhand();
			} else if (attacker instanceof EntityLivingBase && ((EntityLivingBase) attacker).getHeldItemOffhand() != ItemStack.EMPTY) {
				stack = ((EntityLivingBase) attacker).getHeldItemOffhand();
			}
			assert stack != null;
			int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.dragonSlayer, stack);
			if (level > 0 && event.getEntityLiving() instanceof EntityDragon) {
				event.setAmount(event.getAmount() * (1 + 0.1F * level));
			}
		}
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + 20 * (enchantmentLevel - 1);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
}
