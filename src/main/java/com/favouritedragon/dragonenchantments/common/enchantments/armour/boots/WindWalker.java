package com.favouritedragon.dragonenchantments.common.enchantments.armour.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSWindWalk;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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

	public static void setLastPressedKey(String UUID, int keyCode) {
		if (!lastPressedKey.containsKey(UUID)) {
			lastPressedKey.put(UUID, keyCode);
		}
		else {
			doubleTapTicks.replace(UUID, keyCode);
		}
	}

	public static int getLastPressedKey(String UUID) {
		return lastPressedKey.getOrDefault(UUID, Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCodeDefault());
	}

	public static void setDoubleTapTicks(String UUID, int ticks) {
		if (!doubleTapTicks.containsKey(UUID)) {
			doubleTapTicks.put(UUID, ticks);
		}
		else {
			doubleTapTicks.replace(UUID, ticks);
		}
	}

	public static int getDoubleTapTicks(String UUID) {
		return doubleTapTicks.getOrDefault(UUID, 0);
	}

	public static void setCooldown(String UUID, int ticks) {
		if (!coolDown.containsKey(UUID)) {
			coolDown.put(UUID, ticks);
		}
		else {
			coolDown.replace(UUID, ticks);
		}
	}

	public static int getCooldown(String UUID) {
		return coolDown.get(UUID);
	}


	public WindWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "wind_walker");
		setRegistryName("wind_walker");
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
		if (EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player) > 0) {
			if (left.isPressed()) {
				if (getLastPressedKey(UUID) == left.getKeyCode()) {
					if (getDoubleTapTicks(UUID) <= 6) {
						if (getCooldown(UUID) <= 0) {
							//Send Packet
							origin = origin.add(DragonUtils.getVectorForRotation(player.rotationPitch, player.rotationYaw - 90));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
							setDoubleTapTicks(UUID, 0);
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
							origin = origin.add(player.getLookVec());
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
							setDoubleTapTicks(UUID, 0);
						}
					}
				} else {
					setLastPressedKey(UUID, forward.getKeyCode());
					setDoubleTapTicks(UUID, 0);
				}
			}
			if (right.isPressed()) {
				if (getLastPressedKey(UUID) == right.getKeyCode()) {
					if (getDoubleTapTicks(UUID) <= 6) {
						if (getCooldown(UUID) <= 0) {
							//Send Packet
							origin = origin.add(DragonUtils.getVectorForRotation(player.rotationPitch, player.rotationYaw + 90));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
							setDoubleTapTicks(UUID, 0);
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
							origin = origin.add(DragonUtils.getVectorForRotation(player.rotationPitch, player.rotationYaw + 180));
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSWindWalk(player.getUniqueID().toString(),
									origin.x, origin.y, origin.z, EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.windWalker, player))));
							setDoubleTapTicks(UUID, 0);
						}
					}
				} else {
					setLastPressedKey(UUID, back.getKeyCode());
					setDoubleTapTicks(UUID, 0);
				}
			}
		}
	}

	public static void windWalk(EntityLivingBase entity, double x, double y, double z, int level) {
			DragonUtils.teleportTo(entity, x, y, z, SoundEvents.ENTITY_VEX_CHARGE);
	}



	@Override
	public int getMaxLevel() {
		return 3;
	}
}
