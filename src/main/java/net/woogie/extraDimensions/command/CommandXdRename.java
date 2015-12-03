package net.woogie.extraDimensions.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.woogie.extraDimensions.ExtraDimensionsUtil;

public class CommandXdRename extends CommandBase {

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
	public boolean canCommandSenderUse(ICommandSender agent) {

		try {
			if (MinecraftServer.getServer().isSinglePlayer() || MinecraftServer.getServer().getConfigurationManager()
					.canSendCommands(getPlayer(agent, agent.getName()).getGameProfile())) {
				return true;
			}
		} catch (PlayerNotFoundException e) {
		}
		return false;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws WrongUsageException {

		if (args.length != 2) {
			throw new WrongUsageException("Usage: " + getCommandUsage(sender));
		}
		sender.addChatMessage(new ChatComponentText(ExtraDimensionsUtil.renameDimension(args[0], args[1])));
	}

	@Override
	public String getCommandUsage(ICommandSender agent) {
		return "/" + getName() + " <Dimension Name or ID> <New Dimension Name>";
	}

	@Override
	public String getName() {
		return "xdrename";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return MinecraftServer.getServer().getOpPermissionLevel();
	}
}