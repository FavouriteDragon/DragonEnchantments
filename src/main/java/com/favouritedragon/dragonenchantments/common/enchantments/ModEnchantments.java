package com.favouritedragon.dragonenchantments.common.enchantments;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.sword.SoulDevour;
import com.favouritedragon.dragonenchantments.common.enchantments.sword.ThunderAspect;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.DragonSlayer;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Enderference;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Lifesteal;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ModEnchantments {
	public static final EnumEnchantmentType WEAPONS = EnumHelper.addEnchantmentType("weapons", (item) ->
			(item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemAxe));


	public static final Enchantment dragonSlayer = new DragonSlayer();
	public static final Enchantment lifeSteal = new Lifesteal();
	public static final Enchantment thunderAspect = new ThunderAspect();
	public static final Enchantment enderference = new Enderference();
	public static final Enchantment soulDevour = new SoulDevour();
}
