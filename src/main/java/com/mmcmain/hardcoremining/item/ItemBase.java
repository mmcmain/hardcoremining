package com.mmcmain.hardcoremining.item;

import com.mmcmain.hardcoremining.HardcoreMiningMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item 
{
	protected String name;
	
	public ItemBase(String name)
	{
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(HardcoreMiningMod.creativeTab);
	}
	
	public void registerItemModel()
	{
		HardcoreMiningMod.proxy.registerItemRenderer(this, 0, name);
	}
	
	@Override
	public ItemBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	
}
