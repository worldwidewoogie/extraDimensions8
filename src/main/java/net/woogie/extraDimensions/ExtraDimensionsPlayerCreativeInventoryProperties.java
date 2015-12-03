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

public class ExtraDimensionsPlayerCreativeInventoryProperties implements IExtendedEntityProperties {

	private static final String identifier = "ExtraDimensionsCreativeInventory";
	public static ExtraDimensionsPlayerCreativeInventoryProperties get(EntityPlayer player) {
		return (ExtraDimensionsPlayerCreativeInventoryProperties) player.getExtendedProperties(identifier);
	}
	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(identifier, new ExtraDimensionsPlayerCreativeInventoryProperties(player));
	}

	private final EntityPlayer player;

	private InventoryPlayer creativeInventory;

	public ExtraDimensionsPlayerCreativeInventoryProperties(EntityPlayer player) {
		this.player = player;
		this.creativeInventory = new InventoryPlayer(player);

	}

	public InventoryPlayer getCreativeInventory() {
		return this.creativeInventory;
	}

	@Override
	public void init(Entity entity, World world) {

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.creativeInventory.mainInventory = new ItemStack[36];
		this.creativeInventory.armorInventory = new ItemStack[4];

		NBTBase tagListBase = compound.getTagList(identifier, Constants.NBT.TAG_COMPOUND);
		NBTTagList tagList = (NBTTagList) tagListBase;

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound itemStackNBT = tagList.getCompoundTagAt(i);
			int slot = itemStackNBT.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(itemStackNBT);

			if (itemstack != null) {
				if (slot >= 0 && slot < this.creativeInventory.mainInventory.length) {
					this.creativeInventory.mainInventory[slot] = itemstack;
				}

				if (slot >= 100 && slot < this.creativeInventory.armorInventory.length + 100) {
					this.creativeInventory.armorInventory[slot - 100] = itemstack;
				}
			}
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {

		int i;
		NBTTagCompound nbttagcompound;
		NBTTagList creativeInventoryNBT = new NBTTagList();

		for (i = 0; i < this.creativeInventory.mainInventory.length; ++i) {
			if (this.creativeInventory.mainInventory[i] != null) {
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				this.creativeInventory.mainInventory[i].writeToNBT(nbttagcompound);
				creativeInventoryNBT.appendTag(nbttagcompound);
			}
		}

		for (i = 0; i < this.creativeInventory.armorInventory.length; ++i) {
			if (this.creativeInventory.armorInventory[i] != null) {
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) (i + 100));
				this.creativeInventory.armorInventory[i].writeToNBT(nbttagcompound);
				creativeInventoryNBT.appendTag(nbttagcompound);
			}
		}

		compound.setTag(identifier, creativeInventoryNBT);
	}

	public void setCreativeInventory(InventoryPlayer inventory) {
		this.creativeInventory.clear();
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);
			this.creativeInventory.setInventorySlotContents(i, (stack == null ? null : stack.copy()));
		}
	}

}
