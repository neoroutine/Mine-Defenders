package neoroutine.minetd.common.entities.antiking;

import neoroutine.minetd.MineTD;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;

public class AntikingModel extends HumanoidModel<AntikingEntity>
{
    public static final String BODY = "body";

    public static final ModelLayerLocation ANTIKING_LAYER = new ModelLayerLocation(new ResourceLocation(MineTD.MODID, "antiking"), BODY);

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshDefinition = createMesh(CubeDeformation.NONE, 0.6f);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public AntikingModel(ModelPart part)
    {
        super(part);
    }
}
