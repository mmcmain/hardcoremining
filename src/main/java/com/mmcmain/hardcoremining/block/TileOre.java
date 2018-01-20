package com.mmcmain.hardcoremining.block;


import javax.annotation.Nullable;

import com.mmcmain.hardcoremining.item.ItemTileOreDrop;
import com.mmcmain.hardcoremining.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.mmcmain.hardcoremining.general.RMLog;

import java.util.ArrayList;
import java.util.List;


public class TileOre extends BlockTileEntity<TileEntityOre>
{
	private List<ItemTileOreDrop> dropProducer;
	public static int TOOL_DAMAGE = 5;

    private static final int EXPLOSION_DROP_PRODUCTION = 3;
    private static final int PLAYER_DROP_PRODUCTION = 2;

    private static final int INEFFECIENT_ORE_REDUCTION = 20;

    private BlockPos dropBlockPos = null;

	
	public TileOre(String oreName)
	{
		super(oreName + "Tile");

		this.dropProducer = new ArrayList<>();
	}

	public void addDropProducer(ItemTileOreDrop itemTileOreDrop)
	{
		RMLog.info("Setting drop producer to " + itemTileOreDrop.getItem().getUnlocalizedName() + ".");
		this.dropProducer.add(itemTileOreDrop);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if ( !worldIn.isRemote && hand == EnumHand.MAIN_HAND )
        {
            checkOreSamplerMsg(worldIn, pos, playerIn, hand, heldItem);
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn,hand, heldItem, side, hitX, hitY, hitZ);

    }

