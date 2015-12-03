package net.woogie.extraDimensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.woogie.extraDimensions.network.CreateDimensionIdMessage;
import net.woogie.extraDimensions.network.DeleteDimensionIdMessage;
import net.woogie.extraDimensions.world.ExtraDimensionsWorldProvider;

public class ExtraDimensionsUtil {
	private static List<Integer> dimensionIds = new ArrayList<Integer>(Arrays.asList(-1, 0, 1));
	private static Map<Integer, String> dimensionNames = new HashMap<Integer, String>();
	private static Map<Integer, WorldInfo> dimensionWorldInfos = new HashMap<Integer, WorldInfo>();

	public static String createDimension(String dimensionName, String gameTypeString, String worldType, Long seed,
			String options) {

		DimensionManager.loadDimensionDataMap(null);

		Integer dimensionId = DimensionManager.getNextFreeDimId();

		if (dimensionName == null || dimensionName.equals("")) {
			return ("Dimension must have a name");
		}

		if (dimensionNameExists(dimensionName)) {
			return ("Dimension with name " + dimensionName + " already exists.");
		}

		if (!gameTypeString.equals("creative") && !gameTypeString.equals("survival")) {
			return ("Invalid Game Type");
		}

		if (!ExtraDimensionsUtil.isValidWorldType(worldType)) {
			return ("World Type " + worldType + " is not valid.");
		}

		GameType gameType = gameTypeString.equals("survival") ? GameType.SURVIVAL : GameType.CREATIVE;

		createDimensionFiles(dimensionId, dimensionName, gameType, worldType, seed, options);

		DimensionManager.registerProviderType(dimensionId, ExtraDimensionsWorldProvider.class, false);
		DimensionManager.registerDimension(dimensionId, dimensionId);

		dimensionIds.add(dimensionId);

		dimensionNames.put(dimensionId, dimensionName);

		if (!MinecraftServer.getServer().isSinglePlayer()) {
			ExtraDimensions.network.sendToAll(new CreateDimensionIdMessage(dimensionId));
		}

		return "Dimension " + dimensionName + " created with ID " + dimensionId;

	}

	public static void createDimensionClientOnly(int dimensionId) {
		if (!dimensionIds.contains(dimensionId)) {
			DimensionManager.registerProviderType(dimensionId, ExtraDimensionsWorldProvider.class, false);
			DimensionManager.registerDimension(dimensionId, dimensionId);
			dimensionIds.add(dimensionId);
		}
	}

