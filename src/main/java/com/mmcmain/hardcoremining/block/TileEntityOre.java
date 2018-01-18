package com.mmcmain.hardcoremining.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.mmcmain.hardcoremining.item.ModItems;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityOre extends TileEntity
{
	public static final int MAX_DROPS = 200;
	public static final int EXPLOSION_DROP_PRODUCTION = 5;
	public static final int EXPLOSION_MIN_DECREMENT = 40;
	public static final int DECREMENT_VARIANCE = 15;
	public static final int PLAYER_DROP_PRODUCTION = 3;
	public static final int UNKNOWN_DROP_PRODUCTION = 1;
	public static final int PLAYER_MIN_DECREMENT = 25;
	public static final String NBT_TAG = "oreCounter";
	
	
	private int oreCounter = this.MAX_DROPS;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger(this.NBT_TAG, oreCounter);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		oreCounter = compound.getInteger(this.NBT_TAG);
		super.readFromNBT(compound);
	}
	

	public boolean hasOre()
	{
		return oreCounter > 0;
	}
	
	public int getCurrentOreCount()
	{
		return oreCounter;
	}
	
	public int reduceOre(int amount)
	{
		int oresDestroyed = amount + getWorld().rand.nextInt(this.DECREMENT_VARIANCE);
		oreCounter -= oresDestroyed;
		markDirty();
		return oreCounter;
	}
	
}
