package neoroutine.minetd.common.entities.antiking;

import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AntikingFindKingGoal extends MoveToBlockGoal {
    private final AntikingEntity antiking;

    public AntikingFindKingGoal(AntikingEntity mob, double speedModifier)
    {
        super(mob, speedModifier, 32);
        this.antiking = mob;
    }

    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos)
    {
        if (!pLevel.isEmptyBlock(pPos.above()))
        {
            return false;
        }

        else
        {
            BlockState blockstate = pLevel.getBlockState(pPos);
            return blockstate.is(Registration.BLACK_KING.get()) || blockstate.is(Registration.WHITE_KING.get());
        }
    }
}
