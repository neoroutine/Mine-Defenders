package neoroutine.minetd.client;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.entities.antiking.AntikingModel;
import neoroutine.minetd.common.entities.antiking.AntikingRenderer;
import neoroutine.minetd.common.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineTD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModHandlers
{
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(AntikingModel.ANTIKING_LAYER, AntikingModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(Registration.ANTIKING.get(), AntikingRenderer::new);
    }
}
