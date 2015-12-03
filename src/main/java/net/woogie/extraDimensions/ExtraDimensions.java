package net.woogie.extraDimensions;

import java.util.EnumMap;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.woogie.extraDimensions.proxy.CommonProxy;

@Mod(modid = "extraDimensions", version = "1.8.0", name = "ExtraDimensions", useMetadata = true, dependencies = "required-after:Forge@[11.14.4.1563,);after:TooManyBiomes;after:BiomesOPlenty")
public class ExtraDimensions {

	@SidedProxy(clientSide = "net.woogie.extraDimensions.proxy.ClientProxy", serverSide = "net.woogie.extraDimensions.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance("ExtraDimensions")
	public static ExtraDimensions instance;
	public static EnumMap<Side, FMLEmbeddedChannel> channels;	
	public static SimpleNetworkWrapper network;
	public static final String oldChannel = "ExtraDimensionsOld";
	public static final String simpleChannel = "ExtraDimensions";

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		proxy.serverStopped(event);
	}
}