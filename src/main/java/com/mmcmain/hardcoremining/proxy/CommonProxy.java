package com.mmcmain.hardcoremining.proxy;

import com.mmcmain.hardcoremining.event.ModEventHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(ModEventHandler.class);
	}

	public void registerItemRenderer(Item item, int meta, String id)
	{
		
	}

}
