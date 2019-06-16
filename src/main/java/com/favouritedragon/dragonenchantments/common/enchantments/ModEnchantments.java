package com.favouritedragon.dragonenchantments.common.enchantments;

import com.favouritedragon.dragonenchantments.common.enchantments.weapon.DragonSlayer;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Lifesteal;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ModEnchantments {
	public static final EnumEnchantmentType WEAPONS = EnumHelper.addEnchantmentType("weapons", (item)->
			(item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemAxe));

	public static final Enchantment dragonSlayer = new DragonSlayer();
	public static final Enchantment lifeSteal = new Lifesteal();
}
