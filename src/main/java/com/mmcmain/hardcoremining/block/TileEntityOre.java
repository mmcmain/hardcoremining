package com.mmcmain.hardcoremining.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntityOre extends TileEntity
{
	private static final int DEFAULT_ADDL_DROPS = 3;
	private static final int DEFAULT_MIN_DROPS = 3;

	private static final String NBT_TAG = "oreCounter";

	Random random;
	
	
	private int oreCounter = -1;


	public TileEntityOre()
	{
		random = new Random();
		oreCounter = random.nextInt(DEFAULT_ADDL_DROPS) + DEFAULT_MIN_DROPS;
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
        if (depth >= 128 && depth < 255)
            oreCounter *= (random.nextInt(3) + 1);
        else if (depth > 50 && depth < 128)
            oreCounter *= (random.nextInt(2) + 1);
        else if (depth > 24 && depth <= 50)
            oreCounter *= (random.nextInt(5) + 1);
        else if ( depth > 0 && depth <= 24)
            oreCounter *= (random.nextInt(22) + 3);

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
