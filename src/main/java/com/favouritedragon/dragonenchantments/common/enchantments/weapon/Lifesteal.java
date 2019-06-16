package com.favouritedragon.dragonenchantments.common.enchantments.weapon;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Lifesteal extends Enchantment {
	public Lifesteal() {
		super(Rarity.RARE, ModEnchantments.WEAPONS, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setRegistryName("life_steal");
		setName(DragonEnchants.MODID + ":" + "life_steal");
	}

	@SubscribeEvent
	public static void onLifeSteal(LivingHurtEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		if (attacker instanceof EntityLivingBase && !attacker.world.isRemote){
			ItemStack stack = null;
			if (((EntityLivingBase) attacker).getHeldItemMainhand().isItemEnchanted()) {
				stack = ((EntityLivingBase) attacker).getHeldItemMainhand();
			}
			else if (((EntityLivingBase) attacker).getHeldItemOffhand().isItemEnchanted()) {
				stack = ((EntityLivingBase) attacker).getHeldItemOffhand();
			}
			if(stack == null) return;
			int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lifeSteal, stack);
			if (level > 0) {
				((EntityLivingBase) attacker).heal(event.getAmount() / 10 * level * level);
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
		return 10 + 20 * (enchantmentLevel);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 50;
	}
}
