package net.woogie.extraDimensions.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class ExtraDimensionsTeleporter extends Teleporter {

	private final WorldServer worldServerInstance;

	public ExtraDimensionsTeleporter(WorldServer worldServer) {
		super(worldServer);
		this.worldServerInstance = worldServer;
	}

	@Override
	public boolean makePortal(Entity p_85188_1_) {
		return true;
	}

	@Override
	public void placeInPortal(Entity entityIn, float p_180620_2_) {

		int i = MathHelper.floor_double(entityIn.posX);
		int j = MathHelper.floor_double(entityIn.posY) - 1;
		int k = MathHelper.floor_double(entityIn.posZ);
		byte b0 = 1;
		byte b1 = 0;

		entityIn.setLocationAndAngles(i, j, k, entityIn.rotationYaw, 0.0F);
		entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;

	}
}
