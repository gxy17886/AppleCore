package squeek.applecore.asm.reference;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import squeek.applecore.asm.Hooks;

import java.util.Random;

public class BlockCocoaModifications extends BlockCocoa
{
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		// added line
		Result allowGrowthResult = Hooks.fireAllowPlantGrowthEvent(this, world, pos, state, rand);

		if (!this.canBlockStay(world, pos, state))
		{
			world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
			this.dropBlockAsItem(world, pos, state, 0);
		}
		// changed else if
		else if (allowGrowthResult == Result.ALLOW || (allowGrowthResult == Result.DEFAULT && world.rand.nextInt(5) == 0))
		{
			int i = (state.getValue(AGE)).intValue();

			if (i < 2)
			{
				world.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);

				// added line
				Hooks.fireOnGrowthEvent(this, world, pos, state, i);
			}
		}
	}
}