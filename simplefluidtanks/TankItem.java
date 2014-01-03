package simplefluidtanks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;

public class TankItem extends ItemBlock
{
	public TankItem(int id)
	{
		super(id);
		this.setMaxStackSize(64);
		this.setCreativeTab(SimpleFluidTanks.creativeTab);
		this.setUnlocalizedName(SimpleFluidTanks.REGISTRY_TANKITEM_NAME);
	}

	@Override
	public Icon getIconFromDamage(int side)
	{
	    return Block.blocksList[SimpleFluidTanks.tankBlockId].getIcon(0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}
}