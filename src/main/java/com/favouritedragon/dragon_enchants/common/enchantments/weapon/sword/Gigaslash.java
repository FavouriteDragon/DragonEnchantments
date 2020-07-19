package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.network.PacketSGigaSlash;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Gigaslash extends Enchantment {

	public final static UUID MODIFIER_UUID = UUID.fromString("f64334b8-2f79-47e1-bbc8-e9c098077dff");

	public Gigaslash() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		setName(DragonEnchants.MODID + ":" + "gigaslash");
		setRegistryName("gigaslash");
	}

	/*@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onChargeEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemSword) {
				ItemStack stack = player.getHeldItemMainhand();
				int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.gigaSlash, stack);
				if (level > 0) {
					//Send packets
					int maxChargeTime = 40 + 5 * level;
					float damageMod = readChargeTime(stack) / (float) maxChargeTime * level;
					if (mc.gameSettings.keyBindLeft.isKeyDown() && mc.gameSettings.keyBindRight.isKeyDown() && mc.gameSettings.keyBindBack.isKeyDown()) {
						System.out.println(readChargeTime(stack));
						player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSGigaSlash(player.getUniqueID().toString(), damageMod, level, true)));
					} else {
						if (readChargeTime(stack) > 0) {
							//Send packets for attacking
							player.world.sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSGigaSlash(player.getUniqueID().toString(), damageMod,
									level, false)));
							player.playSound(SoundEvents.ENTITY_LIGHTNING_IMPACT, 1.5F + level / 10F, 0.8F + player.world.rand.nextFloat() / 5);
							player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 2.0F + level / 10F, 0.8F + player.world.rand.nextFloat() / 5);
							player.swingArm(EnumHand.MAIN_HAND);
							writeNbt(player.getHeldItemMainhand(), ((ItemSword) player.getHeldItemMainhand().getItem()).getAttackDamage(), 0);
						}
					}
				}
			}
		}
	}**/

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onUpdateTick(TickEvent.PlayerTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemSword) {
				ItemStack stack = player.getHeldItemMainhand();
				int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.gigaSlash, stack);
				if (level > 0) {
					//Send packets
					int maxChargeTime = 40 + 5 * level;
					float damageMod = readChargeTime(stack) / (float) maxChargeTime * level;
					if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
						player.getEntityWorld().sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSGigaSlash(player.getUniqueID().toString(), damageMod, level, true)));
						player.motionX *= 0.675;
						player.motionZ *= 0.675;
					} else {
						if (readChargeTime(stack) > 20) {
							//Send packets for attacking
							player.getEntityWorld().sendPacketToServer(DragonEnchants.NETWORK.getPacketFrom(new PacketSGigaSlash(player.getUniqueID().toString(), damageMod,
									level, false)));
							player.playSound(SoundEvents.ENTITY_LIGHTNING_IMPACT, 1.5F + level / 10F, 0.8F + player.world.rand.nextFloat() / 5);
							player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 2.0F + level / 10F, 0.8F + player.world.rand.nextFloat() / 5);
							player.swingArm(EnumHand.MAIN_HAND);
							writeNbt(player.getHeldItemMainhand(), ((ItemSword) player.getHeldItemMainhand().getItem()).getAttackDamage(), 0);
						}
					}
				}
			}
		}
	}

	public static void setNbt(ItemStack stack, float damageMod) {
		writeNbt(stack, readInitialDamage(stack) + damageMod, readChargeTime(stack) + 1);
	}

	public static void attackEnemies(EntityPlayer player, int level, float damageMod) {
		if (level > 0) {
			((EntityPlayerMP) player).getServerWorld().addScheduledTask(() -> {
				int maxChargeTime = (int) ((readChargeTime(player.getHeldItemMainhand()) * level) / damageMod);
				float radius = (float) (player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() / 2 + Math.min(damageMod, 3) + 1);
				AxisAlignedBB box = player.getEntityBoundingBox().grow(radius);
				List<EntityLivingBase> targets = player.world.getEntitiesWithinAABB(EntityLivingBase.class, box);
				World world = player.world;
				if (!targets.isEmpty()) {
					for (EntityLivingBase living : targets) {
						if (living != player && player.getRidingEntity() != living) {
							if (living.getTeam() != null && living.getTeam() != player.getTeam() || living.getTeam() == null) {
								Vec3d vel = living.getPositionVector().subtract(player.getPositionVector()).scale(0.25);
								vel = vel.scale(1 / DragonUtils.getMagnitude(vel));
								vel = vel.scale(1 + 0.5F * level);
								living.attackEntityFrom(DamageSource.LIGHTNING_BOLT, (float) getAttackDamage(player.getHeldItemMainhand()));
								living.addVelocity(vel.x, vel.y + 0.15, vel.z);
								living.setFire(level + 1);
								//living.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) (getAttackDamage(player.getHeldItemMainhand()) - damageMod));
							}
						}
					}
				}
				world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, true));
				if (world instanceof WorldServer) {
					//Copied from the player class. It converts the angle to radians
					double d0 = -MathHelper.sin((player.rotationYaw) * 0.017453292F);
					double d1 = MathHelper.cos((player.rotationYaw) * 0.017453292F);
					//((WorldServer) world).spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, player.posX + d0, player.posY + (double) player.height * 0.5D,
					//		player.posZ + d1, 0, d0, 0.0D, d1, 0);
					for (int angle = 0; angle < 360; angle += 10) {
						d0 = -MathHelper.sin((player.rotationYaw + angle) * 0.017453292F) * radius / 2;
						d1 = MathHelper.cos((player.rotationYaw + angle) * 0.017453292F) * radius / 2;
						((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, player.posX + d0, player.posY + (double) player.height * 0.5D,
								player.posZ + d1, level * 2 + 5, d0, 0.0D, d1, 0.2 + 0.1 * level * readChargeTime(player.getHeldItemMainhand()) / 50);
						((WorldServer) world).spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, player.posX + d0, player.posY + (double) player.height * 0.5D,
								player.posZ + d1, level * 2 + 10, d0, 0.0D, d1, 0.2 + 0.1 * level * readChargeTime(player.getHeldItemMainhand()) / 50);

					}
				}
				player.getFoodStats().setFoodLevel((int) (player.getFoodStats().getFoodLevel() - (4 + 0.5 * level)));
			});
		}
		writeNbt(player.getHeldItemMainhand(), ((ItemSword) player.getHeldItemMainhand().getItem()).getAttackDamage(), 0);

	}

	private static void writeNbt(ItemStack stack, double initialDamage, int chargeTime) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		assert nbt != null;
		nbt.setInteger("GigaSlashChargeTime", chargeTime);
		if (!nbt.hasKey("InitialDamage"))
			nbt.setDouble("InitialDamage", initialDamage);
	}

	private static void writeModifier(ItemStack stack, double value) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		assert nbt != null;
		NBTTagCompound nestedNbt = new NBTTagCompound();
		AttributeModifier modifier = new AttributeModifier(MODIFIER_UUID, "Gigaslash Damage Boost", value, 0);
		nestedNbt.setString("AttributeName", SharedMonsterAttributes.ATTACK_DAMAGE.getName());
		nestedNbt.setString("Name", modifier.getName());
		nestedNbt.setDouble("Amount", modifier.getAmount());
		nestedNbt.setInteger("Operation", modifier.getOperation());
		nestedNbt.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
		nestedNbt.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
		NBTTagList list = new NBTTagList();
		list.appendTag(nestedNbt);
		nbt.setTag("AttributeModifiers", list);
	}

	private static double getAttackDamage(ItemStack stack) {
		double value = 0;
		for (AttributeModifier modifier : stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
			value = modifier.getAmount();
		}
		return value;
	}

	private static int readChargeTime(ItemStack stack) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		assert nbt != null;
		if (nbt.hasKey("GigaSlashChargeTime")) {
			return nbt.getInteger("GigaSlashChargeTime");
		} else {
			return 0;
		}
	}

	private static double readInitialDamage(ItemStack stack) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		if (nbt.hasKey("IntialDamage")) {
			return nbt.getDouble("IntialDamage");
		} else {
			return 0;
		}
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel) + 400;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}
}
