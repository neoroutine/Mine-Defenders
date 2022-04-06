package neoroutine.minetd.common.blocks.towers.rook;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RookScreen extends BaseContainerScreen<RookContainer>
{
    public RookScreen(RookContainer container, Inventory inv, Component name)
    {
        super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/tower_base_gui.png"));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        String energyState = String.format("Energy: %d/%d", menu.getEnergy(), menu.getMaxPowerCapacity());
        String grandmaster = String.format("Grandmaster : %s", menu.getGrandmasterName());

        String uuid = menu.getGrandmasterUUID();
        if (uuid.length() > 20) { uuid = uuid.substring(0, 19);}
        String grandmasterUUID = String.format("UUID : %s...", uuid);

        drawString(matrixStack, Minecraft.getInstance().font, energyState, 10, 10, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().font, grandmaster, 10, 30, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().font, grandmasterUUID, 10, 60, 0xffffff);
    }
}
