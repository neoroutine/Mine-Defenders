package neoroutine.minetd.common.blocks.towers.knight;

import neoroutine.minetd.common.blocks.towers.*;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class KnightBlockEntity extends TowerBlockEntity
{

    public KnightBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.KNIGHT_BE.get(), position, state);

        towerProperties.setReach(TowerReach.KNIGHT);
        towerProperties.setDelay(TowerDelay.KNIGHT);
        towerProperties.setDamage(TowerDamage.KNIGHT);
        towerProperties.setConsumption(TowerConsumption.KNIGHT);

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        reachBox = new AABB(relativeCounter(worldPosition, facing, towerProperties.getReach()+1)).inflate(towerProperties.getReach());

        this.moveItem = Registration.KNIGHT_MOVE.get();
    }

}
