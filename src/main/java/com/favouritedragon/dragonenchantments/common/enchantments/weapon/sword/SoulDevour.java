package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
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
                int level = DragonUtils.getHeldLevelForEnchantment(trueEntity, ModEnchantments.soulDevour);
                short numberKilled = readSoulsKilled(stack);
                numberKilled++;
                writeNbt(stack, numberKilled, readInitialDamage(stack, trueEntity), readTotalHealthConsumed(stack));
                trueEntity.heal(((EntityLivingBase) target).getMaxHealth() / 4);
                if (numberKilled < level * 25 + 1) {
                    float newMod = (float) (readInitialDamage(stack, trueEntity) * ((100 + numberKilled) / 100F));
                    float oldMod = (float) (readInitialDamage(stack, trueEntity) * ((100 + numberKilled - 1) / 100F));
                    writeModifier(stack, trueEntity, newMod, oldMod);
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

    private static void writeModifier(ItemStack stack, EntityLivingBase entity, double value, double oldValue) {
        AttributeModifier modifier;
        IAttributeInstance attack = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        Multimap<String, AttributeModifier> attributeMap = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        boolean shouldRemove = false;

        attack.removeModifier(MODIFIER_UUID);

        for (AttributeModifier mod : stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
            if (mod.getID().equals(MODIFIER_UUID))
                shouldRemove = true;

        if (shouldRemove) {
            NBTTagList compound = stack.getTagCompound().getTagList("AttributeModifiers", 10);
            int i = 0;
            compound.removeTag(i);
        }
        modifier = new AttributeModifier(MODIFIER_UUID, "Soul Devour Damage Boost", value, 0);
        for (Map.Entry<String, AttributeModifier> originalMod : attributeMap.entries())
            stack.addAttributeModifier(originalMod.getKey(), originalMod.getValue(), EntityEquipmentSlot.MAINHAND);

        stack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), modifier, EntityEquipmentSlot.MAINHAND);
    }

    private static double getAttackDamage(ItemStack stack, EntityLivingBase entity) {
        double value = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(MODIFIER_UUID) != null) {
            AttributeModifier modifier = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(MODIFIER_UUID);
            value -= modifier.getAmount();
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
