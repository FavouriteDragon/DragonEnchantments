package com.favouritedragon.dragon_enchants.common.network;

import com.favouritedragon.dragon_enchants.common.enchantments.armour.boots.WindWalker;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSWindWalk implements IMessage {

	public int uuid_length;
	public String uuid;
	public double x, y, z;
	int level;

	public PacketSWindWalk() {
	}

	public PacketSWindWalk(String entityuuid, double x, double y, double z, int level) {
		this.uuid_length = entityuuid.length();
		this.uuid = entityuuid;
		this.x = x;
		this.y = y;
		this.z = z;
		this.level = level;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		uuid_length = buf.readInt();
		uuid = buf.readCharSequence(uuid_length, Charsets.UTF_8).toString();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		level = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(uuid_length);
		buf.writeCharSequence(uuid, Charsets.UTF_8);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeInt(level);
	}

	public static class Handler implements IMessageHandler<PacketSWindWalk, IMessage> {

		public Handler() {
		}

		@Override
		public IMessage onMessage(PacketSWindWalk message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(message.uuid));
			assert player != null;
			WindWalker.windWalk(player, message.x, message.y, message.z, message.level);
			return null;
		}
	}
}
