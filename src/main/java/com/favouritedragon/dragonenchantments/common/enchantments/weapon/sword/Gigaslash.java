package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class Gigaslash extends Enchantment {

	public final static UUID MODIFIER_UUID = UUID.fromString("f64334b8-2f79-47e1-bbc8-e9c098077dff");

	public Gigaslash() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onChargeEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemSword && player.getHeldItemMainhand() == player.getActiveItemStack()) {
				ItemStack stack = player.getHeldItemMainhand();
				int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.gigaSlash, stack);
				if (level > 0) {
					//Send packets
					int maxChargeTime = 40 + 5 * level;
					float damageMod = readChargeTime(stack) / (float) maxChargeTime * level;
					if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
						writeNbt(stack, readInitialDamage(stack) + damageMod, readChargeTime(stack) + 1);
					} else {
						if (readChargeTime(stack) > 0) {
							//Send packets for attacking
							writeNbt(stack, ((ItemSword) stack.getItem()).getAttackDamage(), 0);
						}
					}
				}
			}
		}
	}

	public static void setNbt(ItemStack stack, float damageMod) {

	}

	public static void attackEnemies() {

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
}
