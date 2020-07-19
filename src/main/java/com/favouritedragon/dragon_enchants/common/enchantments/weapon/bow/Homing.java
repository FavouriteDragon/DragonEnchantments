package com.favouritedragon.dragon_enchants.common.enchantments.weapon.bow;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import com.favouritedragon.dragon_enchants.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Homing extends Enchantment {

	public Homing() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setName(DragonEnchants.MODID + ":" + "homing");
		setRegistryName("homing");
	}

	@SubscribeEvent
	public static void onHomingArrows(EntityEvent event) {
		if (event.getEntity() instanceof EntityArrow) {
			EntityArrow entity = (EntityArrow) event.getEntity();
			if (entity.shootingEntity instanceof EntityLivingBase && entity.ticksExisted >= 1) {
				int level = DragonUtils.getHeldLevelForEnchantment((EntityLivingBase) entity.shootingEntity, ModEnchantments.homing);
				if (level > 0) {
					Vec3d initialVelocity = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
					double scale = DragonUtils.getMagnitude(initialVelocity);
					double dist = 100;
					EntityLivingBase target = null;
					AxisAlignedBB box = entity.getEntityBoundingBox();
					box = box.grow(7 * level);
					List<EntityLivingBase> targets = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, box);
					if (!targets.isEmpty()) {
						for (EntityLivingBase e : targets) {
							if (e instanceof EntityMob && e != entity.shootingEntity &&
									((EntityLivingBase) entity.shootingEntity).canEntityBeSeen(e)) {
								if (entity.getDistance(e) < dist) {
									dist = entity.getDistance(e);
									target = e;
								}
							}
						}
					}
					if (target == null) return;
					double motionX = (target.posX - entity.posX) * scale;
					double motionY = (target.posY + target.getEyeHeight() - entity.posY) * scale;
					double motionZ = (target.posZ - entity.posZ) * scale;
					entity.shoot(motionX, motionY, motionZ, (float) scale, 0.F);
					entity.velocityChanged = true;
				}
			}
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
