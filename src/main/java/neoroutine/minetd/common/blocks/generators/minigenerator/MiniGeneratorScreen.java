package neoroutine.minetd.common.blocks.generators.minigenerator;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MiniGeneratorScreen extends BaseContainerScreen<MiniGeneratorContainer>
{
        public MiniGeneratorScreen(MiniGeneratorContainer container, Inventory inv, Component name)
        {
            super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/minigenerator_gui.png"));
        }

        @Override
        protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
        {
            String energyState = String.format("Energy: %d/%d", menu.getEnergy(), menu.getMaxPowerCapacity());
            String energyProduction = String.format("+ %d", menu.getBurningItemCounter()*menu.getPowerGeneration());
            String burningItem = String.format("(%s)", menu.getBurningItemName());

            drawString(matrixStack, Minecraft.getInstance().font, energyState, 10, 10, 0xffffff);
            drawString(matrixStack, Minecraft.getInstance().font, energyProduction, 50, 50, 0x00ff00);
            drawString(matrixStack, Minecraft.getInstance().font, burningItem, 50, 60, 0xff0000);
        }
}