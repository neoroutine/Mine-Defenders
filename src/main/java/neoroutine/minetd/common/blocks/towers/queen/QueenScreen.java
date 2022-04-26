package neoroutine.minetd.common.blocks.towers.queen;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
import neoroutine.minetd.common.blocks.towers.pawn.PawnContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QueenScreen extends BaseContainerScreen<QueenContainer>
{
    public QueenScreen(QueenContainer container, Inventory inv, Component name)
    {
        super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/tower_gui.png"));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        String energyState = String.format("Energy: %d/%d", menu.getEnergy(), menu.getMaxPowerCapacity());
        String grandmaster = String.format("GM : %s", menu.getGrandmasterName());
        String grandmasterPoints = String.format("Elo : %d", menu.getGrandmasterEloPoints());

        //String uuid = menu.getGrandmasterUUID();
        //if (uuid.length() > 20) { uuid = uuid.substring(0, 19);}
        //String grandmasterUUID = String.format("UUID : %s...", uuid);

        drawString(matrixStack, Minecraft.getInstance().font, energyState, 10, 10, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().font, grandmaster, 10, 30, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().font, grandmasterPoints, 10, 40, 0xffffff);
        //drawString(matrixStack, Minecraft.getInstance().font, grandmasterUUID, 10, 60, 0xffffff);

    }
}
