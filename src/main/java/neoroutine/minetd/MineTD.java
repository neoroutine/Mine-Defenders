package neoroutine.minetd;

import neoroutine.minetd.client.MenuScreenManager;
import neoroutine.minetd.client.Rendering;
import neoroutine.minetd.common.grandmaster.SimpleNetworkHandler;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//TODO:Create a template for json blockstates (tower template)
//TODO:Javadoc
//TODO:Trim out "updatePlayer" shenanigans
//TODO:Display elo points on player screen
//TODO:Elo Level ?
//TODO:Melee weapons (swords) or traps ?
//TODO:HP and energy bars display in be screens
//TODO:Buttons too (for help)
//TODO:Particles and throwables on items
//TODO:Helper book for the mod
//TODO:Sound design
//TODO:Staffs block message
//TODO:Smarter jumping ai/block avoiding ai
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
