package com.mmcmain.hardcoremining.block;

import java.util.ArrayList;
import java.util.List;

import com.mmcmain.hardcoremining.general.ModChecker;
import com.mmcmain.hardcoremining.item.ItemTileOreDrop;
import com.mmcmain.hardcoremining.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.mmcmain.hardcoremining.item.ItemOreDict;
import com.mmcmain.hardcoremining.world.ModBiomeEntry;

public class ModBlocks 
{
	public static List<BlockOre> supportedOres = new ArrayList();
	
	
	// Vanilla


    public static BlockOre oreSedimentary;
    public static BlockOre oreMetamorphic;
    public static BlockOre oreIgneous;
    public static BlockOre oreShale;


	public static void init()
	{


		// Vanilla

        oreSedimentary = register(new BlockOre("oreSedimentary"));
        oreSedimentary.setHarvestLevel("pickaxe", 1);
        register(oreSedimentary.getTileOre());
        oreSedimentary.minY = 5;
        oreSedimentary.maxY = 75;
        oreSedimentary.clumpSize = 4;
        oreSedimentary.veinSize = 30;
		oreSedimentary.biomeEntries.add(new ModBiomeEntry("Hills", ModBiomeEntry.Preferences.BIOME_PREFERRED));
		oreSedimentary.biomeEntries.add(new ModBiomeEntry("Jungle", ModBiomeEntry.Preferences.BIOME_RESTRICTED));
        oreSedimentary.biomeEntries.add(new ModBiomeEntry("Forest", ModBiomeEntry.Preferences.BIOME_RESTRICTED));



        oreMetamorphic = register(new BlockOre("oreMetamorphic"));
        oreMetamorphic.setHarvestLevel("pickaxe", 3);
        register(oreMetamorphic.getTileOre());
        oreMetamorphic.minY = 5;
        oreMetamorphic.maxY = 40;
        oreMetamorphic.clumpSize = 4;
        oreMetamorphic.veinSize = 30;
        oreMetamorphic.biomeEntries.add(new ModBiomeEntry("Plains", ModBiomeEntry.Preferences.BIOME_RESTRICTED));
        oreMetamorphic.biomeEntries.add(new ModBiomeEntry("Desert", ModBiomeEntry.Preferences.BIOME_RESTRICTED));
        oreMetamorphic.biomeEntries.add(new ModBiomeEntry("Taiga", ModBiomeEntry.Preferences.BIOME_PREFERRED));


        oreIgneous = register(new BlockOre("oreIgneous"));
        oreIgneous.setHarvestLevel("pickaxe", 2);
        register(oreIgneous.getTileOre());
        oreIgneous.minY = 5;
        oreIgneous.maxY = 55;
        oreIgneous.clumpSize = 4;
        oreIgneous.veinSize = 30;
        oreIgneous.biomeEntries.add(new ModBiomeEntry("Beach", ModBiomeEntry.Preferences.BIOME_RESTRICTED));
        oreIgneous.biomeEntries.add(new ModBiomeEntry("Jungle", ModBiomeEntry.Preferences.BIOME_PREFERRED));
        oreIgneous.biomeEntries.add(new ModBiomeEntry("Swamp", ModBiomeEntry.Preferences.BIOME_RESTRICTED));



        oreShale = register(new BlockOre("oreShale"));
        oreShale.setHarvestLevel("pickaxe", 1);
        register(oreShale.getTileOre());
        oreShale.minY = 5;
        oreShale.maxY = 50;
        oreShale.clumpSize = 5;
        oreShale.veinSize = 65;
        oreShale.biomeEntries.add(new ModBiomeEntry("Desert", ModBiomeEntry.Preferences.BIOME_PREFERRED));
        oreShale.biomeEntries.add(new ModBiomeEntry("Hills", ModBiomeEntry.Preferences.BIOME_RESTRICTED));
        oreShale.biomeEntries.add(new ModBiomeEntry("River", ModBiomeEntry.Preferences.BIOME_RESTRICTED));
	}

	
	private static <T extends Block> T register(T block, ItemBlock itemBlock)
	{
		GameRegistry.register(block);
		
		if (itemBlock != null)
		{
			GameRegistry.register(itemBlock);
			
			if (block instanceof BlockBase)
			{
				((BlockBase)block).registerItemModel(itemBlock);
			}

			if (itemBlock instanceof ItemOreDict)
			{
				((ItemOreDict)itemBlock).initOreDict();
			}
			
			if (block instanceof BlockTileEntity)
			{
				GameRegistry.registerTileEntity(((BlockTileEntity<?>)block).getTileEntityClass(), block.getRegistryName().toString());
			}
			
			return block;
		}

		if (block instanceof ItemOreDict)
		{
			((ItemOreDict)block).initOreDict();
		}
		
		
		return block;
	}
	
