package com.mmcmain.hardcoremining.world;



public class ModBiomeEntry 
{
	public static enum Preferences 
	{
		BIOME_RESTRICTED,
		BIOME_PREFERRED,
		BIOME_DEFAULT,
		BIOME_PLENTIFUL
	}

	
	public String biomeName;
	public Preferences biomePreference;
	public static final String BIOMENAME_DEEFAULT = "DEFAULT";
	

	public ModBiomeEntry(String biomeName, Preferences biomePreference)
	{
		this.biomeName = biomeName;
		this.biomePreference = biomePreference;
	}


    public static ModBiomeEntry preferredBiome(String biomeName)
    {
        return new ModBiomeEntry(biomeName, Preferences.BIOME_PREFERRED);
    }

    public static ModBiomeEntry restrictedBiome(String biomeName)
    {
        return new ModBiomeEntry(biomeName, Preferences.BIOME_RESTRICTED);
    }

	public boolean isRestricted()
	{
		return biomePreference == Preferences.BIOME_RESTRICTED;
	}
	
	public boolean isPreferred()
	{
		return biomePreference == Preferences.BIOME_PREFERRED;
	}

	public boolean isPlentiful()
	{
		return biomePreference == Preferences.BIOME_PLENTIFUL;
	}

}
