package com.mmcmain.hardcoremining.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileMiner extends BlockTileEntity<TileEntityMiner> {
    private boolean isRunning;


    public TileMiner(String name) {
        super(name);
        isRunning = false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.add("A simple miner for simple people.");
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
            TileEntityMiner tile = getTileEntity(world, pos);
            findInventory(world, pos);
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


    private void findInventory(World world, BlockPos blockPos) {
        BlockPos abovePos = blockPos.offset(EnumFacing.UP);
        TileEntity tileEntity = world.getTileEntity(abovePos);

        if ( tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)  )
        {
            IItemHandler inventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
            List <ItemStack> items = new ArrayList<ItemStack>();

            items.add(new ItemStack(Items.IRON_INGOT, 1));
            dumpItems(inventory, items);

        }
        else if (tileEntity instanceof ISidedInventory)
        {
            ISidedInventory inventory = (ISidedInventory) world.getTileEntity(abovePos);
            List <ItemStack> items = new ArrayList<ItemStack>();

            if ( inventory != null && inventory.getSlotsForFace(EnumFacing.DOWN).length > 0 )
            {
                items.add(new ItemStack(Items.IRON_INGOT, 1));
                dumpItemsIntoSided(inventory, items);
            }

        }
        else
        if (tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory) world.getTileEntity(abovePos);
            List<ItemStack> items = new ArrayList<ItemStack>();

            items.add(new ItemStack(Items.IRON_INGOT, 63));
            if (inventory != null)
                dumpItems(inventory, items);

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


}