package com.favouritedragon.dragonenchantments.common.util;

import akka.japi.Pair;
import com.favouritedragon.dragonenchantments.DragonEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Random;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DragonUtils {
	private static final DataParameter<Boolean> POWERED;

	static {
		POWERED = ReflectionHelper.getPrivateValue(EntityCreeper.class, null, "POWERED", "field_184714_b");
	}

	public static void chargeCreeper(EntityCreeper creeper) {
		creeper.getDataManager().set(POWERED, true);
	}

	@SubscribeEvent
	public static void onLightningHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityCreeper && event.getSource() == DamageSource.LIGHTNING_BOLT) {
			chargeCreeper((EntityCreeper) event.getEntityLiving());
		}
	}

	public static int getHeldLevelForEnchantment(EntityLivingBase entity, Enchantment enchantment) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		if (mainStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0) {
			stack = mainStack;
		} else if (offStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0) {
			stack = offStack;
		}
		return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
	}

	public static Pair<Integer, ItemStack> getHeldLevelForEnchantmentAndHeldItem(EntityLivingBase entity, Enchantment enchantment) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		if (mainStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0) {
			stack = mainStack;
		} else if (offStack.isItemEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0) {
			stack = offStack;
		}
		return new Pair<Integer, ItemStack>(stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0, stack);
	}

	public static int getHeldLevelForEnchantment(EntityLivingBase entity, Enchantment enchantment, LivingHurtEvent event) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		boolean mainLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0;
		boolean offLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0;
		if (event.getSource().getImmediateSource() instanceof EntityArrow) {
			if (mainStack.isItemEnchanted() && mainStack.getItem() == Items.BOW && mainLevel) {
				stack = mainStack;
			} else if (offStack.isItemEnchanted() && offStack.getItem() == Items.BOW && offLevel) {
				stack = offStack;
			}
		} else {
			if (mainStack.isItemEnchanted() && mainLevel) {
				stack = mainStack;
			} else if (offStack.isItemEnchanted() && offLevel) {
				stack = offStack;
			}
		}
		return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
	}

	public static int getHeldLevelForEnchantment(EntityLivingBase entity, Enchantment enchantment, LivingAttackEvent event) {
		ItemStack stack = null;
		ItemStack mainStack = entity.getHeldItemMainhand();
		ItemStack offStack = entity.getHeldItemOffhand();
		boolean mainLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0;
		boolean offLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0;
		if (event.getSource().getImmediateSource() instanceof EntityArrow) {
			if (mainStack.isItemEnchanted() && mainStack.getItem() == Items.BOW && mainLevel) {
				stack = mainStack;
			} else if (offStack.isItemEnchanted() && offStack.getItem() == Items.BOW && offLevel) {
				stack = offStack;
			}
		} else {
			if (mainStack.isItemEnchanted() && mainLevel) {
				stack = mainStack;
			} else if (offStack.isItemEnchanted() && offLevel) {
				stack = offStack;
			}
		}
		return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
	}


	public static boolean teleportRandomly(EntityLivingBase entity, double radiusMult) {
		double d0 = entity.posX + (entity.world.rand.nextDouble() - 0.5D) * radiusMult;
		double d1 = entity.posY + (entity.world.rand.nextInt((int) radiusMult) - radiusMult / 2);
		double d2 = entity.posZ + (entity.world.rand.nextDouble() - 0.5D) * radiusMult;
		return teleportTo(entity, d0, d1, d2);
	}

	public static boolean teleportTo(EntityLivingBase entity, double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean teleport = entity.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (teleport) {
			applyPlayerKnockback(entity);
			entity.world.playSound(null, entity.prevPosX, entity.prevPosY, entity.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
			entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return teleport;
	}

	public static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public static void applyPlayerKnockback(Entity target) {
		target.velocityChanged = true;
		if (target instanceof EntityPlayerMP) {
			((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
			((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityTeleport(target));

		}
	}

	/**
	 * Small util method to clean up code and prevent having those < checks
	 *
	 * @param world
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isTimeBetween(World world, long min, long max) {
		long time = world.getWorldTime();
		return min < time && time < max;
	}
}