package com.favouritedragon.dragonenchantments.common.enchantments.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.HashSet;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class CloudWalker extends Enchantment {

	private static HashMap<String, Integer> timesJumped;

	public CloudWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "cloud_walker");
		setRegistryName("cloud_walker");
	}

	@SubscribeEvent
	public static void jumpAndFallEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, entity);
			if (level > 0) {
				if (entity.motionY < 0 && !entity.onGround) {
					entity.motionY += 0.03 + level / 100F;
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
				if (getTimesJumped(player.getUniqueID().toString()) < 2) {
					if (!player.onGround) {

					}
				}
			}
		}

	}


	@SubscribeEvent
	public void clearDoubleJump(LivingFallEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null) {
			int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.cloudWalker, entity);
			if (level > 0) {
				setTimesJumped(entity.getUniqueID().toString(), 0);
			}
		}
	}
	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 100 * enchantmentLevel;
	}

	public static void setTimesJumped(String UUID, int jumped) {
		timesJumped.replace(UUID, jumped);
	}

	public static void addTimesJumped(String UUID, int jumped) {
		timesJumped.put(UUID, jumped);
	}

	public static int getTimesJumped(String UUID) {
		return timesJumped.get(UUID);
	}
}
