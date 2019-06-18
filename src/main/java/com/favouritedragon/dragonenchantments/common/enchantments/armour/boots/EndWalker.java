package com.favouritedragon.dragonenchantments.common.enchantments.armour.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class EndWalker extends Enchantment {

	public EndWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "end_walker");
		setRegistryName("end_walker");
	}

	@SubscribeEvent
	public static boolean onAttackedIndirectly(LivingAttackEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase entity = event.getEntityLiving();
			if (event.getSource() instanceof EntityDamageSourceIndirect) {
				int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.endWalker, entity);
				if (level > 0) {
					if (DragonUtils.getRandomNumberInRange(1, 10) <= 2 * level) {
						for (int i = 0; i < 64; ++i) {
							if (DragonUtils.teleportRandomly(entity, 30)) {
								event.setCanceled(true);
								return true;
							}
						}
					}
				}
				return false;
			}
			return false;
		}
		return false;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
