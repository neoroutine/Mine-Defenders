package neoroutine.minetd;

import neoroutine.minetd.client.MenuScreenManager;
import neoroutine.minetd.client.Rendering;
import neoroutine.minetd.common.grandmaster.SimpleNetworkHandler;
import neoroutine.minetd.common.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//TODO:Player as a moving tower thanks to a magic staff
//TODO:Better generators should only consume specific fuel items
//TODO:Create a template for json blockstates (tower template)
//TODO:Recipes
//TODO:Javadoc
//TODO:Reduce life when placing tower to avoid spamming ?
//TODO:King stuff with a facing property
//TODO:Trim out "updatePlayer" shenanigans
//TODO:Black(1 lane) and white(4lane) kings
@Mod("minetd")
public class MineTD
{
    public static final String MODID   = "minetd";
    public static final String MODNAME = "Mine Defenders";

    public MineTD()
    {
        init();
    }

    private void init()
    {
        //Block/Item registration
        Registration.register();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.addListener(MenuScreenManager::register));
        bus.addListener(this::setup);
    }

    public void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(SimpleNetworkHandler::init);
    }


}
