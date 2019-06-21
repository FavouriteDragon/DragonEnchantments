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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class StormStrider extends Enchantment {

	private static final UUID MOVEMENT_MODIFIER_ID = UUID.fromString("356f2c26-63e7-449e-b1d3-7de2f8967aca");

	public StormStrider() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "storm_strider");
		setRegistryName("storm_strider");
	}

	@SubscribeEvent
	public static void onThunder(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase entity = event.getEntityLiving();
			int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.stormStrider, entity);
			if (level > 0) {
				if (entity.world.isThundering()) {
					if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(MOVEMENT_MODIFIER_ID) == null) {
						applyMovementModifier(entity, 1.5F + level / 5F);
					}
				}
				else if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(MOVEMENT_MODIFIER_ID) != null) {
					entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MOVEMENT_MODIFIER_ID);
				}

			}
			else if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(MOVEMENT_MODIFIER_ID) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MOVEMENT_MODIFIER_ID);
			}
		}
	}
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLightningEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				World world = player.world;
				if (world.isThundering()) {
					int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.stormStrider, player);
					if (level > 0) {
						RayTraceResult res = player.rayTrace(12 * level, 1);
						if (res != null && res.typeOfHit == RayTraceResult.Type.BLOCK && res.hitVec != null) {
							double y = res.hitVec.y;
							if (world.getBlockState(new BlockPos(res.hitVec.x, res.hitVec.y, res.hitVec.z)).getBlock() != Blocks.AIR) {
								y += 1;
							}
							world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new
									PacketSStormStride(player.getCachedUniqueIdString(), res.hitVec.x, y, res.hitVec.z, level)));

						}
					}
				}
			}
		}
	}

	public static void StormStride(EntityPlayer entity, double x, double y, double z, int level) {
		int foodlevel = entity.getFoodStats().getFoodLevel();
		double distance = entity.getDistance(x, y, z);
		if (distance / (level * 2) <= foodlevel || entity.isCreative()) {
			((EntityPlayerMP) entity).getServerWorld().addScheduledTask(() -> {
				// Make sure to run on main server thread
				if (lightningTeleportTo(entity, x, y, z)) {
					AxisAlignedBB box = new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY + entity.height, entity.posZ);
					box = box.grow(4 * level, 0, 4 * level);
					List<Entity> targets = entity.world.getEntitiesWithinAABB(Entity.class, box);
					if (!targets.isEmpty()) {
						for (Entity hit : targets) {
							if (hit != entity) {
								System.out.println(hit);
								Vec3d velocity = new Vec3d(hit.posX - entity.posX, hit.posY - entity.posY, hit.posZ - entity.posZ);
								velocity.scale(level);
								float amount = level * 3;
								hit.attackEntityFrom(DamageSource.LIGHTNING_BOLT, amount);
								hit.motionX += velocity.x * 0.3 * level;
								hit.motionY += velocity.y + 0.3 + level / 10F;
								hit.motionZ += velocity.z * 0.3 * level;
								DragonUtils.applyPlayerKnockback(hit);
							}
						}
					}
					if (entity.world instanceof WorldServer) {
						WorldServer world = (WorldServer) entity.world;
						for (int angle = 0; angle < 360; angle++) {
							double radians = Math.toRadians(angle);
							double x1 = Math.cos(radians);
							double z1 = Math.sin(radians);
							world.spawnParticle((EntityPlayerMP) entity, EnumParticleTypes.CRIT_MAGIC, true, x + x1, y, z + z1, 1 + DragonUtils.getRandomNumberInRange(0, 1),
									0, 0, 0, 2);
						}
					}
				}
			});
			entity.getFoodStats().setFoodLevel(entity.isCreative() ? foodlevel : foodlevel - (int) (distance / (level * 2)));
		}
	}

	private static boolean lightningTeleportTo(EntityLivingBase entity, double x, double y, double z) {
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


	private static void applyMovementModifier(EntityLivingBase entity, float multiplier) {

		IAttributeInstance moveSpeed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		moveSpeed.removeModifier(MOVEMENT_MODIFIER_ID);

		moveSpeed.applyModifier(new AttributeModifier(MOVEMENT_MODIFIER_ID, "Storm Strider Modifier", multiplier, 1));

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
