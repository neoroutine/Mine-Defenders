package neoroutine.minetd.common.events;


import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.entities.antiking.AntikingEntity;
import neoroutine.minetd.common.setup.Registration;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineTD.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModHandlers
{
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event)
    {
        event.put(Registration.ANTIKING.get(), AntikingEntity.prepareAttributes().build());
    }
}
