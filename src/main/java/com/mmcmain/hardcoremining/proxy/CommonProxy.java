package com.mmcmain.hardcoremining.proxy;

import com.mmcmain.hardcoremining.event.ModEventHandler;
import com.mmcmain.hardcoremining.general.FuelHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(ModEventHandler.class);
		GameRegistry.registerFuelHandler(new FuelHandler());
	}

	public void registerItemRenderer(Item item, int meta, String id)
	{
		
	}

}
