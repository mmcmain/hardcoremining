package com.mmcmain.hardcoremining.item;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemTileOreDrop
{
    private Item item;
    private int metaData = 0;
    private List<ItemDropModifier> itemDropModifiers= new ArrayList <ItemDropModifier>();

    public ItemTileOreDrop(String name, String oreName) {
        item = new ItemOre(name, oreName);
    }

    public ItemTileOreDrop(Item item)
    {
        this.item = item;
    }

    public ItemTileOreDrop(Item item, int metaData)
    {
        this.item = item;
        this.metaData = metaData;
    }



    public void addDropModifier(ItemDropModifier itemDropModifier)
    {
        itemDropModifiers.add(itemDropModifier);
    }

    public ItemDropModifier getDropModifier(int index)
    {
        return itemDropModifiers.get(index);
    }

    public int countDropModifiers()
    {
        return itemDropModifiers.size();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getMetaData() {
        return metaData;
    }
}
