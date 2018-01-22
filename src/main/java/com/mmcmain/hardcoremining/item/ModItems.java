package com.mmcmain.hardcoremining.item;

import com.mmcmain.hardcoremining.general.ModChecker;
import com.mmcmain.hardcoremining.world.ModBiomeEntry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static ItemTileOreDrop chunkSedimentary;
    public static ItemTileOreDrop chunkIgneous;
    public static ItemTileOreDrop chunkMetamorphic;
    public static ItemTileOreDrop chunkShale;

    public static ItemTileOreDrop shardRedstone;
    public static ItemOre shardIron;
    public static ItemOre shardGold;
    public static ItemOre chunkKerogen;
    public static ItemOre shardEnder;
    public static ItemOre nuggetIron;

    public static ItemOre itemOreSampler;


    // Vanilla equivalents
    public static ItemTileOreDrop diamond;
    public static ItemTileOreDrop coal;
    public static ItemTileOreDrop emerald;
    public static ItemTileOreDrop lapis;
    public static ItemTileOreDrop nuggetGold;
    public static ItemTileOreDrop bone;
    public static ItemTileOreDrop glowstone;
    public static ItemTileOreDrop netherquartz;
    public static ItemTileOreDrop netherstar;

    // Railcraft

    public static ItemTileOreDrop nuggetCopper;
    public static ItemTileOreDrop nuggetLead;
    public static ItemTileOreDrop nuggetNickel;
    public static ItemTileOreDrop nuggetZinc;
    public static ItemTileOreDrop nuggetSilver;
    public static ItemTileOreDrop nuggetTin;
    public static ItemTileOreDrop saltpeter;
    public static ItemTileOreDrop sulfur;


    // AE2
    public static ItemTileOreDrop quartzCertus;
    public static ItemTileOreDrop quartzCertusCharged;


    public static void init()
	{
        initModItems();
        initVanilla();
        initIgneous();
        initMetamorphic();
        initSedimentary();
        initShale();
    }


    private static void initModItems()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        // Things for recipes that cannot drop
        shardIron = new ItemOre("shardIron", "shardIron");
        register(shardIron);
        shardGold = new ItemOre("shardGold", "shardGold");
        register(shardGold);
        chunkKerogen = new ItemOre("chunkKerogen", "chunkKerogen");
        register(chunkKerogen);
        shardEnder = new ItemOre("shardEnder", "shardEnder");
        register(shardEnder);
        nuggetIron = new ItemOre("nuggetIron", "nuggetIron");
        register(nuggetIron);


        itemOreSampler = new ItemOreSampler("itemOreSampler", "itemOreSampler");
        register(itemOreSampler);


        // Things that can drop from mining.
        shardRedstone = new ItemTileOreDrop("shardRedstone", "shardRedstone");
        register(shardRedstone.getItem());

        dropModifier = new ItemDropModifier(1, 30, 15);
        shardRedstone.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Taiga");
        dropModifier = new ItemDropModifier(1, 30, 20, 2, biomeEntry);
        shardRedstone.addDropModifier(dropModifier);

    }

    private static void initShale()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        chunkShale = new ItemTileOreDrop ("chunkShale", "chunkShale");
        register(chunkShale.getItem());

        dropModifier = new ItemDropModifier(1, 256, 25);
        chunkShale.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Ocean");
        dropModifier = new ItemDropModifier(1, 40, 25, 2, biomeEntry);
        chunkShale.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Desert");
        dropModifier = new ItemDropModifier(1, 50, 30, 2, biomeEntry);
        chunkShale.addDropModifier(dropModifier);
    }



    private static void initIgneous()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        chunkIgneous = new ItemTileOreDrop ("chunkIgneous", "chunkIgneous");
        register(chunkIgneous.getItem());

        dropModifier = new ItemDropModifier(1, 256, 20);
        chunkIgneous.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Savanna");
        dropModifier = new ItemDropModifier(1, 25, 25, 2, biomeEntry);
        chunkIgneous.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Mesa");
        dropModifier = new ItemDropModifier(1, 35, 30, 2, biomeEntry);
        chunkIgneous.addDropModifier(dropModifier);

    }
    
    private static void initSedimentary()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        chunkSedimentary = new ItemTileOreDrop ("chunkSedimentary", "chunkSedimentary");
        register(chunkSedimentary.getItem());

        dropModifier = new ItemDropModifier(1, 256, 35);
        chunkSedimentary.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(1, 40, 45, 2, biomeEntry);
        chunkSedimentary.addDropModifier(dropModifier);

    }


    private static void initMetamorphic()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        chunkMetamorphic = new ItemTileOreDrop ("chunkMetamorphic", "chunkMetamorphic");
        register(chunkMetamorphic.getItem());

        dropModifier = new ItemDropModifier(1, 256, 35);
        chunkMetamorphic.addDropModifier(dropModifier);

        biomeEntry = ModBiomeEntry.preferredBiome("Taiga");
        dropModifier = new ItemDropModifier(5, 15, 45, 2, biomeEntry);
        chunkMetamorphic.addDropModifier(dropModifier);

    }


    private static void initVanilla()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;



        diamond = new ItemTileOreDrop(Items.DIAMOND);
        biomeEntry = new ModBiomeEntry(ModBiomeEntry.BIOMENAME_DEEFAULT, ModBiomeEntry.Preferences.BIOME_DEFAULT);
        dropModifier = new ItemDropModifier(4, 15, 1, -1, biomeEntry);
        diamond.addDropModifier(dropModifier);


        coal = new ItemTileOreDrop(Items.COAL);
        dropModifier = new ItemDropModifier(1, 256, 10);
        coal.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(1, 256, 15, 2, biomeEntry);
        coal.addDropModifier(dropModifier);
        
        emerald = new ItemTileOreDrop(Items.EMERALD);
        biomeEntry = ModBiomeEntry.preferredBiome("Forest");
        dropModifier = new ItemDropModifier(1, 20, 1, 0, biomeEntry);
        emerald.addDropModifier(dropModifier);
        
        nuggetGold = new ItemTileOreDrop(Items.GOLD_NUGGET);
        dropModifier = new ItemDropModifier(1, 30, 15);
        nuggetGold.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(1, 30, 5, 2, biomeEntry);
        nuggetGold.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("River");
        dropModifier = new ItemDropModifier(1, 30, 5, 2, biomeEntry);
        nuggetGold.addDropModifier(dropModifier);

        lapis = new ItemTileOreDrop(Items.DYE, 4);
        dropModifier = new ItemDropModifier(1, 40, 2);
        lapis.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Ocean");
        dropModifier = new ItemDropModifier(1, 30, 3, 2, biomeEntry);
        lapis.addDropModifier(dropModifier);

        bone = new ItemTileOreDrop(Items.BONE);
        biomeEntry = ModBiomeEntry.preferredBiome("Desert");
        dropModifier = new ItemDropModifier(1, 256, 1, 1, biomeEntry);
        bone.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Swamp");
        dropModifier = new ItemDropModifier(1, 25, 2, 1, biomeEntry);
        bone.addDropModifier(dropModifier);

        glowstone = new ItemTileOreDrop(Items.GLOWSTONE_DUST);
        dropModifier = new ItemDropModifier(1, 20, 3);
        glowstone.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Swamp");
        dropModifier = new ItemDropModifier(1, 25, 3, 1, biomeEntry);
        glowstone.addDropModifier(dropModifier);

        netherquartz = new ItemTileOreDrop(Items.QUARTZ);
        dropModifier = new ItemDropModifier(1, 40, 2);
        netherquartz.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Swamp");
        dropModifier = new ItemDropModifier(1, 45, 3, 2, biomeEntry);
        netherquartz.addDropModifier(dropModifier);


        netherstar = new ItemTileOreDrop(Items.NETHER_STAR);
        biomeEntry = ModBiomeEntry.preferredBiome("Swamp");
        dropModifier = new ItemDropModifier(1, 15, 1, -1, biomeEntry);
        netherstar.addDropModifier(dropModifier);

    }

    private static void postInitAE2()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        quartzCertus = new ItemTileOreDrop(Item.getByNameOrId("appliedenergistics2:material"), 0);
        dropModifier = new ItemDropModifier(1, 25, 2);
        quartzCertus.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("River");
        dropModifier = new ItemDropModifier(1, 45, 3, 1, biomeEntry);
        quartzCertus.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Desert");
        dropModifier = new ItemDropModifier(1, 60, 3, 1, biomeEntry);
        quartzCertus.addDropModifier(dropModifier);

        quartzCertusCharged = new ItemTileOreDrop(Item.getByNameOrId("appliedenergistics2:material"), 1);
        dropModifier = new ItemDropModifier(1, 25, 1);
        quartzCertusCharged.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("River");
        dropModifier = new ItemDropModifier(1, 45, 2, 1, biomeEntry);
        quartzCertusCharged.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Desert");
        dropModifier = new ItemDropModifier(1, 60, 2, 1, biomeEntry);
        quartzCertusCharged.addDropModifier(dropModifier);
    }


    private static void postInitRailcraft()
    {
        ItemDropModifier dropModifier;
        ModBiomeEntry biomeEntry;

        nuggetCopper = new ItemTileOreDrop(Item.getByNameOrId("railcraft:nugget"), 2);
        dropModifier = new ItemDropModifier(1, 256, 5);
        nuggetCopper.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(50, 256, 5, 2, biomeEntry);
        nuggetCopper.addDropModifier(dropModifier);

        saltpeter = new ItemTileOreDrop(Item.getByNameOrId("railcraft:dust"), 2);
        dropModifier = new ItemDropModifier(1, 256, 3);
        saltpeter.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(50, 256, 4, 2, biomeEntry);
        saltpeter.addDropModifier(dropModifier);

        sulfur = new ItemTileOreDrop(Item.getByNameOrId("railcraft:dust"), 1);
        dropModifier = new ItemDropModifier(1, 256, 3);
        sulfur.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(50, 256, 4, 2, biomeEntry);
        sulfur.addDropModifier(dropModifier);

        
        nuggetLead = new ItemTileOreDrop(Item.getByNameOrId("railcraft:nugget"), 4);
        dropModifier = new ItemDropModifier(1, 32, 2);
        nuggetLead.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(1, 20, 3, 2, biomeEntry);
        nuggetLead.addDropModifier(dropModifier);


        nuggetNickel = new ItemTileOreDrop(Item.getByNameOrId("railcraft:nugget"), 7);
        dropModifier = new ItemDropModifier(1, 50, 2);
        nuggetNickel.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(1, 40, 3, 2, biomeEntry);
        nuggetNickel.addDropModifier(dropModifier);


        nuggetZinc = new ItemTileOreDrop(Item.getByNameOrId("railcraft:nugget"), 9);
        dropModifier = new ItemDropModifier(20, 60, 2);
        nuggetZinc.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(20, 50, 3, 2, biomeEntry);
        nuggetZinc.addDropModifier(dropModifier);


        nuggetSilver = new ItemTileOreDrop(Item.getByNameOrId("railcraft:nugget"), 5);
        dropModifier = new ItemDropModifier(1, 25, 2);
        nuggetSilver.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(1, 40, 3, 2, biomeEntry);
        nuggetSilver.addDropModifier(dropModifier);


        nuggetTin = new ItemTileOreDrop(Item.getByNameOrId("railcraft:nugget"), 3);
        dropModifier = new ItemDropModifier(30, 256, 2);
        nuggetTin.addDropModifier(dropModifier);
        biomeEntry = ModBiomeEntry.preferredBiome("Hills");
        dropModifier = new ItemDropModifier(30, 256, 3, 2, biomeEntry);
        nuggetTin.addDropModifier(dropModifier);

    }


	private static <T extends Item> T register(T item)
	{
		GameRegistry.register(item);
		
		if (item instanceof ItemBase)
		{
			((ItemBase)item).registerItemModel();
		}
		
		if(item instanceof ItemOreDict)
		{
			((ItemOreDict)item).initOreDict();
		}
		
		return item;
	}

	public static void postInit()
    {
        // Railcraft
        if (ModChecker.isRailcraftInstalled)
        {
            postInitRailcraft();
        }

        // AE2
        if (ModChecker.isAE2Installed)
        {
            postInitAE2();
        }
    }

}
