package neoroutine.minetd.common.capabilities;

import neoroutine.minetd.common.energy.BurningItem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityBurningItem
{
    public static final Capability<BurningItem> BURNING_ITEM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(BurningItem.class);
    }
}