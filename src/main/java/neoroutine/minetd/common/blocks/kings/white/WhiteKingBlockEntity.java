package neoroutine.minetd.common.blocks.kings.white;

import neoroutine.minetd.common.blocks.kings.KingBlockEntity;
import neoroutine.minetd.common.blocks.kings.KingStructure;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class WhiteKingBlockEntity extends KingBlockEntity
{
    public WhiteKingBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.WHITE_KING_BE.get(), position, state);


        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        boundingBox = new AABB(position).inflate(1);

        this.spawnDelay = 20;
    }

    @Override
    protected void spawnAntiking()
    {
        BlockPos pos = getBlockPos();

        Random rand1 = new Random();
        Random rand2 = new Random(rand1.nextLong(0, 100_000_000));

        int widthOffset = rand2.nextInt(-2, 3);
        int lengthOffset = 30;

        BlockPos northPos = new BlockPos(pos.getX()+widthOffset, pos.getY(), pos.getZ()+lengthOffset);
        BlockPos westPos = new BlockPos(pos.getX()-lengthOffset, pos.getY(), pos.getZ()+widthOffset);
        BlockPos eastPos = new BlockPos(pos.getX()+lengthOffset, pos.getY(), pos.getZ()+widthOffset);
        BlockPos southPos = new BlockPos(pos.getX()+widthOffset, pos.getY(), pos.getZ()-lengthOffset);

        BlockPos[] positions = {northPos, westPos, eastPos, southPos};

        int index = rand2.nextInt(0, positions.length);

        spawnAntiking(positions[index]);
    }

    @Override
    protected void cleanLane()
    {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        Runnable task = () ->
        {
            KingStructure.cleanHallway(this, 9, 50, Direction.NORTH);
            KingStructure.cleanHallway(this, 9, 50, Direction.SOUTH);
            KingStructure.cleanHallway(this, 9, 50, Direction.EAST);
            KingStructure.cleanHallway(this, 9, 50, Direction.WEST);

            KingStructure.cleanJail(this, Direction.NORTH);
            KingStructure.cleanJail(this, Direction.SOUTH);
            KingStructure.cleanJail(this, Direction.EAST);
            KingStructure.cleanJail(this, Direction.WEST);
        };

        delayedPredicateClean(task, 20);
    }
}