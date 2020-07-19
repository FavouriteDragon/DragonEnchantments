package com.favouritedragon.dragon_enchants.common.util;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Random;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DragonUtils {
    private static final DataParameter<Boolean> POWERED;

    static {
        POWERED = ObfuscationReflectionHelper.getPrivateValue(CreeperEntity.class, null, "POWERED");
    }

    public static void chargeCreeper(CreeperEntity creeper) {
        creeper.getDataManager().set(POWERED, true);
    }

    @SubscribeEvent
    public static void onLightningHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof CreeperEntity && event.getSource() == DamageSource.LIGHTNING_BOLT) {
            chargeCreeper((CreeperEntity) event.getEntityLiving());
        }
    }

    public static int getHeldLevelForEnchantment(LivingEntity entity, Enchantment enchantment) {
        ItemStack stack = null;
        ItemStack mainStack = entity.getHeldItemMainhand();
        ItemStack offStack = entity.getHeldItemOffhand();
        if (mainStack.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0) {
            stack = mainStack;
        } else if (offStack.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0) {
            stack = offStack;
        }
        return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
    }

    public static Pair<Integer, ItemStack> getHeldLevelForEnchantmentAndHeldItem(LivingEntity entity, Enchantment enchantment) {
        ItemStack stack = null;
        ItemStack mainStack = entity.getHeldItemMainhand();
        ItemStack offStack = entity.getHeldItemOffhand();
        if (mainStack.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0) {
            stack = mainStack;
        } else if (offStack.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0) {
            stack = offStack;
        }
        return new Pair<>(stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0, stack);
    }

    public static int getHeldLevelForEnchantment(LivingEntity entity, Enchantment enchantment, LivingHurtEvent event) {
        ItemStack stack = null;
        ItemStack mainStack = entity.getHeldItemMainhand();
        ItemStack offStack = entity.getHeldItemOffhand();
        boolean mainLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0;
        boolean offLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0;
        if (event.getSource().getImmediateSource() instanceof ArrowEntity) {
            if (mainStack.isEnchanted() && mainStack.getItem() instanceof BowItem && mainLevel) {
                stack = mainStack;
            } else if (offStack.isEnchanted() && offStack.getItem() instanceof BowItem && offLevel) {
                stack = offStack;
            }
        } else {
            if (mainStack.isEnchanted() && mainLevel) {
                stack = mainStack;
            } else if (offStack.isEnchanted() && offLevel) {
                stack = offStack;
            }
        }
        return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
    }

    public static int getHeldLevelForEnchantment(LivingEntity entity, Enchantment enchantment, LivingAttackEvent event) {
        ItemStack stack = null;
        ItemStack mainStack = entity.getHeldItemMainhand();
        ItemStack offStack = entity.getHeldItemOffhand();
        boolean mainLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, mainStack) > 0;
        boolean offLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, offStack) > 0;
        if (event.getSource().getImmediateSource() instanceof ArrowEntity) {
            if (mainStack.isEnchanted() && mainStack.getItem() instanceof BowItem && mainLevel) {
                stack = mainStack;
            } else if (offStack.isEnchanted() && offStack.getItem() instanceof BowItem && offLevel) {
                stack = offStack;
            }
        } else {
            if (mainStack.isEnchanted() && mainLevel) {
                stack = mainStack;
            } else if (offStack.isEnchanted() && offLevel) {
                stack = offStack;
            }
        }
        return stack != null ? EnchantmentHelper.getEnchantmentLevel(enchantment, stack) : 0;
    }


    public static boolean teleportRandomly(LivingEntity entity, double radiusMult) {
        double d0 = entity.getPosX() + (entity.world.rand.nextDouble() - 0.5D) * radiusMult;
        double d1 = entity.getPosY() + (entity.world.rand.nextInt((int) radiusMult) - radiusMult / 2);
        double d2 = entity.getPosZ() + (entity.world.rand.nextDouble() - 0.5D) * radiusMult;
        return teleportTo(entity, d0, d1, d2, SoundEvents.ENTITY_ENDERMAN_TELEPORT);
    }

    public static boolean teleportTo(LivingEntity entity, double x, double y, double z, SoundEvent sound) {
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
    public static boolean attemptTeleport(LivingEntity entity, double x, double y, double z) {
        double d0 = entity.getPosX();
        double d1 = entity.getPosY();
        double d2 = entity.getPosZ();
        entity.setPosition(x, y, z);
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entity);
        World world = entity.world;

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                BlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    entity.setPosition(entity.getPosX(), entity.getPosY() - 1, entity.getPosZ());
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                entity.setPositionAndUpdate(entity.getPosX(), entity.getPosY(), entity.getPosZ());

                if (world.getCollisionShapes(entity, entity.getBoundingBox()).count() == 0 && !world.containsAnyLiquid(entity.getBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            if (entity instanceof CreatureEntity) {
                ((CreatureEntity) entity).getNavigator().clearPath();
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
		/*if (target instanceof PlayerEntity) {
			((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
			((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityTeleport(target));

		}**/
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
        long time = world.getGameTime();
        return min <= time && time <= max;
    }

    //Pretty performance heavy
    public static double getMagnitude(Vec3d vector) {
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
    }

    //Vec3d from pitch and yaw of an entity
    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }
}