package com.favouritedragon.dragon_enchants;

import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class RegisterHandler {
    private static final int LIVING_UPDATE_INTERVAL = 3;
    private static final int PROJECTILE_UPDATE_INTERVAL = 10;
    private static final String REV = "1";
    public static SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(DragonEnchants.MODID, "DragonEnchantsPackets"), () -> REV, REV::equals, REV::equals);


    public static void registerItems() {

    }

    public static void registerEntities() {
        int id = 0;

    }

    public static void registerLoot() {

    }

    public static void registerPackets() {
        //	NETWORK.registerMessage(PacketSDoubleJump.Handler.class, PacketSDoubleJump.class, 1, Side.SERVER);
        //	NETWORK.registerMessage(PacketSVoidWalk.Handler.class, PacketSVoidWalk.class, 2, Side.SERVER);
        //	NETWORK.registerMessage(PacketSStormStride.Handler.class, PacketSStormStride.class, 3, Side.SERVER);
        //	NETWORK.registerMessage(PacketSWindWalk.Handler.class, PacketSWindWalk.class, 4, Side.SERVER);
        //	NETWORK.registerMessage(PacketSGigaSlash.Handler.class, PacketSGigaSlash.class, 5, Side.SERVER);
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(ModEnchantments.berserk);
        event.getRegistry().register(ModEnchantments.cloudWalker);
        event.getRegistry().register(ModEnchantments.dolphin);
        event.getRegistry().register(ModEnchantments.doubleEdge);
        event.getRegistry().register(ModEnchantments.dragonSlayer);
        event.getRegistry().register(ModEnchantments.enderference);
        event.getRegistry().register(ModEnchantments.endWalker);
        event.getRegistry().register(ModEnchantments.gigaSlash);
        event.getRegistry().register(ModEnchantments.homing);
        event.getRegistry().register(ModEnchantments.lifeSteal);
        event.getRegistry().register(ModEnchantments.oblivion);
        event.getRegistry().register(ModEnchantments.solarPowered);
        event.getRegistry().register(ModEnchantments.soulDevour);
        event.getRegistry().register(ModEnchantments.stormProtection);
        event.getRegistry().register(ModEnchantments.stormStrider);
        event.getRegistry().register(ModEnchantments.thunderAspect);
        event.getRegistry().register(ModEnchantments.venomous);
        event.getRegistry().register(ModEnchantments.voidHunger);
        event.getRegistry().register(ModEnchantments.voidWalker);
        event.getRegistry().register(ModEnchantments.windWalker);
    }

    static void registerAll() {
        registerLoot();
        registerEntities();
        registerItems();
        registerPackets();
    }

}