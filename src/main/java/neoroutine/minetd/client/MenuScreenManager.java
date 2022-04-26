package neoroutine.minetd.client;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.generators.densegenerator.DenseGeneratorContainer;
import neoroutine.minetd.common.blocks.generators.densegenerator.DenseGeneratorScreen;
import neoroutine.minetd.common.blocks.generators.neutrongenerator.NeutronGeneratorContainer;
import neoroutine.minetd.common.blocks.generators.neutrongenerator.NeutronGeneratorScreen;
import neoroutine.minetd.common.blocks.generators.softgenerator.SoftGeneratorScreen;
import neoroutine.minetd.common.blocks.kings.black.BlackKingScreen;
import neoroutine.minetd.common.blocks.kings.white.WhiteKingScreen;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseScreen;
import neoroutine.minetd.common.blocks.towers.bishop.BishopScreen;
import neoroutine.minetd.common.blocks.towers.knight.KnightScreen;
import neoroutine.minetd.common.blocks.towers.pawn.PawnScreen;
import neoroutine.minetd.common.blocks.towers.queen.QueenScreen;
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
            //Generators
            MenuScreens.register(Registration.SOFT_GENERATOR_CONTAINER.get(), SoftGeneratorScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.SOFT_GENERATOR.get(), RenderType.translucent());

            MenuScreens.register(Registration.DENSE_GENERATOR_CONTAINER.get(), DenseGeneratorScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.DENSE_GENERATOR.get(), RenderType.translucent());

            MenuScreens.register(Registration.NEUTRON_GENERATOR_CONTAINER.get(), NeutronGeneratorScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.NEUTRON_GENERATOR.get(), RenderType.translucent());

            //Tower base
            MenuScreens.register(Registration.TOWER_BASE_CONTAINER.get(), TowerBaseScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.TOWER_BASE.get(), RenderType.translucent());

            //Towers
            MenuScreens.register(Registration.PAWN_CONTAINER.get(), PawnScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.PAWN.get(), RenderType.translucent());

            MenuScreens.register(Registration.KNIGHT_CONTAINER.get(), KnightScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.KNIGHT.get(), RenderType.translucent());

            MenuScreens.register(Registration.BISHOP_CONTAINER.get(), BishopScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.BISHOP.get(), RenderType.translucent());

            MenuScreens.register(Registration.ROOK_CONTAINER.get(), RookScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.ROOK.get(), RenderType.translucent());

            MenuScreens.register(Registration.QUEEN_CONTAINER.get(), QueenScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.QUEEN.get(), RenderType.translucent());

            //Kings
            MenuScreens.register(Registration.BLACK_KING_CONTAINER.get(), BlackKingScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.BLACK_KING.get(), RenderType.translucent());

            MenuScreens.register(Registration.WHITE_KING_CONTAINER.get(), WhiteKingScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.WHITE_KING.get(), RenderType.translucent());

            //Blocks
            ItemBlockRenderTypes.setRenderLayer(Registration.LABYRINTH_GLASS.get(), RenderType.translucent());
        });
    }
}