	private static void createDimensionFiles(int dimension, String dimensionName, GameType gameType,
			String worldTypeName, Long seed, String options) {

		WorldInfo overWorld = DimensionManager.getWorld(0).getWorldInfo();

		NBTTagCompound data = new NBTTagCompound();

		if (seed == 0) {
			Random random = new Random(System.currentTimeMillis());
			seed = random.nextLong();
		}

		data.setLong("RandomSeed", seed);

		data.setString("generatorName", worldTypeName);

		data.setInteger("generatorVersion", overWorld.getTerrainType().getGeneratorVersion());
		data.setString("generatorOptions", options);
		data.setInteger("GameType", gameType.getID());
		data.setBoolean("MapFeatures", overWorld.isMapFeaturesEnabled());
		data.setInteger("SpawnX", 0);
		data.setInteger("SpawnY", 0);
		data.setInteger("SpawnZ", 0);
		data.setLong("Time", overWorld.getWorldTotalTime());
		data.setLong("DayTime", overWorld.getWorldTime());
		data.setLong("SizeOnDisk", 0L);
		data.setString("LevelName", dimensionName);
		data.setInteger("version", overWorld.getSaveVersion());
		data.setInteger("rainTime", 0);
		data.setBoolean("raining", false);
		data.setInteger("thunderTime", 0);
		data.setBoolean("thundering", false);
		data.setBoolean("hardcore", overWorld.isHardcoreModeEnabled());
		data.setBoolean("initialized", false);
		data.setBoolean("allowCommands", overWorld.areCommandsAllowed());

		WorldInfo worldInfo = new WorldInfo(data);

		dimensionWorldInfos.put(dimension, worldInfo);

		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setTag("Data", data);

		File file = new File(DimensionManager.getCurrentSaveRootDirectory(),
				"/ExtraDimensions/" + dimension + "/level.dat");

		if (!file.exists())
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String deleteDimension(String dimensionName) {

		int dimensionId = getDimensionId(dimensionName);

		if (dimensionName.equals("overworld") || dimensionName.equals("nether") || dimensionName.equals("end")) {
			return ("Cannot delete " + dimensionName);
		}

		if (!getDimensionNames().contains(dimensionName)) {
			return ("Unknown dimension: " + dimensionName);
		}

		if (MinecraftServer.getServer().worldServerForDimension(dimensionId).playerEntities.size() != 0) {
			return ("Dimension not empty, unable to delete.");
		}

		World world = DimensionManager.getWorld(dimensionId);

		String savePath = world.getSaveHandler().getWorldDirectory() + "/" + world.provider.getSaveFolder();

		DimensionManager.setWorld(dimensionId, null);

		if (DimensionManager.isDimensionRegistered(dimensionId)) {
			DimensionManager.unregisterProviderType(dimensionId);
			DimensionManager.unregisterDimension(dimensionId);
		}

		dimensionNames.remove(dimensionId);
		dimensionWorldInfos.remove(dimensionId);
		dimensionIds.remove(Integer.valueOf(dimensionId));

		try {
			FileUtils.deleteDirectory(new File(savePath));
		} catch (IOException e) {
			return ("File I/O error deleting dimension " + dimensionName);
		}

		if (!MinecraftServer.getServer().isSinglePlayer()) {
			ExtraDimensions.network.sendToAll(new DeleteDimensionIdMessage(dimensionId));
		}

		return ("Deleted dimension " + dimensionName);
	}

	public static void deleteDimensionClientOnly(int dimensionId) {
		if (DimensionManager.isDimensionRegistered(dimensionId)) {
			DimensionManager.unregisterProviderType(dimensionId);
			DimensionManager.unregisterDimension(dimensionId);
		}
		dimensionNames.remove(dimensionId);
		dimensionIds.remove(Integer.valueOf(dimensionId));
	}

	public static boolean dimensionNameExists(String dimensionName) {
		return dimensionNames.containsValue(dimensionName);
	}

	public static int getDimensionId(String dimensionName) {
		for (int i : dimensionNames.keySet()) {
			if (dimensionNames.get(i).equals(dimensionName)) {
				return i;
			}
		}
		return 0;
	}

	public static List<Integer> getDimensionIds() {
		return dimensionIds;
	}

	public static String getDimensionName(int dimensionId) {
		return dimensionNames.get(dimensionId);
	}

	public static Collection<String> getDimensionNames() {
		return dimensionNames.values();
	}

	public static WorldInfo getDimensionWorldInfo(int dimensionId) {
		return dimensionWorldInfos.get(dimensionId);
	}

	public static boolean isValidWorldType(String worldType) {
		return ExtraDimensionsUtil.validWorldTypes().contains(worldType);
	}

	public static void registerExistingDimensions() {

		dimensionNames.put(-1, "nether");
		dimensionNames.put(0, "overworld");
		dimensionNames.put(1, "end");

		File extraDimensionsDir = new File(DimensionManager.getCurrentSaveRootDirectory() + "/ExtraDimensions");

		if (extraDimensionsDir.exists() && extraDimensionsDir.isDirectory()) {

			for (final File fileEntry : extraDimensionsDir.listFiles()) {
				if (fileEntry.isDirectory()) {
					String dimensionIdString = fileEntry.getName();
					int dimensionId = Integer.parseInt(dimensionIdString);

					File file = new File(DimensionManager.getCurrentSaveRootDirectory(),
							"/ExtraDimensions/" + dimensionId + "/level.dat");

					if (file.exists()) {
						try {
							NBTTagCompound fileData = CompressedStreamTools.readCompressed(new FileInputStream(file));
							NBTTagCompound data = fileData.getCompoundTag("Data");
							WorldInfo worldInfo = new WorldInfo(data);

							String dimensionName = worldInfo.getWorldName();

							DimensionManager.registerProviderType(dimensionId, ExtraDimensionsWorldProvider.class,
									false);
							DimensionManager.registerDimension(dimensionId, dimensionId);

							dimensionIds.add(dimensionId);
							dimensionNames.put(dimensionId, dimensionName);
							dimensionWorldInfos.put(dimensionId, worldInfo);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static String renameDimension(String dimensionName, String newName) {

		int dimensionId = getDimensionId(dimensionName);

		if (dimensionName.equals("overworld") || dimensionName.equals("nether") || dimensionName.equals("end")) {
			return ("Cannot rename " + dimensionName);
		}

		if (!getDimensionNames().contains(dimensionName)) {
			return ("Unknown dimension: " + dimensionName);
		}

		File file = new File(DimensionManager.getCurrentSaveRootDirectory(),
				"/ExtraDimensions/" + dimensionId + "/level.dat");

		if (!file.exists()) {
			return ("No level.dat for " + dimensionName);
		}

		try {

			NBTTagCompound fileData = CompressedStreamTools.readCompressed(new FileInputStream(file));
			NBTTagCompound data = fileData.getCompoundTag("Data");

			data.setString("LevelName", newName);

			WorldInfo worldInfo = new WorldInfo(data);
			dimensionWorldInfos.put(dimensionId, worldInfo);

			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setTag("Data", data);

			FileOutputStream fileoutputstream = new FileOutputStream(file);
			CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
			fileoutputstream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return ("Unknown error renaming " + dimensionName);
		}

		dimensionNames.put(dimensionId, newName);
		MinecraftServer.getServer().worldServerForDimension(dimensionId).getWorldInfo().setWorldName(newName);

		return ("Renamed " + dimensionName + " to " + newName);
	}

	public static void updateClientDimensionIds(List<Integer> currentDimensionIds) {

		List<Integer> dimensionIdsToAdd = new ArrayList();
		List<Integer> dimensionIdsToRemove = new ArrayList();

		Collections.copy(dimensionIdsToAdd, currentDimensionIds);
		Collections.copy(dimensionIdsToRemove, getDimensionIds());
		dimensionIdsToAdd.removeAll(getDimensionIds());
		dimensionIdsToRemove.removeAll(currentDimensionIds);

		for (Integer dimensionId : dimensionIdsToAdd) {
			DimensionManager.registerProviderType(dimensionId, ExtraDimensionsWorldProvider.class, false);
			DimensionManager.registerDimension(dimensionId, dimensionId);
			dimensionIds.add(dimensionId);
		}

		for (Integer dimensionId : dimensionIdsToRemove) {
			if (DimensionManager.isDimensionRegistered(dimensionId)) {
				DimensionManager.unregisterDimension(dimensionId);
			}
			dimensionNames.remove(dimensionId);
			dimensionIds.remove(Integer.valueOf(dimensionId));
		}
	}

	public static List<String> validWorldTypes() {
		return Arrays.asList("flat", "default", "largeBiomes", "amplified", "default_1_1", "custom");

	}
}