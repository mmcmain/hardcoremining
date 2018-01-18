package com.mmcmain.hardcoremining.block;

import com.mmcmain.hardcoremining.general.RMLog;
import com.mmcmain.hardcoremining.item.ItemTileOreDrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public final class TileHelper
{
    public static int adjustedFortune(World world, BlockPos blockPos, ItemTileOreDrop itemDrop, int defaultFortuneLevel)
    {
        int fortuneLevel = 0;
        int chances = 0;


        int count = itemDrop.countDropModifiers();
        for (int j = 0; j < itemDrop.countDropModifiers(); j++)
        {
            RMLog.debug("Checking for " + itemDrop.getItem().getUnlocalizedName());
            int weight = itemDrop.getDropModifier(j).getWeight();
            int minY = itemDrop.getDropModifier(j).getMinY();
            int maxY = itemDrop.getDropModifier(j).getMaxY();
            String itemBiomeName = itemDrop.getDropModifier(j).getBiomeEntry().biomeName;
            String worldBiomeName = world.getBiome(blockPos).getBiomeName();

            if ( itemDrop.getDropModifier(j).getBiomeEntry().isPreferred() )
            {
                if ( worldBiomeName.contains(itemBiomeName) )
                {
                    RMLog.debug("  Preferred Biome " + itemBiomeName + " for " + itemDrop.getItem().getUnlocalizedName() + ": " + fortuneLevel + " chances, " + weight + "weight.");
                    if ( blockPos.getY() >= minY && blockPos.getY() <= maxY )
                        chances = itemDrop.getDropModifier(j).getChances() + defaultFortuneLevel;
                }
                else
                {
                    RMLog.debug("  Preferred Biome: " + itemBiomeName + ", found " + worldBiomeName + ". Skipping.");
                }
            }
            else
            {
                if ( !itemDrop.getDropModifier(j).getBiomeEntry().isRestricted() )
                {
                    RMLog.debug("  Default Biome " + itemBiomeName + " matching " + worldBiomeName + ".");
                    if ( blockPos.getY() >= minY && blockPos.getY() <= maxY )
                        chances = defaultFortuneLevel;
                }
            }

            RMLog.debug("    Total Chances: " + chances + ".");

            while (chances-- > 0)
            {
                int r = world.rand.nextInt(100);
                if ( weight >= r )
                {
                    fortuneLevel++;
                }
            }

            RMLog.debug(itemDrop.getItem().getUnlocalizedName() + ": " + fortuneLevel + " chances, " + weight + "weight.");
            if ( itemDrop.getDropModifier(j).getBiomeEntry().isRestricted() &&
                    worldBiomeName.contains(itemBiomeName))
            {
                RMLog.debug("  Restricted Biome. Terminating generation.");
                return 0;
            }
        }


        return fortuneLevel;
    }

    public static boolean removeChargesFromTile(World world, BlockPos blockPos, int amount)
    {
        TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);

        if ( tileEntityOre != null && tileEntityOre.reduceOre(amount) <= 0 )
        {
            world.destroyBlock(blockPos, false);
            return true;
        }
        else
            return false;

    }

    protected static IBlockState convertOreToTile(World world, BlockPos blockPos)
    {
        Block block;
        IBlockState blockState = null;

        block = world.getBlockState(blockPos).getBlock();

        if ( block instanceof TileOre )
        {
            blockState = world.getBlockState(blockPos);
        }

        if (block instanceof BlockOre)
        {
            RMLog.debug("Converting block to tile.");
            BlockOre blockOre = (BlockOre)block;
            TileOre tileOre = blockOre.getTileOre();

            world.setBlockState(blockPos, tileOre.getDefaultState());
            blockState = tileOre.getDefaultState();
        }
        return blockState;
    }






}
