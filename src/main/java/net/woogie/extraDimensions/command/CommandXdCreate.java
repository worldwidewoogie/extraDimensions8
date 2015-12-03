package net.woogie.extraDimensions.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.woogie.extraDimensions.ExtraDimensionsUtil;

public class CommandXdCreate extends CommandBase {

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {

		ArrayList returnList = new ArrayList<String>();

		if (args.length == 2) {
			for (String gameType : new ArrayList<String>(Arrays.asList("creative", "survival"))) {
				if (gameType.startsWith(args[1])) {
					returnList.add(gameType);
				}
			}
		}

		if (args.length == 3) {
			for (String worldType : ExtraDimensionsUtil.validWorldTypes()) {
				if (worldType.startsWith(args[2])) {
					returnList.add(worldType);
				}
			}
		}

		if (args.length == 4) {

			ArrayList<String> optionsList = null;

			if (args[2].equals("flat") || args[2].equals("custom")) {
				if (args[2].equals("flat")) {
					optionsList = new ArrayList<String>(
							Arrays.asList("classicflat", "overworld", "dessert", "tunnelersdream", "snowykingdom",
									"redstoneready", "waterworld", "bottomlesspit", "thevoid"));
				} else if (args[2].equals("custom")) {
					optionsList = new ArrayList<String>(Arrays.asList("default", "isleland", "mountainmadness",
							"caveofchaos", "waterworld", "caversdelight", "drought", "goodluck"));
				}

				for (String options : optionsList) {
					if (options.startsWith(args[3])) {
						returnList.add(options);
					}
				}
			}
		}

		return returnList;
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		// if a player sent the command, they must be an op. The only other
		// thing that could send the command is a command block (unless I'm
		// missing something) so we return true if not a player.
		if (sender instanceof EntityPlayer) {
			if (MinecraftServer.getServer().isSinglePlayer() || MinecraftServer.getServer().getConfigurationManager()
					.canSendCommands(((EntityPlayerMP) sender).getGameProfile())) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws WrongUsageException {

		if (args.length > 0 && args.length < 6) {

			String dimensionName = args[0];
			String gameType = "creative";
			String worldType = "largeBiomes";
			String options = "";
			Long seed = 0L;

			if (args.length > 1) {
				if (args[1].equals("creative") || args[1].equals("survival")) {
					gameType = args[1];
				} else {
					sender.addChatMessage(new ChatComponentText("Invalid Game Type"));
					return;
				}
			}

			if (args.length > 2) {
				worldType = args[2];
			}

			if (args.length > 3) {
				if (!args[3].equals("-")) {
					options = args[3];
				}
			}

			if (args.length > 4) {
				try {
					seed = Long.parseLong(args[4], 10);
				} catch (NumberFormatException e) {
					sender.addChatMessage(new ChatComponentText("Invalid Seed"));
					return;
				}
			}

			if (worldType.equals("flat")) {
				if (options.equals("classicflat") || options.equals("")) {
					options = "3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;village";
				} else if (options.equals("overworld")) {
					options = "3;minecraft:bedrock,59*minecraft:stone,3*minecraft:dirt,minecraft:grass;1;village,mineshaft,stronghold,biome_1,dungeon,decoration,lake,lava_lake";
				} else if (options.equals("dessert")) {
					options = "3;minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone,8*minecraft:sand;2;village,mineshaft,stronghold,biome_1,dungeon,decoration";
				} else if (options.equals("tunnelersdream")) {
					options = "3;minecraft:bedrock,230*minecraft:stone,5*minecraft:dirt,minecraft:grass;3;mineshaft,stronghold,biome_1,dungeon,decoration";
				} else if (options.equals("snowykingdom")) {
					options = "3;minecraft:bedrock,59*minecraft:stone,3*minecraft:dirt,minecraft:grass,minecraft:snow_layer;12;village,biome_1";
				} else if (options.equals("redstoneready")) {
					options = "3;minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone;2;";
				} else if (options.equals("waterworld")) {
					options = "3;minecraft:bedrock,5*minecraft:stone,5*minecraft:dirt,5*minecraft:sand,90*minecraft:water;1;village,biome_1";
				} else if (options.equals("bottomlesspit")) {
					options = "3;2*minecraft:cobblestone,3*minecraft:dirt,minecraft:grass;1;village,biome_1";
				} else if (options.equals("thevoid")) {
					options = "3;minecraft:air;127;decoration";
				}
			}

			if (worldType.equals("custom")) {
				if (options.equals("default") || options.equals("")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":63,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":684,\"heightScale\":684,\"mainNoiseScaleX\":80,\"mainNoiseScaleY\":160,\"mainNoiseScaleZ\":80,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":1,\"biomeDepthOffset\":0,\"biomeScaleWeight\":1,\"biomeScaleOffset\":1,\"lowerLimitScale\":512,\"upperLimitScale\":512,\"baseSize\":8.5,\"stretchY\":12,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("isleland")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":63,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":3000,\"heightScale\":6000,\"mainNoiseScaleX\":80,\"mainNoiseScaleY\":160,\"mainNoiseScaleZ\":80,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":1,\"biomeDepthOffset\":0,\"biomeScaleWeight\":1,\"biomeScaleOffset\":1,\"lowerLimitScale\":512,\"upperLimitScale\":250,\"baseSize\":8.5,\"stretchY\":10,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("mountainmadness")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":63,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":738,\"heightScale\":158,\"mainNoiseScaleX\":1356,\"mainNoiseScaleY\":746,\"mainNoiseScaleZ\":1183,\"depthNoiseScaleX\":375,\"depthNoiseScaleZ\":289,\"depthNoiseScaleExponent\":1.21,\"biomeDepthWeight\":1.8,\"biomeDepthOffset\":3.5,\"biomeScaleWeight\":1,\"biomeScaleOffset\":2.5,\"lowerLimitScale\":1254,\"upperLimitScale\":801,\"baseSize\":1.9,\"stretchY\":1.71,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("caveofchaos")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":6,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":684,\"heightScale\":684,\"mainNoiseScaleX\":80,\"mainNoiseScaleY\":160,\"mainNoiseScaleZ\":80,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":1,\"biomeDepthOffset\":0,\"biomeScaleWeight\":1,\"biomeScaleOffset\":1,\"lowerLimitScale\":64,\"upperLimitScale\":2,\"baseSize\":8.5,\"stretchY\":12,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("waterworld")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":255,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":684,\"heightScale\":684,\"mainNoiseScaleX\":5000,\"mainNoiseScaleY\":1000,\"mainNoiseScaleZ\":5000,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":2,\"biomeDepthOffset\":0.5,\"biomeScaleWeight\":2,\"biomeScaleOffset\":1,\"lowerLimitScale\":512,\"upperLimitScale\":512,\"baseSize\":8.5,\"stretchY\":8,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("caversdelight")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":63,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":684,\"heightScale\":684,\"mainNoiseScaleX\":5000,\"mainNoiseScaleY\":1000,\"mainNoiseScaleZ\":5000,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":2,\"biomeDepthOffset\":1,\"biomeScaleWeight\":4,\"biomeScaleOffset\":1,\"lowerLimitScale\":512,\"upperLimitScale\":512,\"baseSize\":8.5,\"stretchY\":5,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("drought")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":false,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":20,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":684,\"heightScale\":684,\"mainNoiseScaleX\":1000,\"mainNoiseScaleY\":3000,\"mainNoiseScaleZ\":1000,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":1,\"biomeDepthOffset\":0,\"biomeScaleWeight\":1,\"biomeScaleOffset\":1,\"lowerLimitScale\":512,\"upperLimitScale\":512,\"baseSize\":8.5,\"stretchY\":10,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				} else if (options.equals("goodluck")) {
					options = "{\"useCaves\":true,\"useStrongholds\":true,\"useVillages\":true,\"useMineShafts\":true,\"useTemples\":true,\"useRavines\":true,\"useMonuments\":true,\"useLavaOceans\":true,\"useWaterLakes\":true,\"useLavaLakes\":true,\"useDungeons\":true,\"seaLevel\":40,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"waterLakeChance\":4,\"lavaLakeChance\":80,\"dungeonChance\":8,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":255,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":255,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisMinHeight\":0,\"lapisMaxHeight\":32,\"coordinateScale\":684,\"heightScale\":684,\"mainNoiseScaleX\":80,\"mainNoiseScaleY\":160,\"mainNoiseScaleZ\":80,\"depthNoiseScaleX\":200,\"depthNoiseScaleZ\":200,\"depthNoiseScaleExponent\":0.5,\"biomeDepthWeight\":1,\"biomeDepthOffset\":0,\"biomeScaleWeight\":1,\"biomeScaleOffset\":1,\"lowerLimitScale\":512,\"upperLimitScale\":512,\"baseSize\":8.5,\"stretchY\":12,\"lapisCenterHeight\":16,\"lapisSpread\":16}";
				}
			}

			sender.addChatMessage(new ChatComponentText(
					ExtraDimensionsUtil.createDimension(dimensionName, gameType, worldType, seed, options)));
		} else {
			throw new WrongUsageException("Usage: " + getCommandUsage(sender));
		}
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getName() + " <Dimension Name> <Game Type> <World Type> <World Options> <World Seed>";
	}

	@Override
	public String getName() {
		return "xdcreate";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return MinecraftServer.getServer().getOpPermissionLevel();
	}
}