package com.favouritedragon.dragonenchantments.common.network;

import com.favouritedragon.dragonenchantments.common.enchantments.boots.CloudWalker;
import com.favouritedragon.dragonenchantments.common.enchantments.boots.VoidWalker;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSVoidWalk implements IMessage {

    public int entity_id;

    public PacketSVoidWalk() {
    }

    public PacketSVoidWalk(int entityId) {
        this.entity_id = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entity_id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entity_id);
    }

    public static class Handler implements IMessageHandler<PacketSVoidWalk, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSVoidWalk message, MessageContext ctx) {
            EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(message.entity_id);
            VoidWalker.onVoidWalk(player);
            return null;
        }
    }
}