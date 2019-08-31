package com.favouritedragon.dragonenchantments.common.enchantments.armour.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSWindWalk;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class WindWalker extends Enchantment {

	public static HashMap<String, Integer> lastPressedKey = new HashMap<>();
	public static HashMap<String, Integer> doubleTapTicks = new HashMap<>();
	public static HashMap<String, Integer> coolDown = new HashMap<>();

	public WindWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "wind_walker");
		setRegistryName("wind_walker");
	}

	public static void setLastPressedKey(String UUID, int keyCode) {
		if (!lastPressedKey.containsKey(UUID)) {
			lastPressedKey.put(UUID, keyCode);
		} else {
			doubleTapTicks.replace(UUID, keyCode);
		}
	}

	public static int getLastPressedKey(String UUID) {
		return lastPressedKey.getOrDefault(UUID, Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCodeDefault());
	}

	public static void setDoubleTapTicks(String UUID, int ticks) {
		if (!doubleTapTicks.containsKey(UUID)) {
			doubleTapTicks.put(UUID, ticks);
		} else {
			doubleTapTicks.replace(UUID, ticks);
		}
	}

	public static int getDoubleTapTicks(String UUID) {
		return doubleTapTicks.getOrDefault(UUID, 0);
	}

	public static void setCooldown(String UUID, int ticks) {
		if (!coolDown.containsKey(UUID)) {
			coolDown.put(UUID, ticks);
		} else {
			coolDown.replace(UUID, ticks);
		}
	}

	public static int getCooldown(String UUID) {
		return coolDown.getOrDefault(UUID, 0);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onDoubleTapEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		String UUID = mc.player.getUniqueID().toString();
		KeyBinding left = mc.gameSettings.keyBindLeft;
		KeyBinding forward = mc.gameSettings.keyBindForward;
		KeyBinding right = mc.gameSettings.keyBindRight;
		KeyBinding back = mc.gameSettings.keyBindBack;
		Vec3d origin = player.getPositionVector();
		int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player);
		if (level > 0) {
			if (left.isPressed()) {
				if (getLastPressedKey(UUID) == left.getKeyCode()) {
					if (getDoubleTapTicks(UUID) <= 6) {
						if (getCooldown(UUID) <= 0) {
							//Send Packet
							origin = origin.add(DragonUtils.getVectorForRotation(player.rotationPitch, player.rotationYaw - 90).scale(level * 2.5));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
						}
					}
				} else {
					setLastPressedKey(UUID, left.getKeyCode());
					setDoubleTapTicks(UUID, 0);
				}
			}
			if (forward.isPressed()) {
				if (getLastPressedKey(UUID) == forward.getKeyCode()) {
					if (getDoubleTapTicks(UUID) <= 6) {
						if (getCooldown(UUID) <= 0) {
							//Send Packet
							origin = origin.add(player.getLookVec().scale(level * 2.5));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
							setDoubleTapTicks(UUID, 0);
						}
					}
				} else {
					setLastPressedKey(UUID, forward.getKeyCode());
				}
			}
			if (right.isPressed()) {
				if (getLastPressedKey(UUID) == right.getKeyCode()) {
					if (getDoubleTapTicks(UUID) <= 6) {
						if (getCooldown(UUID) <= 0) {
							//Send Packet
							origin = origin.add(DragonUtils.getVectorForRotation(player.rotationPitch, player.rotationYaw + 90).scale(level * 2.5));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
						}
					}
				} else {
					setLastPressedKey(UUID, right.getKeyCode());
					setDoubleTapTicks(UUID, 0);
				}
			}
			if (back.isPressed()) {
				if (getLastPressedKey(UUID) == back.getKeyCode()) {
					if (getDoubleTapTicks(UUID) <= 6) {
						if (getCooldown(UUID) <= 0) {
							//Send Packet
							origin = origin.add(DragonUtils.getVectorForRotation(player.rotationPitch, player.rotationYaw + 180).scale(level * 2.5));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, level)));
						}
					}
				} else {
					setLastPressedKey(UUID, back.getKeyCode());
					setDoubleTapTicks(UUID, 0);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onTickEvent(LivingEvent.LivingUpdateEvent event) {
		if (EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, event.getEntityLiving()) > 0) {
			EntityLivingBase entity = event.getEntityLiving();
			String UUID = entity.getUniqueID().toString();
			if (getCooldown(UUID) > 0)
				setCooldown(UUID, getCooldown(UUID) - 1);
			setDoubleTapTicks(UUID, getDoubleTapTicks(UUID) + 1);
		}
	}

	public static void windWalk(EntityPlayer entity, double x, double y, double z, int level) {
		setDoubleTapTicks(entity.getUniqueID().toString(), 0);
		int foodlevel = entity.getFoodStats().getFoodLevel();
		double distance = entity.getDistance(x, y, z);
		if (distance <= level * 2.5) {
			if (foodlevel >= distance / 2) {
				if (!entity.isSneaking() || entity.isCreative()) {
					((EntityPlayerMP) entity).getServerWorld().addScheduledTask(() -> {
						if (DragonUtils.teleportTo(entity, x, y, z, SoundEvents.ENTITY_VEX_CHARGE)) {
							entity.getFoodStats().setFoodLevel(entity.getFoodStats().getFoodLevel() - (int) (distance / 2));
							setCooldown(entity.getUniqueID().toString(), 100 - level * 10);
						}
					});
				}
			}
		}
	}


	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != ModEnchantments.stormStrider && ench != ModEnchantments.voidWalker && ench != ModEnchantments.endWalker;
	}
}
