package net.woogie.extraDimensions.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.woogie.extraDimensions.ExtraDimensionsUtil;

public class CreateDimensionIdMessageHandler implements IMessageHandler<CreateDimensionIdMessage, IMessage> {

	@Override
	public IMessage onMessage(CreateDimensionIdMessage message, MessageContext ctx) {

		ExtraDimensionsUtil.createDimensionClientOnly(message.dimensionId);

		return null;
	}
}
