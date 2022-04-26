package neoroutine.minetd.common.blocks.towers.queen;

import neoroutine.minetd.common.blocks.towers.*;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class QueenBlockEntity extends TowerBlockEntity
{

    public QueenBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.QUEEN_BE.get(), position, state);

        towerProperties.setReach(TowerReach.QUEEN);
        towerProperties.setDelay(TowerDelay.QUEEN);
        towerProperties.setDamage(TowerDamage.QUEEN);
        towerProperties.setConsumption(TowerConsumption.QUEEN);

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        reachBox = new AABB(relativeCounter(worldPosition, facing, towerProperties.getReach()+1)).inflate(towerProperties.getReach());

        this.moveItem = Registration.QUEEN_MOVE.get();
    }

}
