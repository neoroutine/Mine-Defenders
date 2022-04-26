package neoroutine.minetd.common.blocks.generators.neutrongenerator;

import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import neoroutine.minetd.common.blocks.generators.GeneratorCapacity;
import neoroutine.minetd.common.blocks.generators.GeneratorGeneration;
import neoroutine.minetd.common.blocks.generators.GeneratorPush;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronGeneratorBlockEntity extends GeneratorBlockEntity
{

    public NeutronGeneratorBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.NEUTRON_GENERATOR_BE.get(), position, state);

        energyProperties.setPowerGeneration(GeneratorGeneration.LARGE);
        energyProperties.setMaxPowerCapacity(GeneratorCapacity.LARGE);
        energyProperties.setPowerPush(GeneratorPush.LARGE);
    }

}