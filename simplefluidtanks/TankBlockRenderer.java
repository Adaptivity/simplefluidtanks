package simplefluidtanks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TankBlockRenderer extends TileEntitySpecialRenderer
{
	public static final double flickerOffset = 0.001;
	
	public TankBlockRenderer()
	{
		super();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
	{
		// this should never happen, but hey it's minecraft
		if (tileEntity == null || !(tileEntity instanceof TankBlockEntity))
		{
			LogWrapper.log.severe("Possible map corruption detected. TankBlockEntity missing at x:%d / y:%d / z:%d. Expect severe rendering and tank logic issues.", x, y, z);
			return;
		}
		
		Block block = tileEntity.getBlockType();
		
		// this should also never happen
		if (block == null || !(block instanceof TankBlock))
		{
			LogWrapper.log.severe("Possible map corruption detected. TankBlock missing at x:%d / y:%d / z:%d. Expect severe rendering and tank logic issues.", x, y, z);
			return;
		}
		
		TankBlock tank = (TankBlock)block;
		TankBlockEntity entity = (TankBlockEntity)tileEntity;
		Icon[] icons = tank.getIcons();
		
		TessellationManager.setBaseCoords(x, y, z);
		Tessellator tsr = Tessellator.instance;
		int brightness = block.getMixedBrightnessForBlock(entity.worldObj, entity.xCoord, entity.yCoord, entity.zCoord);
		
		tsr.setBrightness(brightness);
		
		bindTexture(TextureMap.locationBlocksTexture);
		
		tsr.startDrawingQuads();
		
		if (!entity.isPartOfTank())
		{
			renderSolid(entity, icons[0]);
		}
		else
		{
			boolean[] connections = entity.getConnections();
			int fillPercentage = entity.getFillPercentage();
			double fluidHeight = 16.0 / 100 * fillPercentage;
			double verticalTextureOffset = 16.0 / 100 * (100 - fillPercentage);
			Icon fluidIcon = getFluidTexture(entity);
			
			renderPositiveXFace(entity, connections, icons, fluidIcon, fillPercentage, fluidHeight, verticalTextureOffset);
			renderNegativeXFace(entity, connections, icons, fluidIcon, fillPercentage, fluidHeight, verticalTextureOffset);
			renderPositiveZFace(entity, connections, icons, fluidIcon, fillPercentage, fluidHeight, verticalTextureOffset);
			renderNegativeZFace(entity, connections, icons, fluidIcon, fillPercentage, fluidHeight, verticalTextureOffset);
			renderPositiveYFace(entity, connections, icons, fluidIcon, fillPercentage, fluidHeight);
			renderNegativeYFace(entity, connections, icons, fluidIcon, fillPercentage);
		}
		
		tsr.draw();
	}
	
	private void renderSolid(TankBlockEntity entity, Icon icon)
	{
		TessellationManager.renderCube(1, 1, 1, 14, 14, 14, icon, true, TessellationManager.pixel);
	}
	
	private void renderPositiveXFace(TankBlockEntity entity, boolean[] connections, Icon[] icons, Icon fluidIcon, int fillPercentage, double fluidHeight, double verticalTextureOffset)
	{
		// only render this side if there isn't a tank block from the same tank in front of it 
		if (!connections[ConnectedTexturesHelper.XPOS])
		{
			if (fillPercentage > 0 && fluidIcon != null)
			{
				TessellationManager.renderPositiveXFace(16 - flickerOffset, 0, 0, fluidHeight, 16, 0, verticalTextureOffset, 0, 0, fluidIcon, TessellationManager.pixel);
			}
			
			TessellationManager.renderPositiveXFace(16, 0, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.XPOS)]);
			// inner face
			if (shouldRenderInside(entity.xCoord, entity.yCoord, entity.zCoord, ConnectedTexturesHelper.XPOS))
			{
				TessellationManager.renderNegativeXFace(16 - flickerOffset, 0, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.XNEG)]);
			}
		}
	}
	
	private void renderNegativeXFace(TankBlockEntity entity, boolean[] connections, Icon[] icons, Icon fluidIcon, int fillPercentage, double fluidHeight, double verticalTextureOffset)
	{
		// only render this side if there isn't a tank block from the same tank in front of it 
		if (!connections[ConnectedTexturesHelper.XNEG])
		{
			if (fillPercentage > 0 && fluidIcon != null)
			{
				TessellationManager.renderNegativeXFace(0 + flickerOffset, 0, 0, fluidHeight, 16, 0, verticalTextureOffset, 0, 0, fluidIcon, TessellationManager.pixel);
			}
			
			TessellationManager.renderNegativeXFace(0, 0, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.XNEG)]);
			// inner face
			if (shouldRenderInside(entity.xCoord, entity.yCoord, entity.zCoord, ConnectedTexturesHelper.XNEG))
			{
				TessellationManager.renderPositiveXFace(0 + flickerOffset, 0, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.XPOS)]);
			}
		}
	}
	
	private void renderPositiveZFace(TankBlockEntity entity, boolean[] connections, Icon[] icons, Icon fluidIcon, int fillPercentage, double fluidHeight, double verticalTextureOffset)
	{
		// only render this side if there isn't a tank block from the same tank in front of it 
		if (!connections[ConnectedTexturesHelper.ZPOS])
		{
			if (fillPercentage > 0 && fluidIcon != null)
			{
				TessellationManager.renderPositiveZFace(0, 0, 16 - flickerOffset, 16, fluidHeight, 0, verticalTextureOffset, 0, 0, fluidIcon, TessellationManager.pixel);
			}
			
			TessellationManager.renderPositiveZFace(0, 0, 16, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.ZPOS)]);
			// inner face
			if (shouldRenderInside(entity.xCoord, entity.yCoord, entity.zCoord, ConnectedTexturesHelper.ZPOS))
			{
				TessellationManager.renderNegativeZFace(0, 0, 16 - flickerOffset, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.ZNEG)]);
			}
		}
	}
	
	private void renderNegativeZFace(TankBlockEntity entity, boolean[] connections, Icon[] icons, Icon fluidIcon, int fillPercentage, double fluidHeight, double verticalTextureOffset)
	{
		// only render this side if there isn't a tank block from the same tank in front of it 
		if (!connections[ConnectedTexturesHelper.ZNEG])
		{
			if (fillPercentage > 0 && fluidIcon != null)
			{
				TessellationManager.renderNegativeZFace(0, 0, 0 + flickerOffset, 16, fluidHeight, 0, verticalTextureOffset, 0, 0, fluidIcon, TessellationManager.pixel);
			}
			
			TessellationManager.renderNegativeZFace(0, 0, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.ZNEG)]);
			// inner face
			if (shouldRenderInside(entity.xCoord, entity.yCoord, entity.zCoord, ConnectedTexturesHelper.ZNEG))
			{
				TessellationManager.renderPositiveZFace(0, 0, 0 + flickerOffset, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.ZPOS)]);
			}
		}
	}
	
	private void renderPositiveYFace(TankBlockEntity entity, boolean[] connections, Icon[] icons, Icon fluidIcon, int fillPercentage, double fluidHeight)
	{
		if (fillPercentage > 0 && fluidIcon != null)
		{
			TessellationManager.renderPositiveYFace(0, fluidHeight - flickerOffset, 0, 16, 16, fluidIcon);
		}
		
		// only render this side if there isn't a tank block from the same tank in front of it 
		if (!connections[ConnectedTexturesHelper.YPOS])
		{
			TessellationManager.renderPositiveYFace(0, 16, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.YPOS)]);
			// inner face
			if (shouldRenderInside(entity.xCoord, entity.yCoord, entity.zCoord, ConnectedTexturesHelper.YPOS))
			{
				TessellationManager.renderNegativeYFace(0, 16 - flickerOffset, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.YNEG)]);
			}
		}
	}
	
	private void renderNegativeYFace(TankBlockEntity entity, boolean[] connections, Icon[] icons, Icon fluidIcon, int fillPercentage)
	{
		// only render this side if there isn't a tank block from the same tank in front of it 
		if (fillPercentage > 0 && fluidIcon != null && !connections[ConnectedTexturesHelper.YNEG])
		{
			TessellationManager.renderNegativeYFace(0, 0 + flickerOffset, 0, 16, 16, fluidIcon);
		}
		
		if (!connections[ConnectedTexturesHelper.YNEG])
		{
			TessellationManager.renderNegativeYFace(0, 0, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.YNEG)]);
			// inner face
			if (shouldRenderInside(entity.xCoord, entity.yCoord, entity.zCoord, ConnectedTexturesHelper.YNEG))
			{
				TessellationManager.renderPositiveYFace(0, 0 + flickerOffset, 0, 16, 16, icons[entity.getTexture(ConnectedTexturesHelper.YPOS)]);
			}
		}
	}
	
	private boolean shouldRenderInside(int x, int y, int z, int side)
	{
		World world = Minecraft.getMinecraft().theWorld;
		
		switch (side)
		{
			case ConnectedTexturesHelper.XPOS:
				return world.isAirBlock(x + 1, y, z);
			case ConnectedTexturesHelper.XNEG:
				return world.isAirBlock(x - 1, y, z);
			case ConnectedTexturesHelper.YPOS:
				return world.isAirBlock(x, y + 1, z);
			case ConnectedTexturesHelper.YNEG:
				return world.isAirBlock(x, y - 1, z);
			case ConnectedTexturesHelper.ZPOS:
				return world.isAirBlock(x, y, z + 1);
			case ConnectedTexturesHelper.ZNEG:
				return world.isAirBlock(x, y, z - 1);
			default:
				return false;
		}
	}
	
	private Icon getFluidTexture(TankBlockEntity entity)
	{
		ValveBlockEntity valve = entity.getValve();
		
		if (valve != null)
		{
			FluidStack fluidStack = valve.getFluid();
			
			if (fluidStack != null)
			{
				return fluidStack.getFluid().getIcon();
			}
		}
		
		return null;
	}
}
