package com.favouritedragon.dragon_enchants;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Logger;

@Mod(DragonEnchants.MODID)
public class DragonEnchants {

	public static DragonEnchants INSTANCE;

	public DragonEnchants() {
		INSTANCE = this;
		RegisterHandler.registerAll();
		//logger = new org.apache.logging.log4j.core.Logger();
	}

	public static final String MODID = "dragon_enchants";
	public static final String NAME = "Dragon Enchantments";
	public static final String VERSION = "1.0";
	public static final String CLIENT = "com.favouritedragon.dragonenchantments.client.ClientProxy";
	public static final String SERVER = "com.favouritedragon.dragonenchantments.proxy.CommonProxy";

	private static Logger logger;

	//TODO: Add visual enchantments!
}
