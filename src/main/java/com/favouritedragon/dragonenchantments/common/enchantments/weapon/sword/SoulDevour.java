package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class SoulDevour extends Enchantment {

	public final static UUID MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");

	//TODO: Add souls as a nbt value, based on enemies killed (every 10 health of enemies killed is a soul). Use it for cool stuff.
	public SoulDevour() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
		setRegistryName("soul_devour");
		setName(DragonEnchants.MODID + ":" + "soul_devour");
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		Entity source = event.getSource().getTrueSource();
		Entity target = event.getEntity();
		if (source == null || target == null)
			return;
		if (source instanceof EntityLivingBase && target instanceof EntityLivingBase) {
			EntityLivingBase trueEntity = (EntityLivingBase) source;
			if (DragonUtils.getHeldLevelForEnchantment(trueEntity, ModEnchantments.soulDevour) > 0) {
				ItemStack stack = DragonUtils
						.getHeldLevelForEnchantmentAndHeldItem(trueEntity, ModEnchantments.soulDevour).second();
				short numberKilled = readSoulsKilled(stack);
				numberKilled++;
				writeNbt(stack, numberKilled, getAttackDamage(stack), readTotalHealthConsumed(stack));
				trueEntity.heal(((EntityLivingBase) target).getMaxHealth() / 4);
				if (numberKilled < 76) {
					writeModifier(stack, readInitalDamage(stack) * ((100F + numberKilled) / 100F));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		Entity source = event.getSource().getTrueSource();
		Entity target = event.getEntity();
		if (source == null || target == null)
			return;
		if (source instanceof EntityLivingBase && target instanceof EntityLivingBase) {
			EntityLivingBase trueEntity = (EntityLivingBase) source;
			if (DragonUtils.getHeldLevelForEnchantment(trueEntity, ModEnchantments.soulDevour) > 0) {
				ItemStack stack = DragonUtils
						.getHeldLevelForEnchantmentAndHeldItem(trueEntity, ModEnchantments.soulDevour).second();
				double healthConsumed = readTotalHealthConsumed(stack);
				healthConsumed += event.getAmount();
				writeNbt(stack, readSoulsKilled(stack), getAttackDamage(stack), healthConsumed);
			}
		}
	}

	private static void writeNbt(ItemStack stack, short numberKilled, double initialDamage, double healthConsumed) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		assert nbt != null;
		nbt.setDouble("ConsumedHealth", healthConsumed);
		nbt.setShort("SoulDevourKills", numberKilled);
		if (!nbt.hasKey("IntialDamage"))
			nbt.setDouble("IntialDamage", initialDamage);
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
		AttributeModifier modifier = new AttributeModifier(MODIFIER_UUID, "Damage Boost", value, 0);
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

	private static short readSoulsKilled(ItemStack stack) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		assert nbt != null;
		if (nbt.hasKey("SoulDevourKills")) {
			return nbt.getShort("SoulDevourKills");
		} else {
			return 0;
		}
	}

	private static double readTotalHealthConsumed(ItemStack stack) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		assert nbt != null;
		if (nbt.hasKey("ConsumedHealth")) {
			return nbt.getDouble("ConsumedHealth");
		} else {
			return 0;
		}
	}

	private static double readInitalDamage(ItemStack stack) {
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
}
