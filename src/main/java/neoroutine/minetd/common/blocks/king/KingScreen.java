package neoroutine.minetd.common.blocks.king;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
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
        String health = String.format("%d/%d", menu.getKingHealth(), menu.getKingMaxHealth());

        String duplicatedItemCount = String.format("+%d", menu.getDuplicatedItemCounter());
        String duplicatedItemName = String.format("[%s]", menu.getDuplicatedItemName());

        String grandmaster = String.format("Grandmaster : %s", menu.getGrandmasterName());

        String uuid = menu.getGrandmasterUUID();
        if (uuid.length() > 20) { uuid = uuid.substring(0, 19);}
        String grandmasterUUID = String.format("UUID : %s...", uuid);

        drawString(matrixStack, Minecraft.getInstance().font, health, 60, 5, 0x00ff00);

        drawString(matrixStack, Minecraft.getInstance().font, grandmaster, 80, 20, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().font, grandmasterUUID, 70, 30, 0xffffff);

        drawString(matrixStack, Minecraft.getInstance().font, duplicatedItemCount, 10, 50, 0x00ff00);
        drawString(matrixStack, Minecraft.getInstance().font, duplicatedItemName, 10, 60, 0xff0000);


    }
}
