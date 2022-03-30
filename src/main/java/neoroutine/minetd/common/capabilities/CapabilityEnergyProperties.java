package neoroutine.minetd.common.capabilities;

import neoroutine.minetd.common.energy.BaseEnergyProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.items.IItemHandler;

public class CapabilityEnergyProperties
{
    public static final Capability<BaseEnergyProperties> ENERGY_PROPERTIES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(BaseEnergyProperties.class);
    }
}