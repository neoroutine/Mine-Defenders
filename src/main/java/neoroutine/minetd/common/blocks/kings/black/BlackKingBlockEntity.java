package neoroutine.minetd.common.blocks.kings.black;

import neoroutine.minetd.common.blocks.kings.KingBlockEntity;
import neoroutine.minetd.common.blocks.towers.TowerConsumption;
import neoroutine.minetd.common.blocks.towers.TowerDamage;
import neoroutine.minetd.common.blocks.towers.TowerDelay;
import neoroutine.minetd.common.blocks.towers.TowerReach;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BlackKingBlockEntity extends KingBlockEntity
{
    public BlackKingBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.BLACK_KING_BE.get(), position, state);


        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        boundingBox = new AABB(position).inflate(1);
    }
}