    public void checkOreSamplerMsg(World world, BlockPos blockPos, EntityPlayer player, EnumHand hand, ItemStack heldItem)
    {

        if ( !world.isRemote && hand == EnumHand.MAIN_HAND )
        {
            if ( heldItem != null && heldItem.getItem() == ModItems.itemOreSampler )
            {
                TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);
                if ( tileEntityOre != null )
                    player.addChatMessage(new TextComponentString("Remaining Ore: " + tileEntityOre.getOreCounter() + "."));
            }
        }
    }

	private ItemTileOreDrop getDropProducer(int index)
	{
		return ((ItemTileOreDrop) this.dropProducer.get(index));
	}


	@Nullable
	@Override
	public TileEntityOre createTileEntity(World world, IBlockState state)
	{
		return new TileEntityOre();
	}
	
	@Override
	public Class<TileEntityOre> getTileEntityClass()
	{
		return TileEntityOre.class;
	}


	@Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState blockState)
    {
        if ( !world.isRemote && blockState.getBlock() instanceof TileOre )
        {
            TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);
            if ( tileEntityOre != null )
            {
                if ( !tileEntityOre.hasOre() )
                    world.destroyBlock(blockPos, false);
            }
        }
        else
            super.onBlockDestroyedByPlayer(world, blockPos, blockState);
    }


    // Need to ignore willHarvest because different implementations handle it differently and
    // my order is important.
    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos blockPos, @Nullable EntityPlayer player, boolean willHarvest)
    {
        boolean didHarvest = false;

        if ( player != null && player.isCreative())
            didHarvest = super.removedByPlayer(blockState, world, blockPos, player, willHarvest);
        else
        {
            if ( blockState.getBlock() instanceof TileOre )
            {
                TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);
                if ( tileEntityOre != null )
                {
                    if ( player != null )
                        tileEntityOre.reduceOre(INEFFECIENT_ORE_REDUCTION);
                    else
                        tileEntityOre.reduceOre();

                    onBlockDestroyedByPlayer(world, blockPos, blockState);
                    harvestBlock(world, player, blockPos, blockState, tileEntityOre, null);
                    didHarvest = true;
                }
            }
            else
                didHarvest = super.removedByPlayer(blockState, world, blockPos, player, willHarvest);

        }

        return didHarvest;
    }

    @Override
    public void onBlockExploded(World world, BlockPos blockPos, Explosion explosion)
    {
        if ( world.getBlockState(blockPos).getBlock() instanceof TileOre )
        {
            TileOre tileOre = (TileOre) world.getBlockState(blockPos).getBlock();
            TileEntityOre tileEntityOre = (TileEntityOre) world.getTileEntity(blockPos);

            if ( tileEntityOre != null )
            {
                dropBlockPos = new BlockPos(explosion.getPosition());
                tileOre.dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), EXPLOSION_DROP_PRODUCTION);
                tileEntityOre.reduceOre(INEFFECIENT_ORE_REDUCTION);
                if ( !tileEntityOre.hasOre() )
                    world.destroyBlock(blockPos, false);
            }
        }
    }

    private static BlockPos findBetterDropPos(@Nullable EntityPlayer player, BlockPos blockPos)
    {
        BlockPos dropBlockPos;

        if ( player == null )
            dropBlockPos = blockPos;
        else
        {
            BlockPos playerPos = player.getPosition();

            dropBlockPos = blockPos.add(blockPos.getX() < playerPos.getX() ? 1 : blockPos.getX() > playerPos.getX() ? -1 : 0,
                    blockPos.getY() < playerPos.getY() ? 1 : blockPos.getY() > playerPos.getY() ? -1 : 0,
                    blockPos.getZ() < playerPos.getZ() ? 1 : blockPos.getZ() > playerPos.getZ() ? -1 : 0);
        }

        return dropBlockPos;
    }
    
    @Override
    public void harvestBlock(World world, @Nullable EntityPlayer player, BlockPos blockPos, IBlockState blockState, @Nullable TileEntity tileEntity, @Nullable ItemStack itemStack)
    {
        if ( blockState.getBlock() instanceof TileOre )
        {
            int fortune;

            TileOre tileOre = (TileOre) blockState.getBlock();
            fortune = tileOre.adjustFortuneForTool(world, player, blockState, PLAYER_DROP_PRODUCTION);

            tileOre.dropBlockPos = findBetterDropPos(player, blockPos);
            tileOre.dropBlockAsItemWithChance(world, blockPos, blockState, 1f, fortune);
        }
        else
            super.harvestBlock(world, player, blockPos, blockState, tileEntity, itemStack);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            java.util.List<ItemStack> items = getDrops(worldIn, pos, state, fortune);

            for (ItemStack item : items)
            {
                //TODO: Make this spawn in a better position.

                if (dropBlockPos != null)
                    spawnAsEntity(worldIn, dropBlockPos, item);
                else
                    spawnAsEntity(worldIn, pos, item);

            }
        }

    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState blockState, int defaultFortune)
    {
        TileOre tileOre = null;
        List<ItemStack> itemStackList = new java.util.ArrayList<ItemStack>();
        int fortune;

        if ( blockState.getBlock() instanceof TileOre)
            tileOre = (TileOre) blockState.getBlock();

        for ( int i = 0; !((World) world).isRemote && tileOre != null && i < tileOre.getDropProducerCount(); i++ )
        {
            fortune = adjustedFortune((World) world, pos, getDropProducer(i), defaultFortune);
            RMLog.debug("  Adding " + fortune + " " + tileOre.getDropProducer(i).getItem().getUnlocalizedName() + ".");
            if ( fortune > 0 )
                itemStackList.add(new ItemStack (tileOre.getDropProducer(i).getItem(), fortune, tileOre.getDropProducer(i).getMetaData()));
        }

        if ( itemStackList.size() == 0 )
            itemStackList.add(new ItemStack(Blocks.COBBLESTONE, 1));

        return itemStackList;
    }

    @Override
    public boolean canDropFromExplosion(final Explosion explosion)
    {
        return false;
    }

    private static int adjustedFortune(World world, BlockPos blockPos, ItemTileOreDrop itemDrop, int defaultFortuneLevel)
    {
        int fortuneLevel = 0;
        int chances = 0;


        for (int j = 0; j < itemDrop.countDropModifiers(); j++)
        {
            RMLog.debug("Checking for " + itemDrop.getItem().getUnlocalizedName());
            int weight = itemDrop.getDropModifier(j).getWeight();
            int minY = itemDrop.getDropModifier(j).getMinY();
            int maxY = itemDrop.getDropModifier(j).getMaxY();
            String itemBiomeName = itemDrop.getDropModifier(j).getBiomeEntry().biomeName;
            String worldBiomeName = world.getBiome(blockPos).getBiomeName();

            if ( itemDrop.getDropModifier(j).getBiomeEntry().isPreferred() )
            {
                if ( worldBiomeName.contains(itemBiomeName) )
                {
                    RMLog.debug("  Preferred Biome " + itemBiomeName + " for " + itemDrop.getItem().getUnlocalizedName() + ": " + fortuneLevel + " chances, " + weight + "weight.");
                    if ( blockPos.getY() >= minY && blockPos.getY() <= maxY )
                        chances = itemDrop.getDropModifier(j).getChances() + defaultFortuneLevel;
                }
                else
                {
                    RMLog.debug("  Preferred Biome: " + itemBiomeName + ", found " + worldBiomeName + ". Skipping.");
                }
            }
            else
            {
                if ( !itemDrop.getDropModifier(j).getBiomeEntry().isRestricted() )
                {
                    RMLog.debug("  Default Biome " + itemBiomeName + " matching " + worldBiomeName + ".");
                    if ( blockPos.getY() >= minY && blockPos.getY() <= maxY )
                        chances = defaultFortuneLevel;
                }
            }

            RMLog.debug("    Total Chances: " + chances + ".");

            while (chances-- > 0)
            {
                int r = world.rand.nextInt(100);
                if ( weight >= r )
                {
                    fortuneLevel++;
                }
            }

            RMLog.debug(itemDrop.getItem().getUnlocalizedName() + ": " + fortuneLevel + " chances, " + weight + "weight.");
            if ( itemDrop.getDropModifier(j).getBiomeEntry().isRestricted() &&
                    worldBiomeName.contains(itemBiomeName))
            {
                RMLog.debug("  Restricted Biome. Terminating generation.");
                return 0;
            }
        }


        return fortuneLevel;
    }


    private int adjustFortuneForTool(World world, EntityPlayer player, IBlockState affectedBlockState, int defaultFortune)
    {
        ItemStack playerTool;
        Item playerToolItem;
        String requiredTool;
        int playerToolLevel;
        int requiredToolLevel;
        int toolLevelDelta;
        int fortune;
        int oreCount = 0;

        if ( player != null )
        {
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

                oreCount = toolLevelDelta + fortune + defaultFortune;
            }
        }

        if ( oreCount < 1 )
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.NEUTRAL, .5F, .3F);

        return oreCount;
    }



    private int getDropProducerCount()
    {
        return dropProducer.size();
    }


}
