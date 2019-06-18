package com.favouritedragon.dragonenchantments.common.enchantments.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSDoubleJump;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class CloudWalker extends Enchantment {

	private static HashMap<String, Integer> timesJumped = new HashMap<>();

	public CloudWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "cloud_walker");
		setRegistryName("cloud_walker");
	}

	@SubscribeEvent
	public static void clearDoubleJump(LivingFallEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, entity);
			if (level > 0) {
				setTimesJumped(entity.getUniqueID().toString(), 0);
				event.setCanceled(level >= 3);
				for (int i = 0; i < event.getDistance(); i++) {
					event.setDamageMultiplier((float) Math.pow(0.3 - level / 100F, i == 0 ? 1 : i));
				}
			}
		}
	}

	@SubscribeEvent
	public static void jumpAndFallEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, entity);
			if (level > 0) {
				if (!entity.isPotionActive(MobEffects.JUMP_BOOST)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 1000000000, level - 2, false, false));
				}
				if (entity.motionY < 0 && !entity.onGround) {
					entity.motionY *= 0.7f - level / 100F;
				}
				if (!timesJumped.containsKey(entity.getUniqueID().toString())) {
					setTimesJumped(entity.getUniqueID().toString(), 0);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyPressEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, player);
				if (level > 0) {
					if (getTimesJumped(player.getUniqueID().toString()) < level) {
						if (!player.onGround) {
							if (player.world instanceof WorldClient) {
								WorldClient world = (WorldClient) player.world;
								for (int angle = 0; angle < 360; angle += 3) {
									double radians = Math.toRadians(angle);
									double x = Math.cos(radians);
									double z = Math.sin(radians);
									world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, true, x + player.posX, player.getEntityBoundingBox().minY,
											z + player.posZ, 0, 0, 0);
								}
							}
							player.getEntityWorld().sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSDoubleJump(player.getEntityId())));
							setTimesJumped(player.getUniqueID().toString(), getTimesJumped(player.getUniqueID().toString()) + 1);
						}
					}
				}
			}
		}

	}

	// Called on the server
	public static void doDoubleJump(EntityPlayer entity) {
		int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, entity);
		if (entity.motionY >= 0) {
			entity.addVelocity(0, 0.46F * (1 + level / 10F), 0);
		} else {
			entity.motionY = 0.46f * (1 + level / 10F);
		}
		net.minecraftforge.common.ForgeHooks.onLivingJump(entity);
	}

	private static void setTimesJumped(String UUID, int jumped) {
		if (timesJumped.containsKey(UUID)) {
			timesJumped.replace(UUID, jumped);
		} else {
			timesJumped.put(UUID, jumped);
		}
	}

	private static int getTimesJumped(String UUID) {
		return timesJumped.getOrDefault(UUID, 0);
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 100 * enchantmentLevel;
	}
}
