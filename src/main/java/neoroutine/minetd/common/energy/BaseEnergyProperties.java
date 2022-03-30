package neoroutine.minetd.common.energy;

public class BaseEnergyProperties
{
    private int POWER_MAX_CAPACITY = 10_000;
    private int POWER_GENERATION   = 10; //Power generation per tick
    private int POWER_PUSH         = 100;

    public BaseEnergyProperties(int maxCapacity, int generation, int push)
    {
        POWER_MAX_CAPACITY = maxCapacity;
        POWER_GENERATION = generation;
        POWER_PUSH = push;
    }

    public int getMaxPowerCapacity()
    {
        return POWER_MAX_CAPACITY;
    }

    public void setMaxPowerCapacity(int maxPowerCapacity)
    {
        this.POWER_MAX_CAPACITY = maxPowerCapacity;
    }

    public int getPowerGeneration()
    {
        return POWER_GENERATION;
    }

    public void setPowerGeneration(int powerGeneration) {
        this.POWER_GENERATION = powerGeneration;
    }

    public int getPowerPush()
    {
        return POWER_PUSH;
    }

    public void setPowerPush(int powerPush) {
        this.POWER_PUSH = powerPush;
    }
}
