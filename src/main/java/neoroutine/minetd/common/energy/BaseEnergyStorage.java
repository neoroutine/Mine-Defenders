package neoroutine.minetd.common.energy;

import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

public class BaseEnergyStorage extends EnergyStorage
{
    public BaseEnergyStorage(int capacity)
    {
        super(capacity, capacity, capacity, 0);
    }

    public BaseEnergyStorage(int capacity, int maxTransfer)
    {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        super(capacity, maxReceive, maxExtract, energy);
    }

    protected void onEnergyChanged(){}



    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        int acceptedEnergy = super.receiveEnergy(maxReceive, simulate);
        if (acceptedEnergy > 0 && !simulate)
        {
            onEnergyChanged();
        }

        return acceptedEnergy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy > 0 && !simulate)
        {
            onEnergyChanged();
        }

        return extractedEnergy;
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
        onEnergyChanged();
    }

    public void addEnergy(int energy)
    {
        this.energy += energy;

        if (this.energy > getMaxEnergyStored())
        {
            this.energy = getMaxEnergyStored();
        }

        onEnergyChanged();
    }

    public void consumeEnergy(int energy)
    {
        this.energy -= energy;
        if (this.energy < 0)
        {
            this.energy = 0;
        }

        onEnergyChanged();
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        super.deserializeNBT(nbt);
        this.energy -= (this.energy * 0.2); // 20% of total energy
    }

    @Override
    public boolean canReceive()
    {
        return getEnergyStored() < getMaxEnergyStored();
    }
}
