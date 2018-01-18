package com.mmcmain.hardcoremining.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mmcmain.hardcoremining.general.RMLog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import com.mmcmain.hardcoremining.item.ItemOreDict;
import com.mmcmain.hardcoremining.world.ModBiomeEntry;

import javax.annotation.Nullable;


public class BlockOre extends BlockBase implements ItemOreDict
{
	protected TileOre tileOre;
	
	public int minY, maxY, clumpSize, veinSize;
	
	public List<ModBiomeEntry> biomeEntries = new ArrayList();
	
	
	public BlockOre(String name)
	{
		super(name);

        tileOre = new TileOre(name);
        this.minY = 16;
		this.maxY = 64;
		this.clumpSize = 5;
		this.veinSize = 25;
	}




    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (state.getBlock() instanceof BlockOre )
		{
			ToolChecker toolChecker = new ToolChecker(worldIn, playerIn, state);

			if (toolChecker.getPotentialOreCount(TileEntityOre.PLAYER_DROP_PRODUCTION) < 1 &&
                    toolChecker.getRequiredTool() != null)
            {
                if(toolChecker.shouldWarn())
                    worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.NEUTRAL, .5F, .5F);
            }
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}


    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos blockPos, @Nullable EntityPlayer player, boolean willHarvest)
    {
        RMLog.debug("BlockOre removedByPlayer called. Converting and calling on Tile.");

        if (!world.isRemote && !player.isCreative())
        {
            if ( blockState.getBlock() instanceof BlockOre )
            {
                BlockOre blockOre = (BlockOre) blockState.getBlock();
                TileOre tileOre = blockOre.getTileOre();

                world.setBlockState(blockPos, tileOre.getDefaultState());
                return tileOre.removedByPlayer(tileOre.getDefaultState(), world, blockPos, player, willHarvest);
            }
        }

        return super.removedByPlayer(blockState, world, blockPos, player, willHarvest);

/*

        IBlockState convertedBlock = TileHelper.convertOreToTile(world, blockPos);
        if (convertedBlock != null && !willHarvest)
            convertedBlock.getBlock().removedByPlayer(blockState, world, blockPos, player, willHarvest);
        else
            return blockState.getBlock().removedByPlayer(blockState, world, blockPos, player, willHarvest);

*/

    }


    @Override
    public void onBlockExploded(World world, BlockPos blockPos, Explosion explosion)
    {
        RMLog.debug("BlockOre onBlockExploded called. Converting and calling on Tile.");

        if (!world.isRemote)
        {
            if ( blockState.getBlock() instanceof BlockOre )
            {
                BlockOre blockOre = (BlockOre) blockState.getBlock();
                TileOre tileOre = blockOre.getTileOre();

                world.setBlockState(blockPos, tileOre.getDefaultState());
                tileOre.onBlockExploded(world, blockPos, explosion);
            }
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
		if (rand.nextBoolean())
			return this.veinSize + rand.nextInt(this.veinSize)/2;
		else
			return this.veinSize - rand.nextInt(this.veinSize)/2;
	}

	public int getClumpSize(Random rand)
	{
		if (rand.nextBoolean())
			return this.clumpSize + rand.nextInt(this.clumpSize)/2;
		else
			return this.clumpSize - rand.nextInt(this.clumpSize)/2;
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
