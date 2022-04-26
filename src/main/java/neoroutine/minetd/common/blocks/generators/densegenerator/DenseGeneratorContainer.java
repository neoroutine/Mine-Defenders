package neoroutine.minetd.common.blocks.generators.densegenerator;

import neoroutine.minetd.common.blocks.container.BaseContainer;
import neoroutine.minetd.common.capabilities.CapabilityBurningItem;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.energy.BurningItem;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class DenseGeneratorContainer extends BaseContainer
{
    public DenseGeneratorContainer(int windowId, BlockPos position, Inventory playerInventory, Player player)
    {
        super(Registration.DENSE_GENERATOR_CONTAINER, Registration.DENSE_GENERATOR, windowId, position, playerInventory, player);

    }

    @Override
    protected void trackPower()
    {
        //Lowest 4 bytes
        addDataSlot(new DataSlot()
        {
            @Override
            public int get()
            {
                return getEnergy() & 0xffff;
            }

            @Override
            public void set(int value)
            {
                blockEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler ->
                {
                    int energyStored = handler.getEnergyStored() & 0xffff0000;
                    ((BaseEnergyStorage)handler).setEnergy(energyStored + (value & 0xffff));
                });
            }
        });

        addDataSlot(new DataSlot()
        {
            @Override
            public int get()
            {
                return (getEnergy() >> 16) & 0xffff;
            }

            @Override
            public void set(int value)
            {
                blockEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler ->
                {
                    int energyStored = handler.getEnergyStored() & 0x0000ffff;
                    ((BaseEnergyStorage)handler).setEnergy(energyStored | (value << 16));
                });
            }
        });
    }

    public int getEnergy()
    {
        return blockEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getBurningItemCounter()
    {
        int result = blockEntity.getCapability(CapabilityBurningItem.BURNING_ITEM_CAPABILITY).map(BurningItem::getBurningCounter).orElse(0);
        return result;
    }

    public String getBurningItemName()
    {
        return blockEntity.getCapability(CapabilityBurningItem.BURNING_ITEM_CAPABILITY).map(BurningItem::getBurningItemName).orElse("None");
    }

    public int getMaxPowerCapacity()
    {
        return blockEntity.getCapability(CapabilityEnergyProperties.ENERGY_PROPERTIES_CAPABILITY).map(BaseEnergyProperties::getMaxPowerCapacity).orElse(0);
    }

    public int getPowerGeneration()
    {
        return blockEntity.getCapability(CapabilityEnergyProperties.ENERGY_PROPERTIES_CAPABILITY).map(BaseEnergyProperties::getPowerGeneration).orElse(0);
    }

}
