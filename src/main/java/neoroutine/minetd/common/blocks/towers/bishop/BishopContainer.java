package neoroutine.minetd.common.blocks.towers.bishop;

import neoroutine.minetd.common.blocks.container.BaseContainer;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.capabilities.CapabilityGrandmaster;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.grandmaster.Grandmaster;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BishopContainer extends BaseContainer
{
    public BishopContainer(int windowId, BlockPos position, Inventory playerInventory, Player player)
    {
        super(Registration.BISHOP_CONTAINER, Registration.BISHOP, windowId, position, playerInventory, player);
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

    public int getMaxPowerCapacity()
    {
        return blockEntity.getCapability(CapabilityEnergyProperties.ENERGY_PROPERTIES_CAPABILITY).map(BaseEnergyProperties::getMaxPowerCapacity).orElse(0);
    }

    public String getGrandmasterName()
    {
        return blockEntity.getCapability(CapabilityGrandmaster.GRANDMASTER_CAPABILITY).map(Grandmaster::getGrandmasterName).orElse("Unknown");
    }

    public String getGrandmasterUUID()
    {
        return blockEntity.getCapability(CapabilityGrandmaster.GRANDMASTER_CAPABILITY).map(Grandmaster::getGrandMasterUUID).orElse("Unknown");
    }

    public int getGrandmasterEloPoints()
    {
        return blockEntity.getCapability(CapabilityGrandmaster.GRANDMASTER_CAPABILITY).map(Grandmaster::getGrandmasterEloPoints).orElse(0);
    }

}
