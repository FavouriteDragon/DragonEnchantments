package com.favouritedragon.dragonenchantments.common.enchantments.armour.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSStormStride;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class StormStrider extends Enchantment {

	public StormStrider() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLightningEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (mc.gameSettings.keyBindSneak.isPressed()) {
				World world = player.world;
				if (world.isThundering()) {
					int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.stormStrider, player);
					if (level > 0) {
						RayTraceResult res = player.rayTrace(12 * level, 1);
						if (res != null && res.typeOfHit == RayTraceResult.Type.BLOCK && res.hitVec != null) {
							world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new
									PacketSStormStride(player.getCachedUniqueIdString(), res.hitVec.x, res.hitVec.y, res.hitVec.z, level)));
						}
					}
				}
			}
		}
	}

	public static void StormStride(EntityLivingBase entity, double x, double y, double z, int level) {
		if (lightningTeleportTo(entity, x, y, z)) {
			AxisAlignedBB box = new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY + entity.height, entity.posZ);
			box.grow(3 * level, 0, level);
			List<Entity> targets = entity.world.getEntitiesWithinAABB(Entity.class, box);
			if (!targets.isEmpty()) {
				for (Entity hit : targets) {
					if (hit != entity) {
						Vec3d velocity = new Vec3d(hit.posX - entity.posX, hit.posY - entity.posY, hit.posZ - entity.posZ);
						velocity.scale(level);
						float amount = level * 3;
						hit.attackEntityFrom(DamageSource.LIGHTNING_BOLT, amount);
						hit.motionX += velocity.x;
						hit.motionY += velocity.y;
						hit.motionZ += velocity.z;
						DragonUtils.applyPlayerKnockback(hit);
					}
				}
			}
		}
	}
	public static boolean lightningTeleportTo(EntityLivingBase entity, double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean teleport = entity.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (teleport) {
			DragonUtils.applyPlayerKnockback(entity);
			entity.world.addWeatherEffect(new EntityLightningBolt(entity.world, entity.posX, entity.posY, entity.posZ, true));
			entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, entity.getSoundCategory(), 1.0F, 1.0F);
			entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, entity.getSoundCategory(), 1.0F, 1.0F);
		}

		return teleport;
	}


	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != ModEnchantments.voidWalker;
	}
}
