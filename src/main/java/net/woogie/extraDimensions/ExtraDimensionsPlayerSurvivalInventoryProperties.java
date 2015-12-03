package net.woogie.extraDimensions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

public class ExtraDimensionsPlayerSurvivalInventoryProperties implements IExtendedEntityProperties {

	private static final String identifier = "ExtraDimensionsSurvivalInventory";
	public static ExtraDimensionsPlayerSurvivalInventoryProperties get(EntityPlayer player) {
		return (ExtraDimensionsPlayerSurvivalInventoryProperties) player.getExtendedProperties(identifier);
	}
	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(identifier, new ExtraDimensionsPlayerSurvivalInventoryProperties(player));
	}

	private final EntityPlayer player;

	private InventoryPlayer survivalInventory;

	public ExtraDimensionsPlayerSurvivalInventoryProperties(EntityPlayer player) {
		this.player = player;
		this.survivalInventory = new InventoryPlayer(player);
	}

	public InventoryPlayer getSurvivalInventory() {
		return this.survivalInventory;
	}

	@Override
	public void init(Entity entity, World world) {

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.survivalInventory.mainInventory = new ItemStack[36];
		this.survivalInventory.armorInventory = new ItemStack[4];

		NBTBase tagListBase = compound.getTagList(identifier, Constants.NBT.TAG_COMPOUND);
		NBTTagList tagList = (NBTTagList) tagListBase;

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound itemStackNBT = tagList.getCompoundTagAt(i);
			int slot = itemStackNBT.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(itemStackNBT);

			if (itemstack != null) {
				if (slot >= 0 && slot < this.survivalInventory.mainInventory.length) {
					this.survivalInventory.mainInventory[slot] = itemstack;
				}

				if (slot >= 100 && slot < this.survivalInventory.armorInventory.length + 100) {
					this.survivalInventory.armorInventory[slot - 100] = itemstack;
				}
			}
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {

		int i;
		NBTTagCompound nbttagcompound;
		NBTTagList survivalInventoryNBT = new NBTTagList();

		for (i = 0; i < this.survivalInventory.mainInventory.length; ++i) {
			if (this.survivalInventory.mainInventory[i] != null) {
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				this.survivalInventory.mainInventory[i].writeToNBT(nbttagcompound);
				survivalInventoryNBT.appendTag(nbttagcompound);
			}
		}

		for (i = 0; i < this.survivalInventory.armorInventory.length; ++i) {
			if (this.survivalInventory.armorInventory[i] != null) {
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) (i + 100));
				this.survivalInventory.armorInventory[i].writeToNBT(nbttagcompound);
				survivalInventoryNBT.appendTag(nbttagcompound);
			}
		}

		compound.setTag(identifier, survivalInventoryNBT);
	}

	public void setSurvivalInventory(InventoryPlayer inventory) {
		this.survivalInventory.clear();
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);
			this.survivalInventory.setInventorySlotContents(i, (stack == null ? null : stack.copy()));
		}
	}

}
