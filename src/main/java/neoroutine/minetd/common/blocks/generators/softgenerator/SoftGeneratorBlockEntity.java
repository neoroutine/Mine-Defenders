package neoroutine.minetd.common.blocks.generators.softgenerator;

import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import neoroutine.minetd.common.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoftGeneratorBlockEntity extends GeneratorBlockEntity
{

    public SoftGeneratorBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.SOFT_GENERATOR_BE.get(), position, state);

    }

}