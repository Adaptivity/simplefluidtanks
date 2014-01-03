package simplefluidtanks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		// create creative tab
		SimpleFluidTanks.creativeTab = new CreativeTabs("Simple Fluid Tanks")
		{
			@Override
			@SideOnly(Side.CLIENT)
			public String getTranslatedTabLabel()
			{
				return this.getTabLabel();
			}

			@Override
			@SideOnly(Side.CLIENT)
			public int getTabIconItemIndex()
			{
				// TODO: Change to block id and add icon
				return super.getTabIconItemIndex();
			}
		};
		
		// load config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		SimpleFluidTanks.tankBlockId = config.getBlock(
				SimpleFluidTanks.CONFIG_CATEGORY,
				SimpleFluidTanks.CONFIG_TANKBLOCK_ID_KEY,
				SimpleFluidTanks.CONFIG_DEFAULT_TANKBLOCK_ID,
				SimpleFluidTanks.CONFIG_TANKBLOCK_ID_COMMENT
				).getInt();
		
		SimpleFluidTanks.valveBlockId = config.getBlock(
				SimpleFluidTanks.CONFIG_CATEGORY,
				SimpleFluidTanks.CONFIG_VALVEBLOCK_ID_KEY,
				SimpleFluidTanks.CONFIG_DEFAULT_VALVEBLOCK_ID,
				SimpleFluidTanks.CONFIG_VALVEBLOCK_ID_COMMENT
				).getInt();
		
		SimpleFluidTanks.bucketsPerTank = config.get(
				SimpleFluidTanks.CONFIG_CATEGORY,
				SimpleFluidTanks.CONFIG_BUCKETS_PER_TANK_KEY,
				SimpleFluidTanks.CONFIG_DEFAULT_BUCKETS_PER_TANK,
				SimpleFluidTanks.CONFIG_BUCKETS_PER_TANK_COMMENT
				).getInt();
		
		config.save();
	}
	
	public void init(FMLInitializationEvent event)
	{
		// create and register TankBlock and TankItem
		SimpleFluidTanks.tankBlock = new TankBlock(SimpleFluidTanks.tankBlockId);
		SimpleFluidTanks.tankItem = new TankItem(SimpleFluidTanks.tankBlockId - 256);
		
		GameRegistry.registerBlock(SimpleFluidTanks.tankBlock, TankItem.class, SimpleFluidTanks.REGISTRY_TANKBLOCK_KEY);
		LanguageRegistry.addName(SimpleFluidTanks.tankBlock, SimpleFluidTanks.REGISTRY_TANKBLOCK_READABLE_NAME);
		
		// create and register ValveBlock
		SimpleFluidTanks.valveBlock = new ValveBlock(SimpleFluidTanks.valveBlockId);
		
		GameRegistry.registerBlock(SimpleFluidTanks.valveBlock, SimpleFluidTanks.REGISTRY_VALVEBLOCK_KEY);
		LanguageRegistry.addName(SimpleFluidTanks.valveBlock, SimpleFluidTanks.REGISTRY_VALVEBLOCK_READABLE_NAME);
		
		// register TileEntities
		GameRegistry.registerTileEntity(ValveBlockEntity.class, SimpleFluidTanks.REGISTRY_VALVEBLOCK_ENTITY_KEY);
		GameRegistry.registerTileEntity(TankBlockEntity.class, SimpleFluidTanks.REGISTRY_TANKBLOCK_ENTITY_KEY);
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
