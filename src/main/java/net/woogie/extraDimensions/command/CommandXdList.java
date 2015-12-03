package net.woogie.extraDimensions.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.woogie.extraDimensions.ExtraDimensionsUtil;

public class CommandXdList extends CommandBase {

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws WrongUsageException {
		if (args.length > 1) {
			throw new WrongUsageException("Usage: " + getCommandUsage(sender), new Object[0]);
		}

		World world = sender.getEntityWorld();

		sender.addChatMessage(new ChatComponentText("Existing Dimensions:"));

		for (int i : ExtraDimensionsUtil.getDimensionIds()) {
			String currentFlag = "";
			if (world.provider.getDimensionId() == i) {
				currentFlag = " *";
			}

			String gameType;
			if (i == -1 || i == 0 || i == 1) {
				gameType = sender.getEntityWorld().getWorldInfo().getGameType().getName();
			} else {
				gameType = ExtraDimensionsUtil.getDimensionWorldInfo(i).getGameType().getName();
			}

			sender.addChatMessage(new ChatComponentText(
					"     " + ExtraDimensionsUtil.getDimensionName(i) + " (" + gameType + ")" + currentFlag));
		}
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getName();
	}

	@Override
	public String getName() {
		return "xdlist";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
