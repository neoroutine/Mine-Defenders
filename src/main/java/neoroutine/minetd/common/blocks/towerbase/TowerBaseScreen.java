package neoroutine.minetd.common.blocks.towerbase;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TowerBaseScreen extends BaseContainerScreen<TowerBaseContainer>
{
    public TowerBaseScreen(TowerBaseContainer container, Inventory inv, Component name)
    {
        super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/tower_base_gui.png"));
    }


    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        String energyState = String.format("Energy: %d/%d", menu.getEnergy(), menu.getMaxPowerCapacity());

        drawString(matrixStack, Minecraft.getInstance().font, energyState, 10, 10, 0xffffff);
    }
}