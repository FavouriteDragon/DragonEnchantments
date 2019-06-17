package com.favouritedragon.dragonenchantments.common.enchantments.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ThunderAspect extends Enchantment {
	public ThunderAspect() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		setRegistryName("thunder_aspect");
		setName(DragonEnchants.MODID + ":" + "thunder_aspect");
	}

	@SubscribeEvent
	public static void onSwingEvent(LivingAttackEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		EntityLivingBase hurt = event.getEntityLiving();
		if (attacker instanceof EntityLivingBase) {
			ItemStack stack = ((EntityLivingBase) attacker).getHeldItemMainhand();
			if (stack.isItemEnchanted()) {
				if (attacker instanceof EntityPlayer) {
					//This requires some weird stuff for a sweep attack (wtf, mojang)
					float attackMult = (float) ((EntityPlayer) attacker).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
					float modifier = EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
					float mult = ((EntityPlayer) attacker).getCooledAttackStrength(0.5F);
					boolean doCrit = mult > 0.9F;
					modifier *= mult;
					attackMult = attackMult * (0.2F + mult * mult * 0.8F);
					attackMult += modifier;
					boolean crit = doCrit && attacker.fallDistance > 0.0F && !attacker.onGround && !((EntityPlayer) attacker).isOnLadder() &&
							!attacker.isInWater() && !(((EntityPlayer) attacker).isPotionActive(MobEffects.BLINDNESS) && !attacker.isRiding() &&
							!attacker.isSprinting() && hurt instanceof EntityLivingBase);

					net.minecraftforge.event.entity.player.CriticalHitEvent hitResult =
							net.minecraftforge.common.ForgeHooks.getCriticalHit((EntityPlayer) attacker, hurt, crit, crit ? 1.5F : 1.0F);
					if (hitResult != null) {
						attackMult *= hitResult.getDamageModifier();
					}
					//Checks to see if the damage is equivalent to a sweeping attack
					if (event.getAmount() == (1.0F + EnchantmentHelper.getSweepingDamageRatio((EntityLivingBase) attacker) * attackMult)) {
						int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
						if (level > 0 && hurt != null) {
							hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT, event.getAmount() / 10 * level * 2);
							Vec3d lookVec = attacker.getLookVec();
							hurt.motionX += lookVec.x * (1 + 0.2 * level);
							hurt.motionY += lookVec.y * (1 + 0.2 * level);
							hurt.motionZ += lookVec.z * (1 + 0.2 * level);
							if (attacker.world.isRemote) {
								attacker.world.playSound(hurt.posX, hurt.posY, hurt.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.PLAYERS,
										1.0F + attacker.world.rand.nextFloat(), 1.0F + attacker.world.rand.nextFloat(), true);
							}
						}
					} else {
						int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
						if (level > 0 && hurt != null) {
							hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT, event.getAmount() / 10 * level);
							Vec3d lookVec = attacker.getLookVec();
							hurt.motionX += lookVec.x * (1 + 0.15 * level);
							hurt.motionY += lookVec.y * (1 + 0.15 * level);
							hurt.motionZ += lookVec.z * (1 + 0.15 * level);
						}
					}
				} else {
					int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
					if (level > 0 && hurt != null) {
						hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT, event.getAmount() / 10 * level);
						Vec3d lookVec = attacker.getLookVec();
						hurt.motionX += lookVec.x * (1 + 0.15 * level);
						hurt.motionY += lookVec.y * (1 + 0.15 * level);
						hurt.motionZ += lookVec.z * (1 + 0.15 * level);
					}
				}

			}
		}

	}


	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + enchantmentLevel * 50;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 60;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

}
