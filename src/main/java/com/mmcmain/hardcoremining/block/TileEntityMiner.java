package com.mmcmain.hardcoremining.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityMiner extends TileEntity implements IEnergyStorage
{
    public final static int DEF_MAX_RECEIVE = 20;
    public final static int DEF_MAX_EXTRACT = 20;
    public final static int DEF_CAPACITY = 100000;

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public TileEntityMiner()
    {
        this(DEF_CAPACITY, DEF_MAX_EXTRACT, DEF_MAX_RECEIVE);
    }

    public TileEntityMiner(int capacity, int maxExtract, int maxReceive)
    {
        energy = 0;
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        markDirty();
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        super.readFromNBT(compound);
    }


    @Override
    public int receiveEnergy(int energyIn, boolean simulate)
    {
        if ( !simulate )
        {
            energyIn = Math.max(energyIn, maxReceive);
            energy += energyIn;
            markDirty();
        }

        return energyIn;
    }

    @Override
    public int extractEnergy(int energyOut, boolean simulate)
    {
        if ( !simulate )
        {
            energyOut = Math.max(energyOut, maxExtract);
            energy -= energyOut;
            markDirty();
        }

        return energyOut;
    }

    @Override
    public int getEnergyStored()
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return capacity;
    }

    @Override
    public boolean canExtract()
    {
        return true;
    }

    @Override
    public boolean canReceive()
    {
        return true;
    }
}
