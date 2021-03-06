package com.favouritedragon.dragonenchantments.common.network;

import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.StormStrider;
import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.VoidWalker;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSStormStride implements IMessage {

	public int uuid_length;
	public String uuid;
	public double x, y, z;
	int level;

	public PacketSStormStride() {
	}

	public PacketSStormStride(String entityuuid, double x, double y, double z, int level) {
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

	public static class Handler implements IMessageHandler<PacketSStormStride, IMessage> {

		public Handler() {
		}

		@Override
		public IMessage onMessage(PacketSStormStride message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(message.uuid));
			StormStrider.StormStride(player, message.x, message.y, message.z, message.level);
			return null;
		}
	}
}
