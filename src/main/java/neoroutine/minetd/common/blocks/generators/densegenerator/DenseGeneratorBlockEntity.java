package neoroutine.minetd.common.blocks.generators.densegenerator;

import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import neoroutine.minetd.common.blocks.generators.GeneratorCapacity;
import neoroutine.minetd.common.blocks.generators.GeneratorGeneration;
import neoroutine.minetd.common.blocks.generators.GeneratorPush;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DenseGeneratorBlockEntity extends GeneratorBlockEntity
{

    public DenseGeneratorBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.DENSE_GENERATOR_BE.get(), position, state);

        energyProperties.setPowerGeneration(GeneratorGeneration.MEDIUM);
        energyProperties.setMaxPowerCapacity(GeneratorCapacity.MEDIUM);
        energyProperties.setPowerPush(GeneratorPush.MEDIUM);
    }

}