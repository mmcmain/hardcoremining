package com.mmcmain.hardcoremining.proxy;

import com.mmcmain.hardcoremining.block.TileEventHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(new TileEventHandler());

	}

	public void registerItemRenderer(Item item, int meta, String id)
	{
		
	}

}
