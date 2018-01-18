package com.mmcmain.hardcoremining.block;

import com.mmcmain.hardcoremining.HardcoreMiningMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockBase extends Block 
{
	protected String name;
	
	public BlockBase(String name)
	{
		super(Material.IRON);
		
		this.name = name;
		
		this.setHardness(10f);
		this.setResistance(5f);
		this.setHarvestLevel("pickaxe", 1);

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(HardcoreMiningMod.creativeTab);
	}

	@SubscribeEvent
	public boolean canDropFromExplosion(final ExplosionEvent event)
	{
		return false;
	}


	public void registerItemModel(ItemBlock itemBlock)
	{
		
		HardcoreMiningMod.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	@Override
	public BlockBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
}
