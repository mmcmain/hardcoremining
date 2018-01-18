package com.mmcmain.hardcoremining.block;

import com.mmcmain.hardcoremining.general.RMLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TileEventHandler
{
//    @SubscribeEvent
    public void onBlockDestroyedByPlayer(BlockEvent.BreakEvent event)
    {
        boolean isCreative = false;


        if (event.getPlayer() != null)
        {
            isCreative = event.getPlayer().isCreative();
        }

        if ( !isCreative && !event.getWorld().isRemote )
        {
            IBlockState affectedBlockState = TileHelper.convertOreToTile(event.getWorld(), event.getPos());

            if(affectedBlockState != null && affectedBlockState.getBlock() instanceof TileOre)
            {
                affectedBlockState.getBlock().removedByPlayer(affectedBlockState, event.getWorld(), event.getPos(), event.getPlayer(), false);
                event.setCanceled(true);
            }
            else
            {
                event.getState().getBlock().onBlockDestroyedByPlayer(event.getWorld(), event.getPos(), event.getState());
            }
        }
        else
        {
            event.getWorld().destroyBlock(event.getPos(), false);
        }

    }


//    @SubscribeEvent
    public void onBlockExplode(ExplosionEvent.Detonate event)
    {

        if (!event.getWorld().isRemote)
        {
            List<BlockPos> affectedBlocks = event.getExplosion().getAffectedBlockPositions();

            RMLog.debug(affectedBlocks.size() + " blocks hit by explosion.");
            for (int i = 0; i < affectedBlocks.size(); i++)
            {
                BlockPos blockPos = affectedBlocks.get(i);
                IBlockState affectedBlockState = TileHelper.convertOreToTile(event.getWorld(), blockPos);

                if ( affectedBlockState != null && affectedBlockState.getBlock() instanceof TileOre)
                {
                    RMLog.debug( affectedBlockState.getBlock().getLocalizedName() + " detected.");
                    affectedBlocks.remove(i);
                    affectedBlockState.getBlock().onBlockExploded(event.getWorld(), blockPos, event.getExplosion());
                }
                else
                {
                    event.getWorld().getBlockState(blockPos).getBlock().onBlockExploded(event.getWorld(), blockPos, event.getExplosion());
                }

            }
        }
    }





}
