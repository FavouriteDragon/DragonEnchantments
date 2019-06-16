package com.favouritedragon.dragonenchantments.common.enchantments.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ThunderAspect extends Enchantment {
	public ThunderAspect() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		setRegistryName("thunder_aspect");
		setName(DragonEnchants.MODID + ":" + "thunder_aspect");
	}

	@SubscribeEvent
	public static void onSwingEvent(LivingAttackEvent event) {
		Entity attacker = event.getSource().getTrueSource();
		EntityLivingBase hurt = event.getEntityLiving();
		if (attacker instanceof EntityLivingBase) {
			ItemStack stack = ((EntityLivingBase) attacker).getHeldItemMainhand();
			if (stack.isItemEnchanted()) {
				if (attacker instanceof EntityPlayer && !((EntityPlayer) attacker).getCooldownTracker().hasCooldown(stack.getItem())) {
					int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
					if (level > 0 && hurt != null) {
						hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT, event.getAmount() / 10 * level * 2);
						Vec3d lookVec = attacker.getLookVec();
						hurt.motionX += lookVec.x * (1 + 0.2 * level);
						hurt.motionY += lookVec.y * (1 + 0.2 * level);
						hurt.motionZ += lookVec.z * (1 + 0.2 * level);
						if (attacker.world.isRemote) {
							attacker.world.playSound(hurt.posX, hurt.posY, hurt.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.PLAYERS,
									1.0F + attacker.world.rand.nextFloat(), 1.0F + attacker.world.rand.nextFloat(), true);
						}
					}
				}
				else {
					int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
					if (level > 0 && hurt != null) {
						hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT,event.getAmount() / 10 * level);
						Vec3d lookVec = attacker.getLookVec();
						hurt.motionX += lookVec.x * (1 + 0.15 * level);
						hurt.motionY += lookVec.y * (1 + 0.15 * level);
						hurt.motionZ += lookVec.z * (1 + 0.15 * level);
					}
				}

			}
		}
	}


	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + enchantmentLevel * 50;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + 60;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
}
