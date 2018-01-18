package com.mmcmain.hardcoremining.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mmcmain.hardcoremining.HardcoreMiningMod;

public class RMLog
{
	private static final Logger logger = LogManager.getLogger(HardcoreMiningMod.modId);
	private static final boolean DEBUG = false;

	public final static void warn(String msg)
	{
		logger.warn(msg);
	}
	
	public final static void error(String msg)
	{
		logger.error(msg);
	}
	
	public final static void info(String msg)
	{
		logger.info(msg);
	}

	public final static void debug(String msg)
	{
		if ( DEBUG )
			logger.info(msg);
		else
			logger.debug(msg);
	}
}
