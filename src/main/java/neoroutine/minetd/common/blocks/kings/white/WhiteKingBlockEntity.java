package neoroutine.minetd.common.blocks.kings.white;

import neoroutine.minetd.common.blocks.kings.KingBlockEntity;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class WhiteKingBlockEntity extends KingBlockEntity
{
    public WhiteKingBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.WHITE_KING_BE.get(), position, state);


        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        boundingBox = new AABB(position).inflate(1);
    }
}