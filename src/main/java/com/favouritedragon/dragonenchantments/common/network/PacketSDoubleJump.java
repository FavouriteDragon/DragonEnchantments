package com.favouritedragon.dragonenchantments.common.network;

import com.favouritedragon.dragonenchantments.common.enchantments.boots.CloudWalker;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSDoubleJump implements IMessage {

    public PacketSDoubleJump() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketSDoubleJump, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSDoubleJump message, MessageContext ctx) {
            CloudWalker.doDoubleJump(ctx.getServerHandler().player);
            return null;
        }
    }
}