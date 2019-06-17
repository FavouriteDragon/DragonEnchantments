package com.favouritedragon.dragonenchantments;

import com.favouritedragon.dragonenchantments.common.enchantments.sword.SoulDevour;
import com.favouritedragon.dragonenchantments.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = DragonEnchants.MODID, name = DragonEnchants.NAME, version = DragonEnchants.VERSION)
public class DragonEnchants {
	public static final String MODID = "dragon_enchants";
	public static final String NAME = "Dragon Enchantments";
	public static final String VERSION = "1.0";
	public static final String CLIENT = "com.favouritedragon.dragonenchantments.client.ClientProxy";
	public static final String SERVER = "com.favouritedragon.dragonenchantments.proxy.CommonProxy";

	@Mod.Instance(DragonEnchants.MODID)
	public static DragonEnchants instance;

	@SidedProxy(clientSide = DragonEnchants.CLIENT, serverSide = DragonEnchants.SERVER)
	public static IProxy proxy;
	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
		proxy.registerRender();
		RegisterHandler.registerAll();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.Init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
