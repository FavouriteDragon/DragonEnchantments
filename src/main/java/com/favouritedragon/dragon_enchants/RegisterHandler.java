package com.favouritedragon.dragon_enchants;

import com.favouritedragon.dragon_enchants.common.enchantments.ModEnchantments;
import com.favouritedragon.dragon_enchants.common.network.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class RegisterHandler {
	private static final int LIVING_UPDATE_INTERVAL = 3;
	private static final int PROJECTILE_UPDATE_INTERVAL = 10;
	private static final String REV = "1";
	public static SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(DragonEnchants.MODID, "DragonEnchantsPackets"), () -> REV, REV::equals, REV::equals);


	/**
	 * Private helper method for registering entities; keeps things neater. For some reason, Forge 1.11.2 wants a
	 * ResourceLocation and a string name... probably because it's transitioning to the registry system.
	 */
	private static void registerEntity(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		ResourceLocation registryName = new ResourceLocation(DragonEnchants.MODID, name);
		Entity.registerModEntity(registryName, entityClass, registryName.toString(), id, DragonEnchants.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	/**
	 * Private helper method for registering entities with eggs; keeps things neater. For some reason, Forge 1.11.2
	 * wants a ResourceLocation and a string name... probably because it's transitioning to the registry system.
	 */
	private static void registerEntityAndEgg(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggColour, int spotColour) {
		ResourceLocation registryName = new ResourceLocation(DragonEnchants.MODID, name);
		GameRegistry.registerModEntity(registryName, entityClass, registryName.toString(), id, DragonEnchants.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		EntityRegistry.registerEgg(registryName, eggColour, spotColour);
	}


	public static void registerItems() {

	}

	public static void registerEntities() {
		int id = 0;

	}

	public static void registerLoot() {

	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

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