package neoroutine.minetd.common.blocks.kings.black;

import com.mojang.blaze3d.vertex.PoseStack;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.containerscreen.BaseContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlackKingScreen extends BaseContainerScreen<BlackKingContainer>
{
    public BlackKingScreen(BlackKingContainer container, Inventory inv, Component name)
    {
        super(container, inv, name, new ResourceLocation(MineTD.MODID, "textures/gui/king_gui.png"));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        String health = String.format("%d/%d", menu.getKingHealth(), menu.getKingMaxHealth());

        String duplicatedItemCount = String.format("%.2fs left", menu.getDuplicatedItemCounter()/20.0);
        String duplicatedItemName = String.format("[%s]", menu.getDuplicatedItemName());

        String grandmaster = String.format("GM : %s", menu.getGrandmasterName());
        String grandmasterPoints = String.format("Elo : %d", menu.getGrandmasterEloPoints());

        //String uuid = menu.getGrandmasterUUID();
        //if (uuid.length() > 20) { uuid = uuid.substring(0, 19);}
        //String grandmasterUUID = String.format("UUID : %s...", uuid);

        drawString(matrixStack, Minecraft.getInstance().font, health, 120, 5, 0x00ff00);

        drawString(matrixStack, Minecraft.getInstance().font, grandmaster, 10, 5, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().font, grandmasterPoints, 10, 15, 0xffffff);
        //drawString(matrixStack, Minecraft.getInstance().font, grandmasterUUID, 40, 60, 0xffffff);

        drawString(matrixStack, Minecraft.getInstance().font, duplicatedItemCount, 110, 30, 0x00ff00);


    }
}