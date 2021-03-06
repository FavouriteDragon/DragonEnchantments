package com.favouritedragon.dragonenchantments.common.enchantments.armour.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSVoidWalk;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class VoidWalker extends Enchantment {

	public VoidWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
		setName(DragonEnchants.MODID + ":" + "void_walker");
		setRegistryName("void_walker");
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyPressEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.voidWalker, player);
				// Have to use the sun brightness, because isDaytime and skylight stuff are
				// completely screwed.
				if (level > 0 && (player.getEntityWorld().getLight(player.getPosition()) < 7
						|| player.world.getSunBrightness(1.0F) < 0.4F)) {
					RayTraceResult result = player.rayTrace(8 * level, 1);
					if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
						BlockPos position = result.getBlockPos().add(0, 1, 0);
						player.getEntityWorld().sendPacketToServer(DragonEnchants.NETWORK
								.getPacketFrom(new PacketSVoidWalk(player.getCachedUniqueIdString(), position)));
					}
				}
			}
		}

	}

	public static void onVoidWalk(EntityPlayer entity, BlockPos position) {
		double distance = entity.getDistance(position.getX(), position.getY(), position.getZ());
		if (entity.getEntityWorld().getLight(entity.getPosition()) < 7
				|| !DragonUtils.isTimeBetween(entity.world, 0, 11750)) {
			int foodlevel = entity.getFoodStats().getFoodLevel();
			foodlevel -= Double.valueOf(distance / 4).intValue();
			if (foodlevel >= Double.valueOf(distance / 4).intValue() || entity.isCreative()) {
				((EntityPlayerMP) entity).getServerWorld().addScheduledTask(() -> {
					// Make sure to run on main server thread
					if (DragonUtils.teleportTo(entity, position.getX(), position.getY(), position.getZ(), SoundEvents.ENTITY_ENDERMEN_SCREAM)) {
						entity.addExhaustion((float) (distance / 3F - EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.voidWalker, entity) + 2));
					}
				});

			}
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != ModEnchantments.endWalker;
	}
}
