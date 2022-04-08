package neoroutine.minetd.common.blocks.blockentity.king;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class KingScreen extends BaseContainerScreen<KingContainer>
{
    public KingScreen(KingContainer container, Inventory inv, Component name)
    {
        super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/minigenerator_gui.png"));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        String energyState = String.format("THE KING");

        drawString(matrixStack, Minecraft.getInstance().font, energyState, 40, 10, 0xffffff);
    }
}
