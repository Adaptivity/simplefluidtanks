package simplefluidtanks;

import net.minecraftforge.client.MinecraftForgeClient;
import simplefluidtanks.rendering.TankBlockRenderer;
import simplefluidtanks.rendering.TankItemRenderer;
import simplefluidtanks.rendering.ValveItemRenderer;
import simplefluidtanks.tileentities.TankBlockEntity;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		// create and register custom renderers
		SimpleFluidTanks.tankBlockRenderer = new TankBlockRenderer();
		SimpleFluidTanks.tankItemRenderer = new TankItemRenderer();
		SimpleFluidTanks.valveItemRenderer = new ValveItemRenderer();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TankBlockEntity.class, SimpleFluidTanks.tankBlockRenderer);
		MinecraftForgeClient.registerItemRenderer(SimpleFluidTanks.tankBlock.blockID, SimpleFluidTanks.tankItemRenderer);
		MinecraftForgeClient.registerItemRenderer(SimpleFluidTanks.valveBlock.blockID, SimpleFluidTanks.valveItemRenderer);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}
