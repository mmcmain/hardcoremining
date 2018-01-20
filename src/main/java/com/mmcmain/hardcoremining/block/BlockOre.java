package com.mmcmain.hardcoremining.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mmcmain.hardcoremining.general.RMLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import com.mmcmain.hardcoremining.item.ItemOreDict;
import com.mmcmain.hardcoremining.world.ModBiomeEntry;

import javax.annotation.Nullable;


public class BlockOre extends BlockBase implements ItemOreDict
{
	private TileOre tileOre;
	
	public int minY, maxY, clumpSize, veinSize;
	
	public List<ModBiomeEntry> biomeEntries;
	
	
	public BlockOre(String name)
	{
		super(name);

		biomeEntries = new ArrayList<>();
        tileOre = new TileOre(name);
        this.minY = 16;
		this.maxY = 64;
		this.clumpSize = 3;
		this.veinSize = 25;
	}


    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos blockPos, @Nullable EntityPlayer player, boolean willHarvest)
    {
        RMLog.debug("BlockOre removedByPlayer called. Converting and calling on Tile.");

        if ( !player.isCreative() )
        {
            if (!world.isRemote)
                return convertToTile(world, blockPos).removedByPlayer(tileOre.getDefaultState(), world, blockPos, player, willHarvest);
        }
        else
            return super.removedByPlayer(blockState, world, blockPos, player, willHarvest);

        return false;
    }

    private TileOre convertToTile(World world, BlockPos blockPos)
    {
        world.setBlockState(blockPos, getTileOre().getDefaultState(), 7);

        TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);
        if ( tileEntityOre != null )
            tileEntityOre.setOreCounterForDepth(blockPos.getY());

        return tileOre;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if ( !worldIn.isRemote && hand == EnumHand.MAIN_HAND )
        {
            convertToTile(worldIn, pos);
            getTileOre().checkOreSamplerMsg(worldIn, pos, playerIn, hand, heldItem);
        }


        return super.onBlockActivated(worldIn, pos, state, playerIn,hand, heldItem, side, hitX, hitY, hitZ);
    }


    @Override
    public void onBlockExploded(World world, BlockPos blockPos, Explosion explosion)
    {
        RMLog.debug("BlockOre onBlockExploded called. Converting and calling on Tile.");

        if (!world.isRemote)
        {
            if ( blockState.getBlock() instanceof BlockOre )
                convertToTile(world, blockPos).onBlockExploded(world, blockPos, explosion);
        }
    }

	@Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState blockState, float chance, int fortune)
    {
        if ( blockState.getBlock() instanceof BlockOre )
            RMLog.warn("BlockOre DropBlock called. Should not be possible.");
        else
            super.dropBlockAsItemWithChance(world, blockPos, blockState, chance, fortune);
    }

	public int getVeinSize(Random rand)
	{
	    return veinSize  + rand.nextInt(35);
	}

	public int getClumpSize(Random rand)
	{
	    return clumpSize - rand.nextInt(4);
	}

    @Override
    public boolean canDropFromExplosion(final Explosion explosion)
    {
        return false;
    }


    @Override
	public void setHarvestLevel(String toolClass, int level)
	{
		super.setHarvestLevel(toolClass, level);
		if ( this.tileOre != null )
			this.tileOre.setHarvestLevel(toolClass, level);
	}
	
	@Override
	public BlockOre setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public void initOreDict()
	{
		OreDictionary.registerOre(name, this);
	}
	

	public TileOre getTileOre()
	{
		return tileOre;
	}
	
}
