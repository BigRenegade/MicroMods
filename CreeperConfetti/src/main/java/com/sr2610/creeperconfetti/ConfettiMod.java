package com.sr2610.creeperconfetti;

import com.sr2610.creeperconfetti.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ConfettiMod.MODID, version = ConfettiMod.VERSION)
public class ConfettiMod {
	public static final String MODID = "creeperconfetti";
	public static final String VERSION = "1.1";
	public static final String PROXY_COMMON = "com.sr2610.creeperconfetti.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "com.sr2610.creeperconfetti.proxy.ClientProxy";

	@SidedProxy(serverSide = PROXY_COMMON, clientSide = PROXY_CLIENT)
	public static CommonProxy proxy;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

}
