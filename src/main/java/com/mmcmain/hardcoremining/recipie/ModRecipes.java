package com.mmcmain.hardcoremining.recipie;

import com.mmcmain.hardcoremining.block.ModBlocks;
import com.mmcmain.hardcoremining.general.ModChecker;
import com.mmcmain.hardcoremining.general.RMLog;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.mmcmain.hardcoremining.item.ModItems;
import net.minecraftforge.oredict.OreDictionary;

public class ModRecipes
{
	public static void postInit()
	{
        RMLog.info ("Adding nugget recipes.");
        GameRegistry.addSmelting(ModItems.chunkMetamorphic.getItem(), new ItemStack(ModItems.shardRedstone.getItem(), 1), 0.1f);
		GameRegistry.addSmelting(ModItems.chunkIgneous.getItem(), new ItemStack(ModItems.shardGold, 1), 0.1f);
        GameRegistry.addSmelting(ModItems.chunkSedimentary.getItem(), new ItemStack(ModItems.shardIron, 1), 0.1f);

        GameRegistry.addShapelessRecipe(new ItemStack( ModItems.nuggetIron, 2), ModItems.shardIron);


        if ( ModChecker.isRailcraftInstalled && ModChecker.isEnderIOInstalled )
        {
            GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.tileMiner), 1),
                    "ifi",
                    "iai",
                    "ipi",
                    'i',
                    Items.IRON_INGOT,
                    'f',
                    Item.getItemFromBlock(Blocks.FURNACE),
                    'p',
                    Items.IRON_PICKAXE,
                    'a',
                    Item.getItemFromBlock(Blocks.ANVIL));
        }
        else
        {
            GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.tileMiner), 1),
                    "ifi",
                    "iai",
                    "ipi",
                    'i',
                    new ItemStack(Item.getByNameOrId("railcraft:plate"), 1, 0).getItem(),
                    'f',
                    Item.getByNameOrId("enderio:blockStirlingGenerator"),
                    'p',
                    new ItemStack(Item.getByNameOrId("railcraft:bore"), 1, 0).getItem(),
                    'a',
                    new ItemStack(Item.getByNameOrId("railcraft:anvil"), 1, 0).getItem());
        }


        GameRegistry.addShapedRecipe(new ItemStack(Items.IRON_INGOT, 1),
                "nn ",
                "nn ",
                "   ",
                'n',
                ModItems.shardIron);



        GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemOreSampler, 1),
                "cxr",
                " g ",
                "i  ",
                'c',
                Items.CLOCK,
                'x',
                Items.COMPASS,
                'r',
                ModItems.shardRedstone.getItem(),
                'g',
                ModItems.shardGold,
                'i',
                ModItems.shardIron);




        GameRegistry.addShapedRecipe(new ItemStack(Items.REDSTONE, 1),
                "nn ",
                "nn ",
                "   ",
                'n',
                ModItems.shardRedstone.getItem());


        GameRegistry.addShapedRecipe(new ItemStack(Items.GOLD_NUGGET, 1),
                "ss ",
                "ss ",
                "   ",
                's',
                ModItems.shardGold);



        RMLog.info ("Adding Railcraft recipes");
		if ( ModChecker.isRailcraftInstalled )
		{
            GameRegistry.addSmelting(ModItems.chunkShale.getItem(), new ItemStack(Item.getByNameOrId("railcraft:dust"), 1,  1), 0.3f);
            GameRegistry.addSmelting(ModItems.chunkKerogen, new ItemStack(Item.getByNameOrId("railcraft:dust"), 1,  2), 0.3f);
		}
	}
}
