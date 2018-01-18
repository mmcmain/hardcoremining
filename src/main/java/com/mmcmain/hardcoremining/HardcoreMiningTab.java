package com.mmcmain.hardcoremining;

import com.mmcmain.hardcoremining.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class HardcoreMiningTab extends CreativeTabs
{

	public HardcoreMiningTab()
	{
		super(HardcoreMiningMod.modId);
	}
	
	
	@Override
	public Item getTabIconItem() 
	{
		return ModItems.chunkSedimentary.getItem();
	}

}
