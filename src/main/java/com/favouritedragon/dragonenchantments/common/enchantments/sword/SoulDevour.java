package com.favouritedragon.dragonenchantments.common.enchantments.sword;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;

import akka.japi.Pair;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class SoulDevour extends Enchantment {

	public final static UUID MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");

	public SoulDevour() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		setRegistryName("soul_devour");
		setName(DragonEnchants.MODID + ":" + "soul_devour");
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		Entity source = event.getSource().getTrueSource();
		Entity target = event.getSource().getTrueSource();
		if (source == null || target == null)
			return;
		if (source instanceof EntityLivingBase) {
			EntityLivingBase trueEntity = (EntityLivingBase) source;
			if (DragonUtils.getHeldLevelForEnchantment(trueEntity, ModEnchantments.soulDevour) > 0) {
				ItemStack stack = DragonUtils
						.getHeldLevelForEnchantmentAndHeldItem(trueEntity, ModEnchantments.soulDevour).second();
				short number_killed = readNbt(stack);
				number_killed++;
				writeNbt(stack, number_killed, getAttackDamage(stack));
				trueEntity.heal(((EntityLivingBase) target).getHealth() / 2);
				System.out.println(readInitalDamage(stack) + " " + stack.getItem().getDamage(stack));
				if(number_killed < 76){
				writeModifier(stack,  readInitalDamage(stack) * ((100F + number_killed) / 100F));
				}
			}
		}
	}

	private static void writeNbt(ItemStack stack, short number_killed, double intial_damage) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		nbt.setShort("SoulDevourKills", number_killed);
		if(!nbt.hasKey("IntialDamage"))
		nbt.setDouble("IntialDamage", intial_damage);
	}

	private static void writeModifier(ItemStack stack, double value) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
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

	private static double getAttackDamage(ItemStack stack){
		double value = 0;
		for(AttributeModifier modifier : stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())){
			value = modifier.getAmount();
		}
		return value;
	}

	private static short readNbt(ItemStack stack) {
		NBTTagCompound nbt;
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		if (nbt.hasKey("SoulDevourKills")) {
			return nbt.getShort("SoulDevourKills");
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
