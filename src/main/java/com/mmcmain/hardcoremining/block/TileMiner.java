package com.mmcmain.hardcoremining.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileMiner extends BlockTileEntity<TileEntityMiner>
{
    private final static int DELAY = 200;

    private int delayCounter = 0;
    private List<ItemStack> heldItems = null;

    private BlockPos currentMiningPos = null;



    public TileMiner(String name)
    {
        super(name);
        currentMiningPos = null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.add("A simple miner for simple people.");
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND)
        {
            getTileEntity(world, pos).toggleRunning();
            player.addChatMessage(new TextComponentString("Miner: " + (getTileEntity(world, pos).isRunning() ? "On." : "Off.")));
        }
        return true;
    }

    @Override
    public Class<TileEntityMiner> getTileEntityClass() {
        return TileEntityMiner.class;
    }

    @Nullable
    @Override
    public TileEntityMiner createTileEntity(World world, IBlockState state) {
        return new TileEntityMiner();
    }


    private void ejectToInventory(World world, BlockPos blockPos)
    {
        BlockPos abovePos = blockPos.offset(EnumFacing.UP);
        TileEntity tileEntity = world.getTileEntity(abovePos);

        if ( tileEntity != null && heldItems != null && heldItems.size() > 0 )
        {
            if ( tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)  )
            {
                IItemHandler inventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                dumpItems(inventory, heldItems);

            }
            else if (tileEntity instanceof ISidedInventory)
            {
                ISidedInventory inventory = (ISidedInventory) world.getTileEntity(abovePos);
                if ( inventory != null && inventory.getSlotsForFace(EnumFacing.DOWN).length > 0 )
                    dumpItemsIntoSided(inventory, heldItems);

            }
            else
            if (tileEntity instanceof IInventory)
            {
                IInventory inventory = (IInventory) world.getTileEntity(abovePos);
                if (inventory != null)
                    dumpItems(inventory, heldItems);

            }
        }

    }


    private void dumpItems(IItemHandler inventory, List<ItemStack> itemStacks) {
        boolean thingsLeft = itemStacks.size() > 0;
        boolean storageFull = false;

        while (!storageFull && thingsLeft) {
            if (ItemHandlerHelper.insertItemStacked(inventory, itemStacks.get(0), true) != itemStacks.get(0)) {
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, itemStacks.get(0), false);
                if (remainder != null && remainder.stackSize > 0) {
                    itemStacks.remove(0);
                    itemStacks.add(remainder);
                    storageFull = true;
                } else
                    itemStacks.remove(0);
            }
            thingsLeft = itemStacks.size() > 0;
        }

    }

    private boolean legacyInsertItemStacked(IInventory inventory, List<ItemStack> itemStacks, int insertSlot) {
        if (inventory.isItemValidForSlot(insertSlot, itemStacks.get(0))) {
            ItemStack inventoryStack = inventory.getStackInSlot(insertSlot);
            if (inventoryStack == null || inventoryStack.stackSize == 0)
            {
                inventory.setInventorySlotContents(insertSlot, new ItemStack(itemStacks.get(0).getItem(), itemStacks.get(0).stackSize));
                itemStacks.remove(0);
            }
            else if (inventoryStack.getItem() == itemStacks.get(0).getItem() &&
                    itemStacks.get(0).getMetadata() == inventoryStack.getMetadata())
            {
                int spaceAvailable = inventory.getInventoryStackLimit() - inventoryStack.stackSize;
                if (spaceAvailable > 0) {
                    int amountStorable = Math.min(spaceAvailable, itemStacks.get(0).stackSize);
                    inventoryStack.stackSize += amountStorable;
                    itemStacks.get(0).stackSize -= amountStorable;

                    if (itemStacks.get(0).stackSize == 0)
                        itemStacks.remove(0);
                }
            }
        }

        return itemStacks.size() > 0;

    }

    private boolean dumpItemsIntoSided(ISidedInventory inventory, List<ItemStack> itemStacks) {
        int slots[] = inventory.getSlotsForFace(EnumFacing.DOWN);
        boolean inventoryFull = false;
        boolean thingsLeft = itemStacks.size() > 0;


        while (!inventoryFull && thingsLeft) {
            for (int j = 0; thingsLeft && j < slots.length; j++) {
                int slotIndex = slots[j];
                thingsLeft = legacyInsertItemStacked(inventory, itemStacks, slotIndex);
            }
            inventoryFull = thingsLeft;
        }
        return !thingsLeft;


    }


    private boolean dumpItems(IInventory inventory, List<ItemStack> itemStacks) {
        boolean inventoryFull = false;
        boolean thingsLeft = itemStacks.size() > 0;


        while (!inventoryFull && thingsLeft) {
            for (int j = 0; thingsLeft && j < inventory.getSizeInventory(); j++) {
                thingsLeft = legacyInsertItemStacked(inventory, itemStacks, j);
            }
            inventoryFull = thingsLeft;
        }
        return !thingsLeft;


    }

    private BlockPos findRootOre(World world, BlockPos blockPos)
    {
        BlockPos rootBlockPos = blockPos.offset(EnumFacing.DOWN, 1);
        BlockPos currentPos = null;

        currentPos = findCardinalTile(world, rootBlockPos, EnumFacing.UP);

        if ( currentPos == null )
        {
            if ( getTileOreAtPos(world, rootBlockPos) != null )
                currentPos = rootBlockPos;
        }

        return currentPos;

    }

    private BlockPos findCardinalTile(World world, BlockPos rootPos, EnumFacing excludeFace)
    {
        BlockPos currentPos = null;
        TileOre rootTile = getTileOreAtPos(world, rootPos);

        if ( rootTile != null )
        {
            currentPos = rootPos;
            for ( EnumFacing facing : EnumFacing.values() )
            {
                BlockPos nextPos = null;
                if ( facing != excludeFace )
                    nextPos = findCardinalTile(world, rootPos.offset(facing, 1), facing.getOpposite());

                if ( nextPos != null )
                    currentPos = nextPos;

            }
        }

        return currentPos;

    }




    private TileOre getTileOreAtPos(World world, BlockPos blockPos)
    {
        TileOre tileOre = null;
        BlockOre blockOre;

        if ( world.getBlockState(blockPos).getBlock() instanceof BlockOre )
        {
            blockOre = (BlockOre) world.getBlockState(blockPos).getBlock();
            tileOre = blockOre.convertToTile(world, blockPos);
        }
        else
        {
            TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);
            if ( tileEntityOre != null )
                tileOre = (TileOre) world.getBlockState(blockPos).getBlock();

        }

        return tileOre;
    }


    private void mineCurrentOre(World world, BlockPos blockPos)
    {
        if ( getTileEntity(world, blockPos).isRunning() && heldItems == null || heldItems.size() == 0 )
        {
            if ( currentMiningPos == null )
                currentMiningPos = findRootOre(world, blockPos);

            if ( currentMiningPos != null  )
            {
                TileOre currentMiningTile = getTileOreAtPos(world, currentMiningPos);
                if ( currentMiningTile != null )
                {
                    heldItems = currentMiningTile.removedByMiner(world, currentMiningPos);
                     if ( !currentMiningTile.getTileEntity(world, currentMiningPos).hasOre() )
                    {
                        world.destroyBlock(currentMiningPos, false);
                        currentMiningPos = null;
                    }
                }
            }
            else
                getTileEntity(world, blockPos).toggleRunning();
        }
    }




    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState blockState, Random rand)
    {
        if ( heldItems != null && heldItems.size() > 0 )
            ejectToInventory(world, blockPos);
        else if ( getTileEntity(world, blockPos).isRunning() )
            mineCurrentOre(world, blockPos);
    }


}