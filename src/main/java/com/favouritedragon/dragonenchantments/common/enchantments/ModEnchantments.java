package com.favouritedragon.dragonenchantments.common.enchantments;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.all.SolarPowered;
import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.CloudWalker;
import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.EndWalker;
import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.VoidWalker;
import com.favouritedragon.dragonenchantments.common.enchantments.armour.chest.VoidHunger;
import com.favouritedragon.dragonenchantments.common.enchantments.armour.leggings.Dolphin;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.bow.Homing;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword.SoulDevour;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword.ThunderAspect;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.DragonSlayer;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Enderference;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Lifesteal;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Venomous;
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


	public static final Enchantment cloudWalker = new CloudWalker();
	public static final Enchantment dolphin = new Dolphin();
	public static final Enchantment dragonSlayer = new DragonSlayer();
	public static final Enchantment enderference = new Enderference();
	public static final Enchantment endWalker = new EndWalker();
	public static final Enchantment homing = new Homing();
	public static final Enchantment lifeSteal = new Lifesteal();
	public static final Enchantment thunderAspect = new ThunderAspect();
	public static final Enchantment solarPowered = new SolarPowered();
	public static final Enchantment soulDevour = new SoulDevour();
	public static final Enchantment venomous = new Venomous();
	public static final Enchantment voidHuger = new VoidHunger();
	public static final Enchantment voidWalker = new VoidWalker();

}
