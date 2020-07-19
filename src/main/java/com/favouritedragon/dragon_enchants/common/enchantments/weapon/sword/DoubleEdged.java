package com.favouritedragon.dragon_enchants.common.enchantments.weapon.sword;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import com.favouritedragon.dragon_enchants.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DoubleEdged extends Enchantment {

	private static HashMap<String, Boolean> isCrit = new HashMap<>();

	public DoubleEdged() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		setName(DragonEnchants.MODID + ":" + "double_edge");
		setRegistryName("double_edge");
	}

	//Ensures isCrit is never null
	@SubscribeEvent
	public static void onEnchantmentGet(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase entity = event.getEntityLiving();
			int level = DragonUtils.getHeldLevelForEnchantment(entity, ModEnchantments.doubleEdge);
			if (level > 0) {
				if (!isCrit.containsKey(entity.getUniqueID().toString())) {
					setIsCrit(entity.getUniqueID().toString(), false);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onCritEvent(CriticalHitEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase base = event.getEntityLiving();
			int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.doubleEdge, base.getHeldItemMainhand());
			if (level > 0) {
				event.setDamageModifier(event.getDamageModifier() + (level / 5F));
				setIsCrit(base.getUniqueID().toString(), true);
			}
		}
	}

	@SubscribeEvent
	public static void onHurtCritEvent(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.doubleEdge, attacker.getHeldItemMainhand());
			if (level > 0) {
				if (isCrit(attacker.getUniqueID().toString())) {
					attacker.attackEntityFrom(DamageSource.causeThornsDamage(attacker), event.getAmount() / (2 + level));
					attacker.setPosition(attacker.posX, attacker.posY, attacker.posZ);
				}
			}
		}
	}

	private static void setIsCrit(String UUID, boolean crit) {
		if (isCrit.containsKey(UUID)) {
			isCrit.replace(UUID, crit);
		} else {
			isCrit.put(UUID, crit);
		}
	}

	private static boolean isCrit(String UUID) {
		return isCrit.get(UUID) == null ? false : isCrit.get(UUID);
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}
