package net.zarathul.simplefluidtanks.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.zarathul.simplefluidtanks.SimpleFluidTanks;
import net.zarathul.simplefluidtanks.tileentities.TankBlockEntity;

public final class TankBlockDataProvider implements IWailaDataProvider
{
	public static final TankBlockDataProvider instance = new TankBlockDataProvider();
	
	private TankBlockDataProvider()
	{
	}
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity entity = accessor.getTileEntity();
		
		if (entity != null && entity instanceof TankBlockEntity)
		{
			TankBlockEntity tankEntity = (TankBlockEntity)entity;
			
			if (config.getConfig(WailaRegistrar.WAILA_TANK_LINKED_KEY))
			{
				String readableFlag = StatCollector.translateToLocal((tankEntity.isPartOfTank()) ? WailaRegistrar.WAILA_TOOLTIP_YES : WailaRegistrar.WAILA_TOOLTIP_NO);
				currenttip.add(StatCollector.translateToLocalFormatted(WailaRegistrar.WAILA_TOOLTIP_ISLINKED, readableFlag));
			}
			
			if (config.getConfig(WailaRegistrar.WAILA_TANK_CAPACITY_KEY))
			{
				if (config.getConfig(WailaRegistrar.WAILA_CAPACITY_IN_MILLIBUCKETS_KEY))
				{
					currenttip.add(StatCollector.translateToLocalFormatted(WailaRegistrar.WAILA_TOOLTIP_CAPACITY, SimpleFluidTanks.bucketsPerTank * 1000, "", "", "mB", tankEntity.getFillPercentage()) );
				}
				else
				{
					currenttip.add(StatCollector.translateToLocalFormatted(WailaRegistrar.WAILA_TOOLTIP_CAPACITY, SimpleFluidTanks.bucketsPerTank, "", "", "B", tankEntity.getFillPercentage()));
				}
			}
		}
		
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
}
