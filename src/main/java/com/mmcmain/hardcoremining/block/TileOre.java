package com.mmcmain.hardcoremining.block;


import javax.annotation.Nullable;

import com.mmcmain.hardcoremining.item.ItemTileOreDrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.mmcmain.hardcoremining.general.RMLog;

import java.util.ArrayList;
import java.util.List;


public class TileOre extends BlockTileEntity<TileEntityOre>
{
	private List<ItemTileOreDrop> dropProducer;
	public static int TOOL_DAMAGE = 5;

	
	
	public TileOre(String oreName)
	{
		super(oreName + "Tile");

		this.dropProducer = new ArrayList();
	}

	public void addDropProducer(ItemTileOreDrop itemTileOreDrop)
	{
		RMLog.info("Setting drop producer to " + itemTileOreDrop.getItem().getUnlocalizedName() + ".");
		this.dropProducer.add(itemTileOreDrop);


    }
	
	public ItemTileOreDrop getDropProducer(int index)
	{
		return ((ItemTileOreDrop) this.dropProducer.get(index));
	}


	@Nullable
	@Override
	public TileEntityOre createTileEntity(World world, IBlockState state)
	{
		return new TileEntityOre();
	}
	
	@Override
	public Class<TileEntityOre> getTileEntityClass()
	{
		return TileEntityOre.class;
	}


	@Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState blockState)
    {
        if ( !world.isRemote && blockState.getBlock() instanceof TileOre )
            world.destroyBlock(blockPos, false);
        else
            super.onBlockDestroyedByPlayer(world, blockPos, blockState);
    }

    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos blockPos, @Nullable EntityPlayer player, boolean willHarvest)
    {
        if ( !world.isRemote && player != null && !player.isCreative())
        {
            if ( blockState.getBlock() instanceof TileOre )
            {
                TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);
                if ( tileEntityOre != null )
                {
                    tileEntityOre.reduceOre(TileEntityOre.PLAYER_MIN_DECREMENT);
                    if ( tileEntityOre.hasOre() )
                    {
                        this.harvestBlock(world, player, blockPos, blockState, tileEntityOre, null);
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
            }
        }

        return super.removedByPlayer(blockState, world, blockPos, player, willHarvest);
    }

    @Override
    public void onBlockExploded(World world, BlockPos blockPos, Explosion explosion)
    {
        if ( world.getBlockState(blockPos).getBlock() instanceof TileOre )
        {
            TileOre tileOre = (TileOre) world.getBlockState(blockPos).getBlock();
            TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);

            tileOre.dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), TileEntityOre.EXPLOSION_DROP_PRODUCTION);
            tileEntityOre.reduceOre(TileEntityOre.EXPLOSION_MIN_DECREMENT);
            if ( !tileEntityOre.hasOre() )
                world.destroyBlock(blockPos, false);
        }
    }

    @Override
    public void harvestBlock(World world, @Nullable EntityPlayer player, BlockPos blockPos, IBlockState blockState, @Nullable TileEntity tileEntity, @Nullable ItemStack itemStack)
    {
        if ( blockState.getBlock() instanceof TileOre )
        {
            TileOre tileOre = (TileOre) blockState.getBlock();
            tileOre.dropBlockAsItemWithChance(world, blockPos, blockState, 1f, TileEntityOre.PLAYER_DROP_PRODUCTION);
        }
        else
            super.harvestBlock(world, player, blockPos, blockState, tileEntity, itemStack);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            java.util.List<ItemStack> items = getDrops(worldIn, pos, state, fortune);

            for (ItemStack item : items)
            {

//                EntityPlayer player = worldIn.getClosestPlayer((float) pos.getX(), (float) pos.getY(), (float) pos.getZ(), -1f, false);
                EntityPlayer player = null;
                if ( player != null )
                {
                    spawnAsEntity(worldIn, player.getPosition(), item);
                }
                else
                {
                    spawnAsEntity(worldIn, pos, item);
                }

            }
        }

    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState blockState, int defaultFortune)
    {
        TileOre tileOre = null;
        List<ItemStack> itemStackList = new java.util.ArrayList<ItemStack>();
        int fortune;

        if ( blockState.getBlock() instanceof TileOre)
            tileOre = (TileOre) blockState.getBlock();

        int dropCount = tileOre.getDropProducerCount();
        for ( int i = 0; !((World) world).isRemote && tileOre != null && i < tileOre.getDropProducerCount(); i++ )
        {
            fortune = TileHelper.adjustedFortune((World) world, pos, getDropProducer(i), defaultFortune);
            RMLog.debug("  Adding " + fortune + " " + tileOre.getDropProducer(i).getItem().getUnlocalizedName() + ".");
            if ( fortune > 0 )
                itemStackList.add(new ItemStack (tileOre.getDropProducer(i).getItem(), fortune, tileOre.getDropProducer(i).getMetaData()));
        }

        if ( itemStackList.size() == 0 )
            itemStackList.add(new ItemStack(Blocks.COBBLESTONE, 1));

        return itemStackList;
    }

    @Override
    public boolean canDropFromExplosion(final Explosion explosion)
    {
        return false;
    }



    public int getDropProducerCount()
    {
        return dropProducer.size();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (state.getBlock() instanceof TileOre )
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


}
