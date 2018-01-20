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
		boolean isPlentiful;
		int count;

		for (int i = 0; i < ModBlocks.supportedOres.size(); i++)
		{
			ore = ModBlocks.supportedOres.get(i);
			isPlentiful = false;
			isPreferred = false;

			for ( int j = 0; j < ore.biomeEntries.size(); j++ )
			{
				biomeEntry = ore.biomeEntries.get(j);

				if ( biomeEntry.isPlentiful() && biomeName.contains(biomeEntry.biomeName) )
					isPlentiful = true;

				if ( biomeEntry.isPreferred() && biomeName.contains(biomeEntry.biomeName) )
					isPreferred = true;
			}

			if ( isPlentiful )
			{
				if ( random.nextInt(100) > 95 )
					count = 2;
				else
					count = 1;

				generateOre(ore.getDefaultState(), world, random, x, z, ore.minY, ore.maxY, ore.getVeinSize(random), count);
			}
			else if ( isPreferred )
			{
				if ( random.nextInt(100) > 75 )
					generateOre(ore.getDefaultState(), world, random, x, z, ore.minY, ore.maxY, ore.getVeinSize(random), 1);
			}

			defaultBiomeChances = 22 + random.nextInt(15);
			generateOre(ore.getDefaultState(), world, random, x, z, ore.minY, ore.maxY, ore.getClumpSize(random), defaultBiomeChances);

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
