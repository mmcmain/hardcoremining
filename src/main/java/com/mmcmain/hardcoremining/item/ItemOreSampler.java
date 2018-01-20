package com.mmcmain.hardcoremining.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemOreSampler extends ItemOre
{
    ItemOreSampler(String name, String oreName)
    {
        super(name, oreName);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
    {

        if ( !world.isRemote && hand == EnumHand.MAIN_HAND )
        {
            player.addChatMessage(new TextComponentString("Curernt Biome: " + world.getBiome(player.getPosition()).getBiomeName()));
            player.addChatMessage(new TextComponentString("Curernt Position: " +
                    player.getPosition().getX() + ", " +
                    player.getPosition().getY() + ", " +
                    player.getPosition().getZ() + "."));
        }

        return super.onItemRightClick(itemStack, world, player, hand);
    }



}
