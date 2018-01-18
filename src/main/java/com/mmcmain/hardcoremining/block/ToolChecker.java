package com.mmcmain.hardcoremining.block;

import com.mmcmain.hardcoremining.general.RMLog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ToolChecker
{
    private World world;
    private EntityPlayer player;
    private ItemStack playerTool;
    private int toolLevelDelta=0;
    private int fortune=0;
    private int oreCount=0;
    private Item playerToolItem = null;
    private int playerToolLevel = -1;
    private int requiredToolLevel = 0;
    private String requiredTool = null;
    public static final int TOOL_DAMAGE = 2;


    public ToolChecker(World world, EntityPlayer player, IBlockState affectedBlockState)
    {
        this.player = player;
        this.world = world;
        playerTool = player.inventory.getCurrentItem();
        if(playerTool != null)
        {
            Block block = affectedBlockState.getBlock();
            playerToolItem = player.inventory.getCurrentItem().getItem();
            requiredTool = block.getHarvestTool(affectedBlockState);
            playerToolLevel = playerToolItem.getHarvestLevel(playerTool, requiredTool, player, affectedBlockState);
            requiredToolLevel = block.getHarvestLevel(affectedBlockState);
            toolLevelDelta = playerToolLevel - requiredToolLevel;
            fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, playerTool);
            oreCount = 0;
        }

    }


    public int processPlayerTool(int count)
    {
        int toolDamage;

        oreCount += (toolLevelDelta + fortune);

        if (toolLevelDelta < 0 )
            toolDamage = TOOL_DAMAGE * (Math.abs(toolLevelDelta) + 1);
        else
            toolDamage = TOOL_DAMAGE;

        playerTool.damageItem(toolDamage, player);
        if (playerTool.getItemDamage() <= 0 && player != null)
        {
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.NEUTRAL, 1F, 1F);
            player.inventory.removeStackFromSlot(player.inventory.currentItem);
        }

        RMLog.debug("Player tool return oreCount: " + oreCount + ", count: " + count + ".");
        return oreCount + count;
    }


    public int getPotentialOreCount(int count)
    {
        return oreCount + toolLevelDelta + count;
    }

    public int getOreCount()
    {
        return oreCount;
    }

    public int getFortune()
    {
        return fortune;
    }

    public int getToolLevelDelta() {
        return toolLevelDelta;
    }

    public String getRequiredTool() {
        return requiredTool;
    }

    public boolean shouldWarn()
    {
        return oreCount <= 0 && playerTool.getUnlocalizedName().contains("pickaxe");
    }

}
