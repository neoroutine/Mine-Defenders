package neoroutine.minetd.common.capabilities;

import neoroutine.minetd.common.energy.BurningItem;
import neoroutine.minetd.common.grandmaster.Grandmaster;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityGrandmaster
{
    public static final Capability<Grandmaster> GRANDMASTER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(Grandmaster.class);
    }
}
