package com.favouritedragon.dragonenchants.enchantments.all.armour;

import com.favouritedragon.dragonenchants.DragonEnchants;
import com.favouritedragon.dragonenchants.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

//This subscriber is for generic ingame events. Assumed forge.
@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class StormProtection extends Enchantment {

    public StormProtection() {
        super(Rarity.RARE, EnchantmentType.ARMOR, new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST,
                EquipmentSlotType.LEGS, EquipmentSlotType.FEET});
    }

    public static float getLightningDamageReduction(LivingEntity entityLivingBaseIn, float damage) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.STORM_PROTECTION, entityLivingBaseIn);
        if (i > 0) {
            damage -= MathHelper.floor(damage * (i * 0.15F));
        }

        return damage;
    }

    public static int getFireTimeForEntity(LivingEntity livingEntity, int level) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.STORM_PROTECTION, livingEntity);
        if (i > 0) {
            level -= MathHelper.floor((float) level * (float) i * 0.075F);
        }

        return level;
    }

    @SubscribeEvent
    public static void onLightningHurt(LivingHurtEvent event) {
        if (event.getSource() == DamageSource.LIGHTNING_BOLT)
            event.setAmount(getLightningDamageReduction(event.getEntityLiving(), event.getAmount()));
        int fire = event.getEntityLiving().getFireTimer();
        fire = getFireTimeForEntity(event.getEntityLiving(), fire * 20);

        //Extinguishes and reignites based on how long they should be on fire.
        event.getEntityLiving().extinguish();
        event.getEntityLiving().setFire(fire);
    }

    @Override
    public int calcModifierDamage(int level, @Nullable DamageSource source) {
        if (source == DamageSource.LIGHTNING_BOLT)
            return level * 2;
        return super.calcModifierDamage(level, source);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMaxEnchantability(enchantmentLevel) + 5;
    }
}
