package com.mmcmain.hardcoremining.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntityOre extends TileEntity
{
	private static final int DEFAULT_ADDL_DROPS = 160;
	private static final int DEFAULT_MIN_DROPS = 40;

	private static final String NBT_TAG = "oreCounter";

	Random random;
	
	
	private int oreCounter = -1;


	public TileEntityOre()
	{
		random = new Random();
		oreCounter = getDefaultOreCounter(random);
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger(NBT_TAG, oreCounter);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		oreCounter = compound.getInteger(NBT_TAG);
		super.readFromNBT(compound);
	}

	public void setOreCounterForDepth(int depth)
    {
    	oreCounter += additionalOreCounterForDepth(random, depth);
    }

    public static int estimateOreCounterForDepth(Random random, int depth)
    {
        return getDefaultOreCounter(random) + additionalOreCounterForDepth(random, depth);
    }

    public static int getDefaultOreCounter(Random random)
    {
        return random.nextInt(DEFAULT_ADDL_DROPS) + DEFAULT_MIN_DROPS;
    }

    public static int additionalOreCounterForDepth(Random random, int depth)
	{
		int oreCounter = 0;

		if (depth >= 128 && depth < 255)
			oreCounter += (random.nextInt(DEFAULT_ADDL_DROPS/4));
		else if (depth > 24 && depth <= 50)
			oreCounter += (random.nextInt(DEFAULT_ADDL_DROPS/2));
		else if ( depth > 0 && depth <= 24)
			oreCounter += (random.nextInt(DEFAULT_ADDL_DROPS));

		return oreCounter;
	}

	public void setOreCounter(int amount)
	{
		oreCounter = amount;
		markDirty();
	}

	public boolean hasOre()
	{
		return oreCounter > 0;
	}
	
	public int getOreCounter()
	{
		return oreCounter;
	}

	public int reduceOre()
	{
		return reduceOre(1);
	}

	public int reduceOre(int amount)
	{
		oreCounter -= amount;
		markDirty();
		return oreCounter;
	}
	
}
