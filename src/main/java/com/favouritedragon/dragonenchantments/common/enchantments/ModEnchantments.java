package com.favouritedragon.dragonenchantments.common.enchantments;

import com.favouritedragon.dragonenchantments.common.enchantments.sword.DragonSlayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ModEnchantments {
	public static final EnumEnchantmentType WEAPONS = EnumHelper.addEnchantmentType("weapons", (item)->
			(item instanceof ItemSword || item instanceof ItemBow));

	public static final Enchantment dragonSlayer = new DragonSlayer();
}
