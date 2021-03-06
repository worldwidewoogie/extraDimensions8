package net.woogie.extraDimensions.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CreateDimensionIdMessage implements IMessage {

	int dimensionId;

	public CreateDimensionIdMessage() {
	}

	public CreateDimensionIdMessage(int i) {
		this.dimensionId = i;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.dimensionId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimensionId);
	}

}
