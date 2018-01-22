package com.mmcmain.hardcoremining.general;

import com.mmcmain.hardcoremining.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler
{

    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if ( fuel.getItem() == ModItems.chunkShale.getItem() )
            return 16000;
        else if ( fuel.getItem() == ModItems.chunkKerogen )
            return 32000;
        else
            return 9;
    }
}
