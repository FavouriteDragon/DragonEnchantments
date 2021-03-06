package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.collection.mutable.MultiMap;

import java.util.Map;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class SoulDevour extends Enchantment {

    //Same as vanilla. We got dem hacks boys
    public final static UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    public static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

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
                int level = DragonUtils.getHeldLevelForEnchantment(trueEntity, ModEnchantments.soulDevour);
                short numberKilled = readSoulsKilled(stack);
                //Basically increases the amount of souls gotten per 20 health of the enemy. Ender dragon is worth 10 souls.
                if (((EntityLivingBase) target).getMaxHealth() >= 20)
                    numberKilled += (int) (((EntityLivingBase) target).getMaxHealth() / 20F);
                writeNbt(stack, numberKilled, readInitialDamage(stack, trueEntity), readTotalHealthConsumed(stack));
                trueEntity.heal(((EntityLivingBase) target).getMaxHealth() / 4);
                if (numberKilled < level * 25 + 1) {
                    float mod = ((100 + numberKilled) / 100F);
                    writeModifier(stack, trueEntity, mod);
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
                double totalHealthConsumed = readTotalHealthConsumed(stack);
                double healthConsumed = Math.min(event.getAmount(), ((EntityLivingBase) target).getHealth());

                totalHealthConsumed += healthConsumed;
                if (readSoulsKilled(stack) > 0) {
                    writeNbt(stack, (readSoulsKilled(stack)),
                              totalHealthConsumed);
                } else {
                    writeNbt(stack, (readSoulsKilled(stack)),
                            getAttackDamage(stack, trueEntity), totalHealthConsumed);
                }
                boolean updateInitialDamage = false;
                for (Enchantment enchantment : EnchantmentHelper.getEnchantments(stack).keySet()) {
                    if (enchantment instanceof EnchantmentDamage)
                        updateInitialDamage = true;
                }
                if (updateInitialDamage)
                    writeNbt(stack, readSoulsKilled(stack),
                            readInitialDamage(stack, trueEntity) + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED), totalHealthConsumed);
            }
        }
    }

    private static void writeNbt(ItemStack stack, short numberKilled, double healthConsumed) {
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }
        assert nbt != null;
        nbt.setDouble("ConsumedHealth", healthConsumed);
        nbt.setShort("SoulDevourKills", numberKilled);
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
        if (!nbt.hasKey("InitialDamage"))
            nbt.setDouble("InitialDamage", initialDamage);
    }

    private static void writeModifier(ItemStack stack, EntityLivingBase entity, double value) {
        AttributeModifier modifier = new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon Modifier", readInitialDamage(stack, entity) * value, 0);
        Multimap<String, AttributeModifier> attributeMods = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);

        stack.getTagCompound().setTag("AttributeModifiers", new NBTTagList());
        //Clears it. Get the modifiers before clearing!


        //Provides compatibility with other mods and enchantments
        for (Map.Entry<String, AttributeModifier> modifierEntry : attributeMods.entries()) {
            if (!modifierEntry.getKey().equals(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                AttributeModifier mod = new AttributeModifier(modifierEntry.getValue().getID(), modifierEntry.getValue().getName(),
                        modifierEntry.getValue().getAmount(), modifierEntry.getValue().getOperation());
                stack.addAttributeModifier(modifierEntry.getKey(), mod, EntityEquipmentSlot.MAINHAND);
            }
        }
        stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), modifier, EntityEquipmentSlot.MAINHAND);
    }


    //Only called to get the initial damage!
    private static double getAttackDamage(ItemStack stack, EntityLivingBase entity) {
        double value = 0;
        for (AttributeModifier modifier : stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
           //Copied from the item tooltip code
            value += entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
            value += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
            value += modifier.getAmount();
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
            return nbt.getShort("SoulDevourKills") > 75 ? 75 : nbt.getShort("SoulDevourKills");
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

    private static double readInitialDamage(ItemStack stack, EntityLivingBase entity) {
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }
        assert nbt != null;
        if (nbt.hasKey("InitialDamage")) {
            return nbt.getDouble("InitialDamage");
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMaxEnchantability(enchantmentLevel) + 100;
    }
}
