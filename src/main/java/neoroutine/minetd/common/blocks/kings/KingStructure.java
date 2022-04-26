package neoroutine.minetd.common.blocks.kings;

import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseBE;
import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public class KingStructure
{
    public static boolean isModdedBE(BlockPos pos, Level level)
    {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof GeneratorBlockEntity ||
                be instanceof TowerBaseBE ||
                be instanceof TowerBlockEntity ||
                be instanceof KingBlockEntity)
        {
            return true;
        }

        return false;
    }

    public static void carefulPlace(BlockPos pos, Block buildingBlock, Level level)
    {
        if (!isModdedBE(pos, level))
        {
            level.setBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    public static void recklessPlace(BlockPos pos, Block buildingBlock, Level level)
    {
        level.setBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
    }

    public static void cleanHallway(KingBlockEntity king, int width, int length, Direction facing)
    {
        //Lower left origin (Ground)
        BlockPos pos = king.getBlockPos();
        Level level = king.getLevel();


        BlockPos center = null;
        BlockPos currentPos = null;

        int widthCoefX = 0;
        int widthCoefZ = 0;

        switch (facing)
        {
            //Z+
            case NORTH:
                center = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 5);
                currentPos = new BlockPos(center.getX() + width/2, center.getY(), center.getZ());

                widthCoefX = -1;
                widthCoefZ = 0;
                break;

            case SOUTH:
                center = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 5);
                currentPos = new BlockPos(center.getX() - width/2, center.getY(), center.getZ());

                widthCoefX = 1;
                widthCoefZ = 0;
                break;

            //X+
            case WEST:
                center = new BlockPos(pos.getX() + 5, pos.getY() - 1, pos.getZ());
                currentPos = new BlockPos(center.getX(), center.getY(), center.getZ() - width/2);

                widthCoefX = 0;
                widthCoefZ = 1;
                break;

            case EAST:
                center = new BlockPos(pos.getX() - 5, pos.getY() - 1, pos.getZ());
                currentPos = new BlockPos(center.getX(), center.getY(), center.getZ() + width/2);

                widthCoefX = 0;
                widthCoefZ = -1;

                break;
        }

        for (int i = 0; i < length; i++)
        {
            for (int j = 0; j < width; j++)
            {
                //Guessed middle of the lane
                if (currentPos.getX() == center.getX() || currentPos.getZ() == center.getZ())
                {
                    recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), Blocks.AIR, level);
                    recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), Blocks.AIR, level);
                    recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY()+3, currentPos.getZ()), Blocks.AIR, level);
                }

                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+3, currentPos.getZ()), Blocks.AIR, level);

                currentPos = new BlockPos(currentPos.getX() + 1*widthCoefX, currentPos.getY(), currentPos.getZ() + 1*widthCoefZ);
            }

            switch (facing)
            {
                case NORTH:
                    currentPos = new BlockPos(center.getX() + width/2, currentPos.getY(), currentPos.getZ()+1);
                    break;
                case SOUTH:
                    currentPos = new BlockPos(center.getX() - width/2, currentPos.getY(), currentPos.getZ()-1);
                    break;
                case WEST:
                    currentPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), center.getZ() - width/2);
                    break;
                case EAST:
                    currentPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), center.getZ() + width/2);
                    break;
            }

        }
    }

    public static void cleanJail(KingBlockEntity king, Direction facing)
    {
        BlockPos pos = king.getBlockPos();
        Level level = king.getLevel();

        BlockPos center = pos;
        BlockPos currentPos = pos;

        switch (facing)
        {
            //-Z
            case NORTH:
                center = new BlockPos(pos.getX(), pos.getY(), pos.getZ()+2);
                currentPos = center;

                for (int i =0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        recklessPlace(new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ()), Blocks.AIR, level);
                        recklessPlace(currentPos, Blocks.AIR, level);
                        recklessPlace(new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ()), Blocks.AIR, level);

                        currentPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                    }
                    currentPos = new BlockPos(center.getX(), currentPos.getY()+1, center.getZ());
                }
                break;

            //+Z
            case SOUTH:
                center = new BlockPos(pos.getX(), pos.getY(), pos.getZ()-2);
                currentPos = center;

                for (int i =0; i < 3; i++)
                {
                    for (int j = 0;j < 3; j++)
                    {
                        recklessPlace(new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ()), Blocks.AIR, level);
                        recklessPlace(currentPos, Blocks.AIR, level);
                        recklessPlace(new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ()), Blocks.AIR, level);

                        currentPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                    }
                    currentPos = new BlockPos(center.getX(), currentPos.getY()+1, center.getZ());
                }
                break;

             //-X
            case WEST:
                center = new BlockPos(pos.getX()+2, pos.getY(), pos.getZ());
                currentPos = center;

                for (int i =0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1), Blocks.AIR, level);
                        recklessPlace(currentPos, Blocks.AIR, level);
                        recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1), Blocks.AIR, level);

                        currentPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                    }
                    currentPos = new BlockPos(center.getX(), currentPos.getY()+1, center.getZ());
                }
                break;

            //+X
            case EAST:
                center = new BlockPos(pos.getX()-2, pos.getY(), pos.getZ());
                currentPos = center;

                for (int i =0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1), Blocks.AIR, level);
                        recklessPlace(currentPos, Blocks.AIR, level);
                        recklessPlace(new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1), Blocks.AIR, level);

                        currentPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                    }
                    currentPos = new BlockPos(center.getX(), currentPos.getY()+1, center.getZ());
                }
                break;

        }
    }
}
