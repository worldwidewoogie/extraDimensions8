package net.woogie.extraDimensions.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.woogie.extraDimensions.ExtraDimensionsUtil;

public class DeleteDimensionIdMessageHandler implements IMessageHandler<DeleteDimensionIdMessage, IMessage> {

	@Override
	public IMessage onMessage(DeleteDimensionIdMessage message, MessageContext ctx) {

		ExtraDimensionsUtil.deleteDimensionClientOnly(message.dimensionId);

		return null;
	}
}
