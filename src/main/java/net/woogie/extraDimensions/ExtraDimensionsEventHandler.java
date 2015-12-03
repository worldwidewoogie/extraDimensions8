package net.woogie.extraDimensions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.woogie.extraDimensions.network.ExtraDimensionsDimensionIdPacket;

public class ExtraDimensionsEventHandler {

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing e) {
		if (e.entity instanceof EntityPlayer) {
			if (ExtraDimensionsPlayerSurvivalInventoryProperties.get((EntityPlayer) e.entity) == null) {
				ExtraDimensionsPlayerSurvivalInventoryProperties.register((EntityPlayer) e.entity);
			}
			if (ExtraDimensionsPlayerCreativeInventoryProperties.get((EntityPlayer) e.entity) == null) {
				ExtraDimensionsPlayerCreativeInventoryProperties.register((EntityPlayer) e.entity);
			}
		}
	}

	@SubscribeEvent
	public void serverConnectionFromClient(FMLNetworkEvent.ServerConnectionFromClientEvent e) {
		e.manager.sendPacket(ExtraDimensionsDimensionIdPacket.createPacket(ExtraDimensionsUtil.getDimensionIds()));
	}
}
