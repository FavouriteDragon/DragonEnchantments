package com.favouritedragon.dragonenchantments;

import com.favouritedragon.dragonenchantments.common.enchantments.ModEnchantments;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.DragonSlayer;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.Lifesteal;
import com.favouritedragon.dragonenchantments.common.network.PacketSDoubleJump;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = DragonEnchants.MODID)
public class RegisterHandler {
	private static final int LIVING_UPDATE_INTERVAL = 3;
	private static final int PROJECTILE_UPDATE_INTERVAL = 10;

	/**
	 * Private helper method for registering entities; keeps things neater. For some reason, Forge 1.11.2 wants a
	 * ResourceLocation and a string name... probably because it's transitioning to the registry system.
	 */
	private static void registerEntity(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		ResourceLocation registryName = new ResourceLocation(DragonEnchants.MODID, name);
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.toString(), id, DragonEnchants.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	/**
	 * Private helper method for registering entities with eggs; keeps things neater. For some reason, Forge 1.11.2
	 * wants a ResourceLocation and a string name... probably because it's transitioning to the registry system.
	 */
	private static void registerEntityAndEgg(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggColour, int spotColour) {
		ResourceLocation registryName = new ResourceLocation(DragonEnchants.MODID, name);
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.toString(), id, DragonEnchants.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
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

	public static void registerPackets(){
		//DragonEnchants.NETWORK.registerMessage(PacketSDoubleJump.Handler.class, PacketSDoubleJump.class, 1, Side.SERVER);
	}

	@SubscribeEvent
	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(ModEnchantments.cloudWalker);
		event.getRegistry().register(ModEnchantments.dragonSlayer);
		event.getRegistry().register(ModEnchantments.enderference);
		event.getRegistry().register(ModEnchantments.lifeSteal);
		event.getRegistry().register(ModEnchantments.thunderAspect);
		event.getRegistry().register(ModEnchantments.venomous);
	}

	static void registerAll() {
		registerLoot();
		registerEntities();
		registerItems();
		registerPackets();
	}

}