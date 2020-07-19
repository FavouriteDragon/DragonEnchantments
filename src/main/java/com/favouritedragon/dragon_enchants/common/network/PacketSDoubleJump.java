package com.favouritedragon.dragon_enchants.common.network;

import java.util.UUID;

import com.favouritedragon.dragon_enchants.common.enchantments.armour.boots.CloudWalker;
import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSDoubleJump implements IMessage {

    public int uuid_length;
    public String uuid;

    public PacketSDoubleJump() {
    }

    public PacketSDoubleJump(String entityuuid) {
        this.uuid_length = entityuuid.length();
        this.uuid = entityuuid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid_length = buf.readInt();
        uuid = buf.readCharSequence(uuid_length, Charsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(uuid_length);
        buf.writeCharSequence(uuid, Charsets.UTF_8);
    }

    public static class Handler implements IMessageHandler<PacketSDoubleJump, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSDoubleJump message, MessageContext ctx) {
            EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(message.uuid));
            CloudWalker.doDoubleJump(player);
            return null;
        }
    }
}