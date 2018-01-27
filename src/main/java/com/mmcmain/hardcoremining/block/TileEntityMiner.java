package com.mmcmain.hardcoremining.block;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityMiner extends TileEntity implements IEnergyStorage, IEnergyReceiver, ITickable
{
    public final static int DEF_MAX_RECEIVE = 20;
    public final static int DEF_MAX_EXTRACT = 20;
    public final static int DEF_CAPACITY = 100000;

    public final static int ENERGY_MINING = 1000;

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    private boolean isRunning;
    private int tickCounter = 0;


    public TileEntityMiner()
    {
        this(DEF_CAPACITY, DEF_MAX_EXTRACT, DEF_MAX_RECEIVE);
    }

    public TileEntityMiner(int capacity, int maxExtract, int maxReceive)
    {
        energy = capacity;
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        this.isRunning = false;
        markDirty();

    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        compound.setBoolean("isRunning", isRunning);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        isRunning = compound.getBoolean("isRunning");
        super.readFromNBT(compound);
    }


    @Override
    public int receiveEnergy(int energyIn, boolean simulate)
    {
        if ( !simulate )
        {
            if ( energy <= capacity - energyIn )
            {
                energyIn = Math.max(energyIn, maxReceive);
                energy += energyIn;
                markDirty();
            }
            else
                energyIn = 0;
        }

        return energyIn;
    }

    @Override
    public int extractEnergy(int energyOut, boolean simulate)
    {
        if ( !simulate )
        {
            if ( energyOut <= energy )
            {
                energyOut = Math.max(energyOut, maxExtract);
                energy -= energyOut;
                markDirty();
            }
            else
                energyOut = 0;
        }

        return energyOut;
    }

    public boolean spendMiningRF(boolean simulate)
    {
        boolean sufficientEnergy = false;

        if (!simulate)
        {
            if ( energy >= ENERGY_MINING )
            {
                energy -= ENERGY_MINING;
                sufficientEnergy = true;
                markDirty();
            }
        }

        return sufficientEnergy;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void toggleRunning()
    {
        isRunning = !isRunning;
        markDirty();
    }


    @Override
    public void update()
    {
        World world = getWorld();
        if ( !world.isRemote && isRunning )
        {
            if ( tickCounter++ > 20 )
            {
                tickCounter = 0;
                TileMiner tileMiner = (TileMiner) getBlockType();
                tileMiner.updateTick(world, pos, tileMiner.getDefaultState(), world.rand);
            }
        }
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
    {
        if ( energy + maxReceive <= capacity )
        {
            if ( !simulate )
                energy += maxReceive;
            markDirty();
        }
        else
            maxReceive = 0;

        return maxReceive;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
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



}
