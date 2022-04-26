package neoroutine.minetd.common.blocks.towers.rook;

import neoroutine.minetd.common.blocks.towers.*;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class RookBlockEntity extends TowerBlockEntity
{
    public RookBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.ROOK_BE.get(), position, state);

        towerProperties.setReach(TowerReach.ROOK);
        towerProperties.setDelay(TowerDelay.ROOK);
        towerProperties.setDamage(TowerDamage.ROOK);
        towerProperties.setConsumption(TowerConsumption.ROOK);

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        reachBox = new AABB(relativeCounter(worldPosition, facing, towerProperties.getReach()+1)).inflate(towerProperties.getReach());

        this.moveItem = Registration.ROOK_MOVE.get();
    }
}
