package com.mmcmain.hardcoremining.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraftforge.fml.common.IWorldGenerator;
import com.mmcmain.hardcoremining.block.BlockOre;
import com.mmcmain.hardcoremining.block.ModBlocks;
import com.mmcmain.hardcoremining.general.ModChecker;
import com.mmcmain.hardcoremining.general.RMLog;

public class ModWorldGen implements IWorldGenerator 
{
	private static final int VEIN_CHANCE = 20;
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, 
			World world, IChunkGenerator chunkGenerator, 
			IChunkProvider chunkProvider)
	{
		if (world.provider.getDimension() == 0)
		{
			generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}
	
	private void generateOverworld(Random random, int chunkX, int chunkZ, 
			World world, IChunkGenerator chunkGenerator, 
			IChunkProvider chunkProvider)
	{
		// TODO: Somehow ore is getting generated outside it's Biome's chunk. Need to look deeper.
		String s = world.getBiome(new BlockPos(chunkX * 16, 64, chunkZ * 16)).getBiomeName();

		genOreByBiome(s, world, random, chunkX * 16, chunkZ * 16);
		
	}

	
	private void genOreByBiome(String biomeName, World world, Random random, int x, int z)
	{
		BlockOre ore;
		ModBiomeEntry biomeEntry;
		int defaultBiomeChances;
		boolean isPreferred;
		boolean isRestricted;

		for (int i = 0; i < ModBlocks.supportedOres.size(); i++)
		{
			ore = ModBlocks.supportedOres.get(i);
			isRestricted = false;
			isPreferred = false;

			defaultBiomeChances = 22 + random.nextInt(15);

			for ( int j = 0; !isRestricted && j < ore.biomeEntries.size(); j++ )
			{
				biomeEntry = ore.biomeEntries.get(j);

				if ( biomeEntry.isRestricted() && biomeName.contains(biomeEntry.biomeName) )
				{
					isRestricted = true;
				}

				if ( !isRestricted )
				{
					if ( biomeEntry.isPreferred() && biomeName.contains(biomeEntry.biomeName) )
					{
						isPreferred = true;
					}
				}
			}

			if ( isPreferred && !isRestricted )
			{
				RMLog.debug("(" + biomeName + ")Pref OreGen: " + ore.getLocalizedName());
				int randomInt = random.nextInt(100);
				int count;
				if ( randomInt > 95 )
					count = 3;
				else if (randomInt > 80)
					count = 2;
				else if (randomInt > 50)
					count = 1;
				else
					count = 0;

				if ( count > 0)
				{
					generateOre(ore.getDefaultState(),
							world,
							random,
							x,
							z,
							ore.minY,
							ore.maxY,
							ore.getVeinSize(random),
							count);

					generateGroundOre(ore.getDefaultState(),
							world,
							random,
							x,
							z,
							ore.getClumpSize(random),
							5 + random.nextInt(10));
				}

				defaultBiomeChances = 12 + random.nextInt(10);
			}

			if ( !isRestricted )
			{
				RMLog.debug("(" + biomeName + ")Def OreGen: " + ore.getLocalizedName());
				generateOre(ore.getDefaultState(),
						world,
						random,
						x,
						z,
						ore.minY,
						ore.maxY,
						ore.getClumpSize(random),
						defaultBiomeChances);
			}
		}
	}

	private void generateGroundOre(IBlockState ore, World world, Random random,
							 int x, int z, int size, int chances)
	{
		int maxY = 140;
		int minY = 60;
		int deltaY = maxY - minY;

		for (int i = 0; i < chances; i++)
		{
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

			RMLog.debug(ore.getBlock().getLocalizedName() + " @: " + pos.getX() + ", " + pos.getZ());
			WorldGenMinable generator = new WorldGenMinable(ore, size, BlockMatcher.forBlock(Blocks.DIRT));
			generator.generate(world, random, pos);

			generator = new WorldGenMinable(ore, size, BlockMatcher.forBlock(Blocks.SAND));
			generator.generate(world, random, pos);

			generator = new WorldGenMinable(ore, size, BlockMatcher.forBlock(Blocks.GRAVEL));
			generator.generate(world, random, pos);

			generator = new WorldGenMinable(ore, size, BlockMatcher.forBlock(Blocks.GRASS));
			generator.generate(world, random, pos);

		}

	}

	
	private void generateOre(IBlockState ore, World world, Random random,
			int x, int z, int minY, int maxY, int size, int chances)
	{
		int deltaY = maxY - minY;
		
		for (int i = 0; i < chances; i++)
		{
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

			RMLog.debug(ore.getBlock().getLocalizedName() + " @: " + pos.getX() + ", " + pos.getZ());
			WorldGenMinable generator = new WorldGenMinable(ore, size);
			generator.generate(world, random, pos);
		}
	}
}
