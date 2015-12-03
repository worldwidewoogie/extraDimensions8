package net.woogie.extraDimensions.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.woogie.extraDimensions.ExtraDimensionsUtil;

public class ExtraDimensionsWorldProvider extends WorldProvider {

	@SideOnly(Side.SERVER)
	@Override
	public IChunkProvider createChunkGenerator() {
		WorldInfo worldInfo = ExtraDimensionsUtil.getDimensionWorldInfo(this.dimensionId);
		return worldInfo.getTerrainType().getChunkGenerator(worldObj, worldInfo.getGeneratorOptions());
	}

	@Override
	public String getDepartMessage() {
		return "Leaving " + this.getDimensionName() + " dimension...";
	}

	@Override
	public String getDimensionName() {
		return ExtraDimensionsUtil.getDimensionName(this.dimensionId);
	}

	@Override
	public String getInternalNameSuffix() {
		return "ExtraDimensions";
	}

	@Override
	public int getRespawnDimension(net.minecraft.entity.player.EntityPlayerMP player) {
		return this.dimensionId;
	}

	@Override
	public String getSaveFolder() {
		return "ExtraDimensions/" + this.dimensionId;
	}

	@SideOnly(Side.SERVER)
	@Override
	public long getSeed() {
		WorldInfo worldInfo = ExtraDimensionsUtil.getDimensionWorldInfo(this.dimensionId);
		return worldInfo.getSeed();
	}

	@Override
	public String getWelcomeMessage() {
		return "Entering " + this.getDimensionName() + " dimension...";
	}

	@SideOnly(Side.SERVER)
	@Override
	protected void registerWorldChunkManager() {
		WorldInfo worldInfo = ExtraDimensionsUtil.getDimensionWorldInfo(this.dimensionId);
		this.worldChunkMgr = worldInfo.getTerrainType().getChunkManager(worldObj);
	}

	@Override
	public void setDimension(int dim) {
		this.dimensionId = dim;
		super.setDimension(dim);
	}
}