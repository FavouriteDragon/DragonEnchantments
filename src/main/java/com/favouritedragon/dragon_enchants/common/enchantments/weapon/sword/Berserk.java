package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
	public static void onAttackEvent(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase source = (EntityLivingBase) event.getSource().getTrueSource();
			int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.berserk, source.getHeldItemMainhand());
			if (level > 0) {
				float modifier = (source.getMaxHealth() - source.getHealth()) / (4F / level);
				event.setAmount(event.getAmount() * modifier / 10);
			}
		}
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 80;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

}
