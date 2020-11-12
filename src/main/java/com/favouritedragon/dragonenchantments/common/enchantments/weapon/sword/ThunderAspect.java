package com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword;

import com.favouritedragon.dragonenchantments.DragonEnchants;
import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.util.DragonUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class ThunderAspect extends Enchantment {

    private static final HashMap<String, Boolean> isSweepAttack = new HashMap<>();

    public ThunderAspect() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setRegistryName("thunder_aspect");
        setName(DragonEnchants.MODID + ":" + "thunder_aspect");
    }


    //Ensures isSweepAttack is never null
    @SubscribeEvent
    public static void onEnchantmentGet(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() != null) {
            EntityLivingBase entity = event.getEntityLiving();
            int level = DragonUtils.getHeldLevelForEnchantment(entity, ModEnchantments.thunderAspect);
            if (level > 0) {
                if (!isSweepAttack.containsKey(entity.getUniqueID().toString())) {
                    setIsSweepAttack(entity.getUniqueID().toString(), false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerSweep(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player != null) {
            setIsSweepAttack(player.getUniqueID().toString(), player.getCooledAttackStrength(0) >= 1.0);
        }
    }

    @SubscribeEvent
    public static void onSwingEvent(LivingAttackEvent event) {
        Entity attacker = event.getSource().getTrueSource();
        EntityLivingBase hurt = event.getEntityLiving();
        if (attacker instanceof EntityLivingBase) {
            ItemStack stack = ((EntityLivingBase) attacker).getHeldItemMainhand();
            if (hurt != null && hurt != attacker && stack.isItemEnchanted() && !(event.getSource() instanceof EntityDamageSourceIndirect)) {
                if (getSweepAttack(attacker.getUniqueID().toString())) {
                    int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
                    if (level > 0) {
                        if (!attacker.world.isRemote) {
                            float amount = level;
                            if (attacker.world.isRaining()) {
                                amount *= 1.25F;
                            } else if (attacker.world.isThundering()) {
                                amount *= 1.5f;
                            }
                            hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT, event.getAmount() / 5 * amount);
                            Vec3d lookVec = attacker.getLookVec();
                            hurt.addVelocity(lookVec.x * (1 + 0.2 * amount), lookVec.y > 0 ? lookVec.y * (1 + 0.2 * amount) : 0.2 * amount,
                                    lookVec.z * (1 + 0.2 * amount));
                        }
                        if (attacker.world.isRemote) {
                            attacker.world.playSound(hurt.posX, hurt.posY, hurt.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.PLAYERS,
                                    1.0F + attacker.world.rand.nextFloat(), 1.0F + attacker.world.rand.nextFloat(), true);
                        }
                    }
                    setIsSweepAttack(attacker.getUniqueID().toString(), false);

                } else {
                    int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.thunderAspect, stack);
                    if (level > 0) {
                        if (!attacker.world.isRemote) {
                            float amount = level;
                            if (attacker.world.isRaining()) {
                                amount *= 1.25F;
                            } else if (attacker.world.isThundering()) {
                                amount *= 1.5f;
                            }
                            hurt.attackEntityFrom(DamageSource.LIGHTNING_BOLT, event.getAmount() / 10 * amount);
                            Vec3d lookVec = attacker.getLookVec();
                            hurt.addVelocity(lookVec.x * (1 + 0.1 * amount), lookVec.y > 0 ? lookVec.y * (1 + 0.1 * amount) : 0.1 * amount,
                                    lookVec.z * (1 + 0.1 * amount));
                        }
                    }
                }

            }
        }

    }

    private static void setIsSweepAttack(String UUID, boolean sweepAttack) {
        if (isSweepAttack.containsKey(UUID)) {
            isSweepAttack.replace(UUID, sweepAttack);
        } else {
            isSweepAttack.put(UUID, sweepAttack);
        }
    }

    private static boolean getSweepAttack(String UUID) {
        return isSweepAttack.get(UUID) != null && isSweepAttack.get(UUID);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 80;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return getMinEnchantability(enchantmentLevel) + 200;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
}