	private static <T extends Block> T register(T block)
	{
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		return register(block, itemBlock);
	}

	public static void postInit() 
	{
        postInitSedimentary();
        postInitIgneous();
        postInitMetamorphic();
        postInitShale();
	}

    private static void postInitShale()
    {
        postInitOre(ModBlocks.oreShale, ModItems.chunkShale);

        if (ModChecker.isRailcraftInstalled)
        {
            ModBlocks.oreShale.getTileOre().addDropProducer(ModItems.nuggetCopper);
            ModBlocks.oreShale.getTileOre().addDropProducer(ModItems.saltpeter);
            ModBlocks.oreShale.getTileOre().addDropProducer(ModItems.sulfur);
        }

    }
	
	
	private static void postInitSedimentary()
    {
        postInitOre(ModBlocks.oreSedimentary, ModItems.chunkSedimentary);
        ModBlocks.oreSedimentary.getTileOre().addDropProducer(ModItems.coal);
        ModBlocks.oreSedimentary.getTileOre().addDropProducer(ModItems.bone);

        if (ModChecker.isRailcraftInstalled)
        {
            ModBlocks.oreSedimentary.getTileOre().addDropProducer(ModItems.nuggetIron);
            ModBlocks.oreSedimentary.getTileOre().addDropProducer(ModItems.nuggetLead);
            ModBlocks.oreSedimentary.getTileOre().addDropProducer(ModItems.nuggetNickel);
            ModBlocks.oreSedimentary.getTileOre().addDropProducer(ModItems.nuggetTin);
        }

    }

    private static void postInitIgneous()
    {
        postInitOre(ModBlocks.oreIgneous, ModItems.chunkIgneous);
        ModBlocks.oreIgneous.getTileOre().addDropProducer(ModItems.nuggetGold);
        ModBlocks.oreIgneous.getTileOre().addDropProducer(ModItems.shardRedstone);
        ModBlocks.oreIgneous.getTileOre().addDropProducer(ModItems.bone);

        if (ModChecker.isRailcraftInstalled)
        {
            ModBlocks.oreIgneous.getTileOre().addDropProducer(ModItems.nuggetSilver);
            ModBlocks.oreIgneous.getTileOre().addDropProducer(ModItems.nuggetZinc);
        }


    }

    private static void postInitMetamorphic()
    {
        postInitOre(ModBlocks.oreMetamorphic, ModItems.chunkMetamorphic);

        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.diamond);
        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.bone);
        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.emerald);
        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.netherquartz);
        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.glowstone);
        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.netherstar);
        ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.lapis);

        if ( ModChecker.isAE2Installed)
        {
            ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.quartzCertus);
            ModBlocks.oreMetamorphic.getTileOre().addDropProducer(ModItems.quartzCertusCharged);
        }


    }

    private static void postInitOre(BlockOre blockOre, ItemTileOreDrop itemTileOreDrop)
	{
		blockOre.getTileOre().addDropProducer(itemTileOreDrop);
		ModBlocks.supportedOres.add(blockOre);
	}
	
}
