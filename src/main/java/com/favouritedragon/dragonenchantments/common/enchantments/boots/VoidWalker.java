package com.favouritedragon.dragonenchantments.common.enchantments.boots;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSVoidWalk;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class VoidWalker extends Enchantment {

	public VoidWalker() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
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
				if (level > 0 && player.getEntityWorld().getLight(player.getPosition()) < 6) {
					player.getEntityWorld().sendPacketToServer(
							DragonEnchants.NETWORK.getPacketFrom(new PacketSVoidWalk(player.getEntityId())));
				}
			}
		}

	}

	public static void onVoidWalk(EntityPlayer entity) {
		int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.voidWalker, entity);
		RayTraceResult result = entity.rayTrace(8 * level,1);
		if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos position = result.getBlockPos().offset(result.sideHit);
			double distance = entity.getDistance(position.getX(), position.getY(), position.getZ());
			if (entity.getEntityWorld().getLight(entity.getPosition()) < 6) {
				int foodlevel = entity.getFoodStats().getFoodLevel();
				foodlevel -= Double.valueOf(distance / 3).intValue();
				if (foodlevel >= 0) {
					DragonUtils.teleportTo(entity, position.getX(), position.getY(), position.getZ());
					entity.getFoodStats().setFoodLevel(foodlevel);
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
		return super.canApplyTogether(ench) && ench != ModEnchantments.endWalker;
	}
}
