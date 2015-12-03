package net.woogie.extraDimensions.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings.GameType;
import net.woogie.extraDimensions.ExtraDimensionsPlayerCreativeInventoryProperties;
import net.woogie.extraDimensions.ExtraDimensionsPlayerSurvivalInventoryProperties;
import net.woogie.extraDimensions.ExtraDimensionsUtil;
import net.woogie.extraDimensions.world.ExtraDimensionsTeleporter;

public class CommandXdTp extends CommandBase {
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {

		Collection<String> dimensionsList = ExtraDimensionsUtil.getDimensionNames();
		ArrayList returnList = new ArrayList<String>();

		if (args.length == 1) {
			for (String dimensionName : dimensionsList) {
				if (dimensionName.startsWith(args[0])) {
					returnList.add(dimensionName);
				}
			}
		}
		return returnList;
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws WrongUsageException {

		if (args.length != 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		int dim = ExtraDimensionsUtil.getDimensionId(args[0]);
		String dimensionName = ExtraDimensionsUtil.getDimensionName(dim);

		if (dimensionName == null || !dimensionName.equals(args[0])) {
			sender.addChatMessage(new ChatComponentText("Unknown dimension: " + args[0]));
			return;
		}

		if (args[0].equals("nether") || args[0].equals("end")) {
			sender.addChatMessage(new ChatComponentText("Cannot teleport to " + args[0]));
			return;
		}

		WorldServer nextWorld = MinecraftServer.getServer().worldServerForDimension(dim);

		if (nextWorld == null) {
			sender.addChatMessage(
					new ChatComponentText("Problem with " + dimensionName + " dimension, teleport canceled."));
			return;
		}

		EntityPlayerMP playerMP = (EntityPlayerMP) sender;

		if (playerMP.dimension == dim) {
			sender.addChatMessage(
					new ChatComponentText("Already in " + dimensionName + " dimension, teleport canceled."));
			return;
		}
		
		GameType startGameType;
		GameType nextGameType;

		if (playerMP.dimension == -1 || playerMP.dimension == 0 || playerMP.dimension == 1) {
			startGameType = playerMP.worldObj.getWorldInfo().getGameType();
		} else {
			startGameType = ExtraDimensionsUtil.getDimensionWorldInfo(playerMP.dimension).getGameType();
		}

		if (dim == -1 || dim == 0 || dim == 1) {
			nextGameType = playerMP.worldObj.getWorldInfo().getGameType();
		} else {
			nextGameType = ExtraDimensionsUtil.getDimensionWorldInfo(dim).getGameType();
		}

		if (startGameType == GameType.SURVIVAL) {
			ExtraDimensionsPlayerSurvivalInventoryProperties survivalInventoryHandler = ExtraDimensionsPlayerSurvivalInventoryProperties
					.get(playerMP);
			survivalInventoryHandler.setSurvivalInventory(playerMP.inventory);
		}

		if (startGameType == GameType.CREATIVE) {
			ExtraDimensionsPlayerCreativeInventoryProperties creativeInventoryHandler = ExtraDimensionsPlayerCreativeInventoryProperties
					.get(playerMP);
			creativeInventoryHandler.setCreativeInventory(playerMP.inventory);
		}

		if (nextGameType == GameType.SURVIVAL) {
			ExtraDimensionsPlayerSurvivalInventoryProperties survivalInventoryHandler = ExtraDimensionsPlayerSurvivalInventoryProperties
					.get(playerMP);
			InventoryPlayer survivalInventory = survivalInventoryHandler.getSurvivalInventory();
			playerMP.inventory.clear();
			for (int i = 0; i < survivalInventory.getSizeInventory(); ++i) {
				ItemStack stack = survivalInventory.getStackInSlot(i);
				playerMP.inventory.setInventorySlotContents(i, (stack == null ? null : stack.copy()));
			}
		}

		if (nextGameType == GameType.CREATIVE) {
			ExtraDimensionsPlayerCreativeInventoryProperties creativeInventoryHandler = ExtraDimensionsPlayerCreativeInventoryProperties
					.get(playerMP);
			InventoryPlayer creativeInventory = creativeInventoryHandler.getCreativeInventory();
			playerMP.inventory.clear();
			for (int i = 0; i < creativeInventory.getSizeInventory(); ++i) {
				ItemStack stack = creativeInventory.getStackInSlot(i);
				playerMP.inventory.setInventorySlotContents(i, (stack == null ? null : stack.copy()));
			}
		}
		
		sender.addChatMessage(
				new ChatComponentText("Teleporting to " + dimensionName + " dimension."));
		
		playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dim,
				new ExtraDimensionsTeleporter(nextWorld));

		BlockPos spawnPos = nextWorld.getSpawnPoint();

		spawnPos = nextWorld.getTopSolidOrLiquidBlock(spawnPos);

		int spawnX = spawnPos.getX();
		int spawnY = spawnPos.getY();
		int spawnZ = spawnPos.getZ();

		playerMP.playerNetServerHandler.setPlayerLocation(spawnX + 0.5D, spawnY, spawnZ + 0.5D, playerMP.rotationYaw,
				0.0F);

		playerMP.motionX = playerMP.motionY = playerMP.motionZ = 0.0D;

		playerMP.setGameType(nextGameType);

	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getName() + " [destination dimension name or ID)";
	}

	@Override
	public String getName() {
		return "xdtp";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}