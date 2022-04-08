package neoroutine.minetd.client;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.blockentity.king.KingScreen;
import neoroutine.minetd.common.blocks.generators.minigenerator.MiniGeneratorScreen;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseScreen;
import neoroutine.minetd.common.blocks.towers.pawn.PawnScreen;
import neoroutine.minetd.common.blocks.towers.rook.RookScreen;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MineTD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MenuScreenManager
{
    public static void register(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            MenuScreens.register(Registration.MINIGENERATOR_CONTAINER.get(), MiniGeneratorScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.MINIGENERATOR.get(), RenderType.translucent());

            MenuScreens.register(Registration.TOWER_BASE_CONTAINER.get(), TowerBaseScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.TOWER_BASE.get(), RenderType.translucent());

            MenuScreens.register(Registration.PAWN_CONTAINER.get(), PawnScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.PAWN.get(), RenderType.translucent());

            MenuScreens.register(Registration.ROOK_CONTAINER.get(), RookScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.ROOK.get(), RenderType.translucent());

            MenuScreens.register(Registration.KING_CONTAINER.get(), KingScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.KING.get(), RenderType.translucent());
        });
    }
}
