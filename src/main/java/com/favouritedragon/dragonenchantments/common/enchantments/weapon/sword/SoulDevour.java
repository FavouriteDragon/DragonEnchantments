package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class SoulDevour extends Enchantment {

    //Same as vanilla. We got dem hacks boys
    public final static UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

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
                numberKilled++;
                writeNbt(stack, numberKilled, readInitialDamage(stack, trueEntity), readTotalHealthConsumed(stack));
                trueEntity.heal(((EntityLivingBase) target).getMaxHealth() / 4);
                if (numberKilled < level * 25 + 1) {
                    float mod = (((100 + numberKilled) / 100F));
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
                double healthConsumed = readTotalHealthConsumed(stack);
                healthConsumed += event.getAmount();
                writeNbt(stack, readSoulsKilled(stack), readInitialDamage(stack, trueEntity), healthConsumed);
                System.out.println(event.getAmount());
            }
        }
    }

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return super.calcDamageByCreature(level, creatureType);
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
        Multimap<String, AttributeModifier> attributeMap = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        AbstractAttributeMap abstractMap = entity.getAttributeMap();

        //Removes modifiers so they can update
        abstractMap.removeAttributeModifiers(attributeMap);

        //Removes the default attack damage; soul devour is the captain now
        attributeMap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        attributeMap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), modifier);
        System.out.println(attributeMap);
        //Reapplies them
        abstractMap.applyAttributeModifiers(attributeMap);
    }

    private static double getAttackDamage(ItemStack stack, EntityLivingBase entity) {
        double value = 0;
        for (AttributeModifier modifier : stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
            value += entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
            value += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
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

    private static double readInitialDamage(ItemStack stack, EntityLivingBase entity) {
        return getAttackDamage(stack, entity);
    }
}
