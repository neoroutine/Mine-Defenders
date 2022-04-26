package neoroutine.minetd.common.blocks.towers.pawn;

import neoroutine.minetd.common.blocks.towers.*;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PawnBlockEntity extends TowerBlockEntity
{

    public PawnBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.PAWN_BE.get(), position, state);

        towerProperties.setReach(TowerReach.PAWN);
        towerProperties.setDelay(TowerDelay.PAWN);
        towerProperties.setDamage(TowerDamage.PAWN);
        towerProperties.setConsumption(TowerConsumption.PAWN);

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        reachBox = new AABB(relativeCounter(worldPosition, facing, towerProperties.getReach()+1)).inflate(towerProperties.getReach());

        this.moveItem = Registration.PAWN_MOVE.get();
    }

}
