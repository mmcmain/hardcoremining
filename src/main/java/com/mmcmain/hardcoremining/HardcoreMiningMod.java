package com.mmcmain.hardcoremining;

import com.mmcmain.hardcoremining.general.ModChecker;
import com.mmcmain.hardcoremining.general.RMLog;
import com.mmcmain.hardcoremining.item.ModItems;
import com.mmcmain.hardcoremining.block.ModBlocks;
import com.mmcmain.hardcoremining.proxy.CommonProxy;
import com.mmcmain.hardcoremining.recipie.ModRecipes;
import com.mmcmain.hardcoremining.world.ModWorldGen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = HardcoreMiningMod.modId, name = HardcoreMiningMod.name, version = HardcoreMiningMod.version, acceptedMinecraftVersions = "[1.10.2]")


public class HardcoreMiningMod 
{
	public static final String modId = "hardcoremining";
	public static final String name = "Hardcore Mining Mod";
	public static final String version = "1.0";
	
	public static final HardcoreMiningTab creativeTab = new HardcoreMiningTab();
	
	@Mod.Instance(modId)
	public static HardcoreMiningMod instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		RMLog.info("Hardcore Mining is preinitializing...");
		ModItems.init();
		ModBlocks.init();
		GameRegistry.registerWorldGenerator(new  ModWorldGen(), 3);
	}
	

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
	    proxy.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		RMLog.info("Hardcore Mining is post initializing...");

		ModChecker.postInit();
		ModItems.postInit();
		ModBlocks.postInit();
        ModRecipes.postInit();
	}
	
	@SidedProxy(serverSide = "com.mmcmain.hardcoremining.proxy.CommonProxy",
			clientSide = "com.mmcmain.hardcoremining.proxy.ClientProxy")
	public static CommonProxy proxy;
	
}
