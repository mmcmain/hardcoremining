package com.mmcmain.hardcoremining.item;

import com.mmcmain.hardcoremining.world.ModBiomeEntry;

public class ItemDropModifier
{
    private int minY;
    private int maxY;
    private int weight;
    private int chances;
    private ModBiomeEntry biomeEntry;

    ItemDropModifier(int minY, int maxY, int weight, int chances, ModBiomeEntry biomeEntry)
    {
        this.minY = minY;
        this.maxY = maxY;
        this.weight = weight;
        this.chances = chances;
        this.biomeEntry = biomeEntry;
    }

    ItemDropModifier(int minY, int maxY, int weight)
    {
        this.minY = minY;
        this.maxY = maxY;
        this.weight = weight;
        this.chances = 0;
        this.biomeEntry = new ModBiomeEntry(ModBiomeEntry.BIOMENAME_DEEFAULT, ModBiomeEntry.Preferences.BIOME_DEFAULT);
    }


    public void setBiomeEntry(String biomeName, ModBiomeEntry.Preferences biomePreference)
    {
        biomeEntry = new ModBiomeEntry(biomeName, biomePreference);
    }


    public ModBiomeEntry getBiomeEntry() {
        return biomeEntry;
    }


    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getChances() {
        return chances;
    }

    public void setChances(int chances) {
        this.chances = chances;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
