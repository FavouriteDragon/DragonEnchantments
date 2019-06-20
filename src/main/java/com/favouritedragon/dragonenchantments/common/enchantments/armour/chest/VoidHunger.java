package com.favouritedragon.dragonenchantments.common.enchantments.armour.chest;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class VoidHunger extends Enchantment {

	public VoidHunger() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[]{EntityEquipmentSlot.CHEST});
		setName(DragonEnchants.MODID + ":" + "void_hunger");
		setRegistryName("void_hunger");
	}

	//TODO: Nbt for permanent armour amount increase based on projectiles eaten

	@SubscribeEvent
	public static void onProjectileAttack(LivingAttackEvent event) {
			if (event.getEntityLiving() != null) {
				EntityLivingBase hurt = event.getEntityLiving();
				int level = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.voidHuger, hurt);
				if (level > 0) {
					if (event.getSource().getImmediateSource() != null) {
						if (event.getAmount() <= level * 6) {
							if (hurt instanceof EntityPlayer && (((EntityPlayer) hurt).getFoodStats().getFoodLevel() / (5F / level) >= event.getAmount() / 1.5F ||
									((EntityPlayer) hurt).isCreative())) {
								event.getSource().getImmediateSource().setDead();
								hurt.world.playSound(hurt.posX, hurt.posY, hurt.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS,
										1.0F + hurt.world.rand.nextFloat() / 10, 1.0F + hurt.world.rand.nextFloat() / 10, false);
								if (hurt.world instanceof WorldServer) {
									WorldServer world = (WorldServer) hurt.world;
									for (int i = 0; i < level + 5; i++) {
										double midHeight = (hurt.getEntityBoundingBox().maxY - hurt.getEntityBoundingBox().minY) * 3 / 5;
										world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, hurt.posX, hurt.getEntityBoundingBox().minY + midHeight, hurt.posZ,
												1 + DragonUtils.getRandomNumberInRange(0, 3), 0, 0, 0, 0.015);
									}
								}
								((EntityPlayer) hurt).getFoodStats().setFoodLevel(((EntityPlayer) hurt).getFoodStats().getFoodLevel() / (level * 2));
								event.setCanceled(true);
							} else if (!(hurt instanceof EntityPlayer)) {
								event.getSource().getImmediateSource().setDead();
								hurt.world.playSound(hurt.posX, hurt.posY, hurt.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS,
										1.0F + hurt.world.rand.nextFloat() / 10, 1.0F + hurt.world.rand.nextFloat() / 10, false);
								if (hurt.world instanceof WorldServer) {
									WorldServer world = (WorldServer) hurt.world;
									for (int i = 0; i < level + 5; i++) {
										double midHeight = (hurt.getEntityBoundingBox().maxY - hurt.getEntityBoundingBox().minY) * 3 / 5;
										world.spawnParticle(EnumParticleTypes.PORTAL, hurt.posX, hurt.getEntityBoundingBox().minY + midHeight, hurt.posZ,
												1 + DragonUtils.getRandomNumberInRange(0, 3), 0, 0, 0, 0.01);
									}
								}
								event.setCanceled(true);
							}
						}
					}
				}
			}
	}


	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + enchantmentLevel * 60;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 200;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}
