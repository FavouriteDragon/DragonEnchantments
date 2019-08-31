package com.favouritedragon.dragonenchantments.common.util;

import akka.japi.Pair;
import com.favouritedragon.dragonenchantments.DragonEnchants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
		return teleportTo(entity, d0, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
	}

	public static boolean teleportTo(EntityLivingBase entity, double x, double y, double z, SoundEvent sound) {
		EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean teleport = attemptTeleport(entity, event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (teleport) {
			applyPlayerKnockback(entity);
			entity.world.playSound(null, entity.prevPosX, entity.prevPosY, entity.prevPosZ, sound, entity.getSoundCategory(), 1.0F, 1.0F);
			entity.playSound(sound, 1.0F, 1.0F);
		}

		return teleport;
	}

	//Same as the original method, but without particles
	public static boolean attemptTeleport(EntityLivingBase entity, double x, double y, double z) {
		double d0 = entity.posX;
		double d1 = entity.posY;
		double d2 = entity.posZ;
		entity.posX = x;
		entity.posY = y;
		entity.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(entity);
		World world = entity.world;

		if (world.isBlockLoaded(blockpos)) {
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0) {
				BlockPos blockpos1 = blockpos.down();
				IBlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getMaterial().blocksMovement()) {
					flag1 = true;
				} else {
					--entity.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1) {
				entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

				if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox())) {
					flag = true;
				}
			}
		}

		if (!flag) {
			entity.setPositionAndUpdate(d0, d1, d2);
			return false;
		} else {

			if (entity instanceof EntityCreature) {
				((EntityCreature) entity).getNavigator().clearPath();
			}

			return true;
		}
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
		return min <= time && time <= max;
	}

	//Pretty performance heavy
	public static double getMagnitude(Vec3d vector) {
		return Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
	}

	//Vec3d from pitch and yaw of an entity
	public static Vec3d getVectorForRotation(float pitch, float yaw) {
		float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
	}
}