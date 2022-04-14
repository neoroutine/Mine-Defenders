package neoroutine.minetd.common.capabilities;

import neoroutine.minetd.common.blocks.king.KingHealth;
import neoroutine.minetd.common.energy.BurningItem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityKingHealth
{
    public static final Capability<KingHealth> KING_HEALTH_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(KingHealth.class);
    }
}
