package neoroutine.minetd.common.entities.antiking;

import neoroutine.minetd.MineTD;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class AntikingRenderer extends HumanoidMobRenderer<AntikingEntity, AntikingModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(MineTD.MODID, "textures/entity/antiking.png");

    public AntikingRenderer(EntityRendererProvider.Context context)
    {
        super(context, new AntikingModel(context.bakeLayer(AntikingModel.ANTIKING_LAYER)), 1f);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(AntikingEntity entity)
    {
        return TEXTURE;
    }
}