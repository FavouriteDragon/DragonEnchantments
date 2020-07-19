package com.favouritedragon.dragon_enchants.common.enchantments;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.favouritedragon.dragon_enchants.common.enchantments.all.SolarPowered;
import com.favouritedragon.dragon_enchants.common.enchantments.armour.StormProtection;
import com.favouritedragon.dragon_enchants.common.enchantments.armour.boots.*;
import com.favouritedragon.dragon_enchants.common.enchantments.armour.chest.VoidHunger;
import com.favouritedragon.dragon_enchants.common.enchantments.armour.leggings.Dolphin;
import com.favouritedragon.dragon_enchants.common.enchantments.weapon.*;
import com.favouritedragon.dragon_enchants.common.enchantments.weapon.bow.Homing;
import com.favouritedragon.dragon_enchants.common.enchantments.weapon.sword.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ModEnchantments {
    public static final EnchantmentType WEAPONS = EnchantmentType.create("weapons", (item) ->
            (item instanceof SwordItem || item instanceof BowItem || item instanceof AxeItem || item instanceof TridentItem));


    public static final Enchantment berserk = new Berserk();
    public static final Enchantment cloudWalker = new CloudWalker();
    public static final Enchantment dolphin = new Dolphin();
    public static final Enchantment doubleEdge = new DoubleEdged();
    public static final Enchantment dragonSlayer = new DragonSlayer();
    public static final Enchantment enderference = new Enderference();
    public static final Enchantment endWalker = new EndWalker();
    public static final Enchantment gigaSlash = new Gigaslash();
    public static final Enchantment homing = new Homing();
    public static final Enchantment lifeSteal = new Lifesteal();
    public static final Enchantment oblivion = new Oblivion();
    public static final Enchantment thunderAspect = new ThunderAspect();
    public static final Enchantment solarPowered = new SolarPowered();
    public static final Enchantment soulDevour = new SoulDevour();
    public static final Enchantment stormProtection = new StormProtection();
    public static final Enchantment stormStrider = new StormStrider();
    public static final Enchantment venomous = new Venomous();
    public static final Enchantment voidHunger = new VoidHunger();
    public static final Enchantment voidWalker = new VoidWalker();
    public static final Enchantment windWalker = new WindWalker();

}
