package net.woogie.extraDimensions.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.woogie.extraDimensions.ExtraDimensions;

public abstract class ExtraDimensionsPacket {
        protected static FMLProxyPacket buildPacket(PacketBuffer payload) {
                return new FMLProxyPacket(payload, ExtraDimensions.oldChannel);
        }

        public abstract byte getPacketType();

        public abstract void handle(PacketBuffer packetBuffer,
                        EntityPlayer paramEntityPlayer);
}
