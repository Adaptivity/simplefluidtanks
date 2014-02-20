package simplefluidtanks;

import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * {@link TankBlock} in item form.
 */
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
		return SimpleFluidTanks.tankBlock.getIcon(0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}
}