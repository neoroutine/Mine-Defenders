package neoroutine.minetd.common.blocks.generators.densegenerator;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
import neoroutine.minetd.common.blocks.generators.softgenerator.SoftGeneratorContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DenseGeneratorScreen extends BaseContainerScreen<DenseGeneratorContainer>
{
        public DenseGeneratorScreen(DenseGeneratorContainer container, Inventory inv, Component name)
        {
            super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/generator_gui.png"));
        }

        @Override
        protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
        {
            String energyState = String.format("Energy: %d/%d", menu.getEnergy(), menu.getMaxPowerCapacity());
            String energyProduction = String.format("+ %d", menu.getBurningItemCounter()*menu.getPowerGeneration());
            String burningItem = String.format("(%s)", menu.getBurningItemName());

            drawString(matrixStack, Minecraft.getInstance().font, energyState, 10, 10, 0xffffff);
            drawString(matrixStack, Minecraft.getInstance().font, energyProduction, 120, 10, 0x00ff00);
            //TODO:drawString(matrixStack, Minecraft.getInstance().font, burningItem, 50, 60, 0xff0000);
        }
}