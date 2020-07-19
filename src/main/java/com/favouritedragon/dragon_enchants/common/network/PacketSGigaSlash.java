package com.favouritedragon.dragonenchantments.common.network;

import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.StormStrider;
import com.favouritedragon.dragonenchantments.common.enchantments.weapon.sword.Gigaslash;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSGigaSlash implements IMessage {

	public int uuid_length;
	public String uuid;
	public double damageMod;
	private int level;
	private boolean writeNbt;

	public PacketSGigaSlash() {
	}

	public PacketSGigaSlash(String entityUuid, double damageMod, int level, boolean writeNbt) {
		this.uuid_length = entityUuid.length();
		this.uuid = entityUuid;
		this.damageMod = damageMod;
		this.level = level;
		this.writeNbt = writeNbt;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		uuid_length = buf.readInt();
		uuid = buf.readCharSequence(uuid_length, Charsets.UTF_8).toString();
		damageMod = buf.readDouble();
		level = buf.readInt();
		writeNbt = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(uuid_length);
		buf.writeCharSequence(uuid, Charsets.UTF_8);
		buf.writeDouble(damageMod);
		buf.writeInt(level);
		buf.writeBoolean(writeNbt);
	}

	public static class Handler implements IMessageHandler<PacketSGigaSlash, IMessage> {

		public Handler() {
		}

		@Override
		public IMessage onMessage(PacketSGigaSlash message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(message.uuid));
			if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemSword) {
				if (message.writeNbt)
					Gigaslash.setNbt(player.getHeldItemMainhand(), (float) message.damageMod);
				else Gigaslash.attackEnemies(player, message.level, (float) message.damageMod);
			}
			return null;
		}
	}
}
