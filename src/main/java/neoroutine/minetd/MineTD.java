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

//TODO:Chess theme (dungeon defenders king's game inspired)
//TODO:Implement a point system, held 30s -> x points (points can be used to create other resources)
//TODO:3D rendering of tower attacks/actions
//TODO:Player as a moving tower thanks to a magic staff
//TODO:Better generators should only consume specific fuel items
//TODO:Create a template for json blockstates (tower template)
//TODO:Recipes
//TODO:Javadoc
//TODO:Take a stance on this. or juste accessing the variable
//TODO:Reduce life when placing tower to avoid spamming ?
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
