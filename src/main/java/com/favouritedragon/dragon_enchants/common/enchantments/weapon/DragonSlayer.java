package com.favouritedragon.dragon_enchants.common.enchantments.weapon;

import com.favouritedragon.dragon_enchants.DragonEnchants;
import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import com.favouritedragon.dragon_enchants.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class DragonSlayer extends Enchantment {
    public DragonSlayer() {
        super(Rarity.RARE, Objects.requireNonNull(ModEnchantments.WEAPONS), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
        setRegistryName(DragonEnchants.MODID + ":" + "dragon_slayer");
    }

    @SubscribeEvent
    public static void onDragonHurt(LivingHurtEvent event) {
        Entity attacker = event.getSource().getTrueSource();
        if (attacker instanceof LivingEntity && !attacker.world.isRemote) {
            int level = DragonUtils.getHeldLevelForEnchantment((LivingEntity) attacker, ModEnchantments.dragonSlayer, event);
            if (level > 0) {
                float amount = Math.max(event.getAmount(), event.getEntityLiving().getHealth());
                if (event.getEntityLiving() instanceof EnderDragonEntity) {
                    event.setAmount(event.getAmount() * (1 + 0.1F * level));
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + 5 * enchantmentLevel;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMaxEnchantability(enchantmentLevel) + 200;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
}
