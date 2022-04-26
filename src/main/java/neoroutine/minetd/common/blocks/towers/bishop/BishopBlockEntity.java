package neoroutine.minetd.common.blocks.towers.bishop;

import neoroutine.minetd.common.blocks.towers.*;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BishopBlockEntity extends TowerBlockEntity
{

    public BishopBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.BISHOP_BE.get(), position, state);

        towerProperties.setReach(TowerReach.BISHOP);
        towerProperties.setDelay(TowerDelay.BISHOP);
        towerProperties.setDamage(TowerDamage.BISHOP);
        towerProperties.setConsumption(TowerConsumption.BISHOP);

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        reachBox = new AABB(relativeCounter(worldPosition, facing, towerProperties.getReach()+1)).inflate(towerProperties.getReach());

        this.moveItem = Registration.BISHOP_MOVE.get();
    }

}
