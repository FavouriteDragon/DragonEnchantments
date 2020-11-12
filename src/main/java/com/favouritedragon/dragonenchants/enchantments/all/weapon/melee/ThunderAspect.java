package com.favouritedragon.dragonenchants.enchantments.all.weapon.melee.sword;

import com.favouritedragon.dragonenchants.DragonEnchants;
import com.favouritedragon.dragonenchants.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchants.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ThunderAspect extends Enchantment {

    private static final HashMap<UUID, Boolean> sweepAttack = new HashMap<>();

    public ThunderAspect() {
        super(Rarity.UNCOMMON, ModEnchantments.MELEE_WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    private static boolean isSweepAttack(UUID playerId) {
        return sweepAttack.getOrDefault(playerId, false);
    }

    private static void setSweepAttack(UUID playerId, boolean isSweep) {
        if (sweepAttack.containsKey(playerId))
            sweepAttack.replace(playerId, isSweep);
        else sweepAttack.put(playerId, isSweep);
    }

    @SubscribeEvent
    public static void onSweepAttack(AttackEntityEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.THUNDER_ASPECT,
                    player.getHeldItemMainhand()) > 0) {
                //It's 0.5 because that's what vanilla does when the player attacks with an item.
                setSweepAttack(player.getUniqueID(),
                        player.getCooledAttackStrength(0.5F) >= 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
            ItemStack stack = attacker.getHeldItemMainhand();
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.THUNDER_ASPECT, stack);
            if (level > 0) {
                if (stack.isEnchanted()) {
                    if (!(event.getSource() instanceof IndirectEntityDamageSource) && !event.getSource().equals(DamageSource.LIGHTNING_BOLT))
                        if (attacker instanceof PlayerEntity) {
                            if (event.getSource().equals(DamageSource.causePlayerDamage((PlayerEntity) attacker))) {
                                handleThunderDamage(attacker, event.getEntityLiving(), attacker.world,
                                        level, event.getAmount(), isSweepAttack(attacker.getUniqueID()));
                            }
                        } else {
                            if (event.getSource().equals(DamageSource.causeMobDamage(attacker)))
                                handleThunderDamage(attacker, event.getEntityLiving(), attacker.world,
                                        level, event.getAmount(), false);
                        }
                }
            }
        }
    }

    private static void handleThunderDamage(LivingEntity attacker, LivingEntity target, World world,
                                            int enchantmentLevel, float initialDamage, boolean playSounds) {
        float extraDamage = 1 + enchantmentLevel / 2F;
        float knockbackMult;
        if (DragonUtils.canRainAtPos(attacker.getPosition(), world)) {
            if (world.isThundering())
                extraDamage *= 1.75;
            else if (world.isRaining())
                extraDamage *= 1.25;
        }

        target.hurtResistantTime = 0;
        extraDamage *= initialDamage / 10;
        knockbackMult = extraDamage;

        if (!attacker.world.isRemote) {
            Vector3d knockback = attacker.getLookVec().scale(knockbackMult / 20);
            target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, extraDamage);
            target.addVelocity(knockback.x, knockback.y + 0.15, knockback.z);
        }

        if (playSounds) {
            world.playSound(attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(),
                    SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 5F,
                    1.0F + world.rand.nextFloat() / 5F, false);
            world.playSound(attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(),
                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0F + world.rand.nextFloat() / 5F,
                    1.0F + world.rand.nextFloat() / 5F, true);
        }

    }

    @Override
    public int getMaxLevel() {
        return 3;
    }


}
