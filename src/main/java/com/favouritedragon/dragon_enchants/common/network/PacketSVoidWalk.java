package com.favouritedragon.dragon_enchants.common.network;

import java.util.UUID;

import com.favouritedragon.dragon_enchants.common.enchantments.armour.boots.VoidWalker;
import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSVoidWalk implements IMessage {

    public int uuid_length;
    public String uuid;
    public int x, y, z;

    public PacketSVoidWalk() {
    }

    public PacketSVoidWalk(String entityuuid, BlockPos pos) {
        this.uuid_length = entityuuid.length();
        this.uuid = entityuuid;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid_length = buf.readInt();
        uuid = buf.readCharSequence(uuid_length, Charsets.UTF_8).toString();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(uuid_length);
        buf.writeCharSequence(uuid, Charsets.UTF_8);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public static class Handler implements IMessageHandler<PacketSVoidWalk, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSVoidWalk message, MessageContext ctx) {
            EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(message.uuid));
            VoidWalker.onVoidWalk(player, new BlockPos(message.x, message.y, message.z));
            return null;
        }
    }
}