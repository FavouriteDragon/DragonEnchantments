package com.favouritedragon.dragonenchantments.common.network;

import com.favouritedragon.dragonenchantments.common.enchantments.armour.boots.CloudWalker;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSDoubleJump implements IMessage {

    public int entity_id;

    public PacketSDoubleJump() {
    }

    public PacketSDoubleJump(int entityId) {
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

    public static class Handler implements IMessageHandler<PacketSDoubleJump, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSDoubleJump message, MessageContext ctx) {
            EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(message.entity_id);
            CloudWalker.doDoubleJump(player);
            return null;
        }
    }
}