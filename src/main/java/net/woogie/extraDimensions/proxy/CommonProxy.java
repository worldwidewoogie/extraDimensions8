package net.woogie.extraDimensions.proxy;

import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.woogie.extraDimensions.ExtraDimensions;
import net.woogie.extraDimensions.ExtraDimensionsEventHandler;
import net.woogie.extraDimensions.ExtraDimensionsUtil;
import net.woogie.extraDimensions.command.CommandXdCreate;
import net.woogie.extraDimensions.command.CommandXdDelete;
import net.woogie.extraDimensions.command.CommandXdList;
import net.woogie.extraDimensions.command.CommandXdRename;
import net.woogie.extraDimensions.command.CommandXdTp;
import net.woogie.extraDimensions.network.CreateDimensionIdMessage;
import net.woogie.extraDimensions.network.CreateDimensionIdMessageHandler;
import net.woogie.extraDimensions.network.DeleteDimensionIdMessage;
import net.woogie.extraDimensions.network.DeleteDimensionIdMessageHandler;
import net.woogie.extraDimensions.network.ExtraDimensionsDimensionIdPacket;
import net.woogie.extraDimensions.network.ExtraDimensionsPacketHandler;

public class CommonProxy {

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ExtraDimensionsEventHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void preInit(FMLPreInitializationEvent event) {

		ExtraDimensionsPacketHandler.registerPacketHandler(new ExtraDimensionsDimensionIdPacket());
		ExtraDimensionsPacketHandler.bus = NetworkRegistry.INSTANCE.newEventDrivenChannel(ExtraDimensions.oldChannel);
		ExtraDimensionsPacketHandler.bus.register(new ExtraDimensionsPacketHandler());

		ExtraDimensions.network = NetworkRegistry.INSTANCE.newSimpleChannel(ExtraDimensions.simpleChannel);
		ExtraDimensions.network.registerMessage(CreateDimensionIdMessageHandler.class, CreateDimensionIdMessage.class,
				0, Side.CLIENT);
		ExtraDimensions.network.registerMessage(DeleteDimensionIdMessageHandler.class, DeleteDimensionIdMessage.class,
				1, Side.CLIENT);

		FMLCommonHandler.instance().bus().register(new ExtraDimensionsEventHandler());
	}

	public void serverStarting(FMLServerStartingEvent event) {

		((ServerCommandManager) event.getServer().getCommandManager()).registerCommand(new CommandXdDelete());
		((ServerCommandManager) event.getServer().getCommandManager()).registerCommand(new CommandXdCreate());
		((ServerCommandManager) event.getServer().getCommandManager()).registerCommand(new CommandXdList());
		((ServerCommandManager) event.getServer().getCommandManager()).registerCommand(new CommandXdRename());
		((ServerCommandManager) event.getServer().getCommandManager()).registerCommand(new CommandXdTp());

		ExtraDimensionsUtil.registerExistingDimensions();

	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
	}
}
